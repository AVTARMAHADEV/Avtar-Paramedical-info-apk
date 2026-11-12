package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.UserRole
import com.example.ui.CbtUiState
import com.example.ui.ParamedicalViewModel
import com.example.ui.Screen
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.components.RazorpayModal
import com.example.ui.screens.*
import com.example.ui.theme.AvtarTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ParamedicalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AvtarTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: ParamedicalViewModel) {
    val context = LocalContext.current
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentStudent by viewModel.repository.currentStudent.collectAsStateWithLifecycle()
    val allStudents by viewModel.repository.allStudents.collectAsStateWithLifecycle()
    val categories by viewModel.repository.categories.collectAsStateWithLifecycle()
    val mockTests by viewModel.repository.mockTests.collectAsStateWithLifecycle()
    val attempts by viewModel.repository.attempts.collectAsStateWithLifecycle()
    val studyMaterials by viewModel.repository.studyMaterials.collectAsStateWithLifecycle()
    val jobNotifications by viewModel.repository.jobNotifications.collectAsStateWithLifecycle()
    val transactions by viewModel.repository.transactions.collectAsStateWithLifecycle()
    val selectedCategoryId by viewModel.repository.selectedCategoryId.collectAsStateWithLifecycle()

    val cbtState by viewModel.cbtState.collectAsStateWithLifecycle()
    val checkoutState by viewModel.checkoutState.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    // Handle Toast messages
    LaunchedEffect(userMessage) {
        userMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Compose Back Navigation Handling
    BackHandler(enabled = true) {
        when (currentScreen) {
            is Screen.CbtEngine -> {
                // Prompt before quitting exam
                viewModel.toggleSubmitConfirm(true)
            }
            is Screen.CbtResult -> {
                viewModel.navigateTo(Screen.Dashboard)
            }
            is Screen.AdminPanel -> {
                viewModel.navigateTo(Screen.Dashboard)
            }
            Screen.Dashboard -> {
                // In root, allow standard back
            }
            else -> {
                if (!viewModel.navigateBack()) {
                    viewModel.navigateTo(Screen.Dashboard)
                }
            }
        }
    }

    val isCbtEngineActive = currentScreen is Screen.CbtEngine
    val isPrimaryTab = currentScreen in listOf(
        Screen.Dashboard,
        Screen.Exams,
        Screen.Study,
        Screen.Jobs,
        Screen.Profile
    )

    Scaffold(
        topBar = {
            if (!isCbtEngineActive && currentScreen !is Screen.AdminPanel && currentScreen !is Screen.AuthScreen) {
                AppTopBar(
                    student = currentStudent,
                    currentScreen = currentScreen,
                    onNavigateToAdmin = { viewModel.navigateTo(Screen.AdminPanel) },
                    onBackClick = if (!isPrimaryTab) {
                        { viewModel.navigateBack() }
                    } else null
                )
            }
        },
        bottomBar = {
            if (isPrimaryTab && currentStudent != null) {
                AppBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { viewModel.navigateTo(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Render active screen
            if (currentStudent == null || currentScreen is Screen.AuthScreen) {
                AuthScreen(viewModel = viewModel, categories = categories)
            } else {
                when (val screen = currentScreen) {
                    Screen.Dashboard -> {
                        DashboardScreen(
                            viewModel = viewModel,
                            student = currentStudent,
                            categories = categories,
                            mockTests = mockTests,
                            attempts = attempts,
                            selectedCategoryId = selectedCategoryId,
                            onSelectCategory = { viewModel.repository.setSelectedCategory(it) }
                        )
                    }
                    Screen.Exams -> {
                        ExamsScreen(
                            viewModel = viewModel,
                            student = currentStudent,
                            categories = categories,
                            mockTests = mockTests
                        )
                    }
                    Screen.Study -> {
                        StudyMaterialsScreen(
                            viewModel = viewModel,
                            categories = categories,
                            studyMaterials = studyMaterials
                        )
                    }
                    Screen.Jobs -> {
                        JobsScreen(
                            viewModel = viewModel,
                            categories = categories,
                            jobNotifications = jobNotifications
                        )
                    }
                    Screen.Profile -> {
                        ProfileScreen(
                            viewModel = viewModel,
                            student = currentStudent
                        )
                    }
                    is Screen.CbtEngine -> {
                        CbtEngineScreen(
                            viewModel = viewModel,
                            cbtState = cbtState
                        )
                    }
                    is Screen.CbtResult -> {
                        CbtResultScreen(
                            viewModel = viewModel,
                            attemptId = screen.attemptId,
                            onBackToHome = { viewModel.navigateTo(Screen.Dashboard) }
                        )
                    }
                    Screen.AdminPanel -> {
                        AdminPanelScreen(
                            viewModel = viewModel,
                            students = allStudents,
                            categories = categories,
                            mockTests = mockTests,
                            attempts = attempts,
                            transactions = transactions,
                            onBack = { viewModel.navigateTo(Screen.Dashboard) }
                        )
                    }
                    Screen.AuthScreen -> {
                        AuthScreen(viewModel = viewModel, categories = categories)
                    }
                }
            }

            // Razorpay Payment Modal
            if (checkoutState.testToBuy != null) {
                RazorpayModal(
                    state = checkoutState,
                    onApplyCoupon = { viewModel.applyCoupon(it) },
                    onProceedPayment = { viewModel.processRazorpayPayment(it) },
                    onDismiss = { viewModel.closePaymentCheckout() },
                    onStartExamNow = { test ->
                        viewModel.startCbtExam(test.id)
                    }
                )
            }
        }
    }
}
