package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ParamedicalRepository
import com.example.data.ParamedicalSeedData
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Dashboard : Screen()
    object Exams : Screen()
    object Study : Screen()
    object Jobs : Screen()
    object Profile : Screen()
    data class CbtEngine(val testId: String) : Screen()
    data class CbtResult(val attemptId: String) : Screen()
    object AdminPanel : Screen()
    object AuthScreen : Screen()
}

data class CbtUiState(
    val test: MockTest? = null,
    val currentQuestionIndex: Int = 0,
    val selectedOptions: Map<String, Int> = emptyMap(), // questionId -> optionIndex
    val markedReviewIds: Set<String> = emptySet(),
    val remainingSeconds: Long = 0L,
    val isTimerRunning: Boolean = false,
    val showSubmitConfirmDialog: Boolean = false,
    val showQuestionPalette: Boolean = false
) {
    val currentQuestion: Question?
        get() = test?.questions?.getOrNull(currentQuestionIndex)

    fun getStatusForQuestion(questionId: String): QuestionStatus {
        val isAnswered = selectedOptions.containsKey(questionId)
        val isMarked = markedReviewIds.contains(questionId)

        return when {
            isAnswered && isMarked -> QuestionStatus.ANSWERED_AND_MARKED
            isMarked -> QuestionStatus.MARKED_FOR_REVIEW
            isAnswered -> QuestionStatus.ANSWERED
            else -> QuestionStatus.NOT_ANSWERED
        }
    }
}

data class PaymentCheckoutState(
    val testToBuy: MockTest? = null,
    val appliedCoupon: Coupon? = null,
    val couponInput: String = "",
    val couponError: String? = null,
    val payableAmount: Int = 0,
    val isProcessing: Boolean = false,
    val paymentSuccessTx: PaymentTransaction? = null
)

class ParamedicalViewModel(
    val repository: ParamedicalRepository = ParamedicalRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenStack = mutableListOf<Screen>(Screen.Dashboard)

    // CBT Engine State
    private val _cbtState = MutableStateFlow(CbtUiState())
    val cbtState: StateFlow<CbtUiState> = _cbtState.asStateFlow()

    private var timerJob: Job? = null

    // Payment Sheet State
    private val _checkoutState = MutableStateFlow(PaymentCheckoutState())
    val checkoutState: StateFlow<PaymentCheckoutState> = _checkoutState.asStateFlow()

    // Notification toast / banner
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun showToast(message: String) {
        _userMessage.value = message
    }

    fun clearToast() {
        _userMessage.value = null
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _screenStack.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenStack.size > 1) {
            _screenStack.removeAt(_screenStack.size - 1)
            _currentScreen.value = _screenStack.last()
            return true
        }
        return false
    }

    // AUTH ACTIONS
    fun handleRegister(name: String, mobile: String, categoryId: String): Boolean {
        if (name.isBlank() || mobile.length < 10) {
            showToast("Please enter a valid 10-digit mobile number and name.")
            return false
        }
        val result = repository.registerStudent(name, mobile, categoryId)
        return if (result.isSuccess) {
            showToast("Welcome! Student ID: ${result.getOrNull()?.studentId}")
            _currentScreen.value = Screen.Dashboard
            true
        } else {
            showToast(result.exceptionOrNull()?.message ?: "Registration failed")
            false
        }
    }

    fun handleLogin(mobile: String, otp: String): Boolean {
        val result = repository.loginWithMobile(mobile, otp)
        return if (result.isSuccess) {
            showToast("Login Successful! Welcome ${result.getOrNull()?.name}")
            _currentScreen.value = Screen.Dashboard
            true
        } else {
            showToast(result.exceptionOrNull()?.message ?: "Login failed")
            false
        }
    }

    fun handleLogout() {
        repository.logout()
        _currentScreen.value = Screen.AuthScreen
        showToast("Logged out successfully.")
    }

    // CBT ENGINE WORKFLOWS
    fun startCbtExam(testId: String) {
        val test = repository.getTestById(testId) ?: return
        if (test.isPaid && !repository.isTestPurchased(test.id)) {
            openPaymentCheckout(test)
            return
        }

        timerJob?.cancel()
        val durationSeconds = test.durationMinutes * 60L
        _cbtState.value = CbtUiState(
            test = test,
            currentQuestionIndex = 0,
            selectedOptions = emptyMap(),
            markedReviewIds = emptySet(),
            remainingSeconds = durationSeconds,
            isTimerRunning = true,
            showSubmitConfirmDialog = false,
            showQuestionPalette = false
        )

        // Start countdown timer
        timerJob = viewModelScope.launch {
            while (_cbtState.value.remainingSeconds > 0 && _cbtState.value.isTimerRunning) {
                delay(1000L)
                val newRemaining = _cbtState.value.remainingSeconds - 1
                _cbtState.value = _cbtState.value.copy(remainingSeconds = newRemaining)
                if (newRemaining <= 0L) {
                    // Time up! Auto submit exam
                    autoSubmitCbtExam()
                    break
                }
            }
        }

        navigateTo(Screen.CbtEngine(testId))
    }

    fun selectOption(optionIndex: Int) {
        val state = _cbtState.value
        val q = state.currentQuestion ?: return
        val currentAnswers = state.selectedOptions.toMutableMap()
        currentAnswers[q.id] = optionIndex
        _cbtState.value = state.copy(selectedOptions = currentAnswers)
    }

    fun clearResponse() {
        val state = _cbtState.value
        val q = state.currentQuestion ?: return
        val currentAnswers = state.selectedOptions.toMutableMap()
        currentAnswers.remove(q.id)
        _cbtState.value = state.copy(selectedOptions = currentAnswers)
    }

    fun toggleMarkForReview() {
        val state = _cbtState.value
        val q = state.currentQuestion ?: return
        val currentReviews = state.markedReviewIds.toMutableSet()
        if (currentReviews.contains(q.id)) {
            currentReviews.remove(q.id)
        } else {
            currentReviews.add(q.id)
        }
        _cbtState.value = state.copy(markedReviewIds = currentReviews)
    }

    fun nextQuestion() {
        val state = _cbtState.value
        val total = state.test?.questions?.size ?: 0
        if (state.currentQuestionIndex < total - 1) {
            _cbtState.value = state.copy(currentQuestionIndex = state.currentQuestionIndex + 1)
        }
    }

    fun previousQuestion() {
        val state = _cbtState.value
        if (state.currentQuestionIndex > 0) {
            _cbtState.value = state.copy(currentQuestionIndex = state.currentQuestionIndex - 1)
        }
    }

    fun jumpToQuestion(index: Int) {
        val state = _cbtState.value
        val total = state.test?.questions?.size ?: 0
        if (index in 0 until total) {
            _cbtState.value = state.copy(
                currentQuestionIndex = index,
                showQuestionPalette = false
            )
        }
    }

    fun toggleQuestionPalette(show: Boolean) {
        _cbtState.value = _cbtState.value.copy(showQuestionPalette = show)
    }

    fun toggleSubmitConfirm(show: Boolean) {
        _cbtState.value = _cbtState.value.copy(showSubmitConfirmDialog = show)
    }

    private fun autoSubmitCbtExam() {
        showToast("Time expired! Automatically submitting your CBT exam.")
        performSubmission()
    }

    fun confirmManualSubmission() {
        _cbtState.value = _cbtState.value.copy(showSubmitConfirmDialog = false)
        performSubmission()
    }

    private fun performSubmission() {
        timerJob?.cancel()
        val state = _cbtState.value
        val test = state.test ?: return

        val totalDurationSeconds = test.durationMinutes * 60L
        val timeTaken = (totalDurationSeconds - state.remainingSeconds).coerceAtLeast(1)

        val attempt = repository.submitExamAttempt(
            testId = test.id,
            userAnswers = state.selectedOptions,
            markedReviewIds = state.markedReviewIds,
            timeTakenSeconds = timeTaken
        )

        _cbtState.value = _cbtState.value.copy(isTimerRunning = false)
        _currentScreen.value = Screen.CbtResult(attempt.id)
    }

    // PAYMENT & RAZORPAY INTEGRATION
    fun openPaymentCheckout(test: MockTest) {
        _checkoutState.value = PaymentCheckoutState(
            testToBuy = test,
            payableAmount = test.price,
            couponInput = "",
            couponError = null,
            appliedCoupon = null,
            isProcessing = false,
            paymentSuccessTx = null
        )
    }

    fun applyCoupon(code: String) {
        val test = _checkoutState.value.testToBuy ?: return
        val discounted = repository.applyCoupon(code, test.price)
        if (discounted < test.price) {
            val coupon = ParamedicalSeedData.validCoupons.find { it.code.equals(code.trim(), ignoreCase = true) }
            _checkoutState.value = _checkoutState.value.copy(
                appliedCoupon = coupon,
                payableAmount = discounted,
                couponError = null
            )
            showToast("Coupon Applied! Saved ₹${test.price - discounted}")
        } else {
            _checkoutState.value = _checkoutState.value.copy(
                couponError = "Invalid or expired coupon code. Try 'AVTAR50' or 'PARAMED2026'"
            )
        }
    }

    fun closePaymentCheckout() {
        _checkoutState.value = PaymentCheckoutState()
    }

    fun processRazorpayPayment(simulatedSuccess: Boolean = true) {
        val current = _checkoutState.value
        val test = current.testToBuy ?: return

        _checkoutState.value = current.copy(isProcessing = true)

        viewModelScope.launch {
            delay(1500L) // Simulate secure gateway handshake
            if (simulatedSuccess) {
                val orderId = "order_${System.currentTimeMillis().toString().takeLast(8)}"
                val paymentId = "pay_rzp_${System.currentTimeMillis().toString().takeLast(9)}"

                val result = repository.verifyAndActivateTestPurchase(
                    testId = test.id,
                    amountInr = current.payableAmount,
                    razorpayOrderId = orderId,
                    razorpayPaymentId = paymentId
                )

                if (result.isSuccess) {
                    val tx = result.getOrNull()
                    _checkoutState.value = _checkoutState.value.copy(
                        isProcessing = false,
                        paymentSuccessTx = tx
                    )
                    showToast("Payment Verified & Test Series Activated!")
                } else {
                    _checkoutState.value = _checkoutState.value.copy(isProcessing = false)
                    showToast(result.exceptionOrNull()?.message ?: "Payment verification failed")
                }
            } else {
                _checkoutState.value = _checkoutState.value.copy(isProcessing = false)
                showToast("Payment cancelled or failed.")
            }
        }
    }
}
