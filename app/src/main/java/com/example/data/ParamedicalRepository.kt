package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class ParamedicalRepository {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private val simpleDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // Default registered student for quick testing / development
    private val defaultStudent = Student(
        id = "stu_001",
        studentId = "API-2026-4892",
        name = "Vikram Sharma",
        mobile = "9876543210",
        email = "vikram.paramed@gmail.com",
        registrationDate = "10 Nov 2026",
        targetCategoryId = "radiographer",
        isActive = true,
        isBlocked = false,
        role = UserRole.STUDENT,
        enrolledTestIds = listOf("rad_free_demo", "mlt_free_demo"),
        completedAttemptIds = emptyList()
    )

    // Current logged in user (null if logged out)
    private val _currentStudent = MutableStateFlow<Student?>(defaultStudent)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    // Students list for Admin Management
    private val _allStudents = MutableStateFlow<List<Student>>(
        listOf(
            defaultStudent,
            Student(
                id = "stu_002",
                studentId = "API-2026-1024",
                name = "Pooja Verma",
                mobile = "9812345678",
                email = "pooja.lab@gmail.com",
                registrationDate = "08 Nov 2026",
                targetCategoryId = "lab_technician",
                isActive = true,
                isBlocked = false,
                role = UserRole.STUDENT,
                enrolledTestIds = listOf("mlt_free_demo", "mlt_cbt_master_pack")
            ),
            Student(
                id = "stu_003",
                studentId = "API-2026-9051",
                name = "Rahul Deshmukh",
                mobile = "9765432109",
                email = "rahul.pharm@gmail.com",
                registrationDate = "05 Nov 2026",
                targetCategoryId = "pharmacy_officer",
                isActive = false,
                isBlocked = true, // Blocked example
                role = UserRole.STUDENT,
                enrolledTestIds = emptyList()
            ),
            Student(
                id = "admin_001",
                studentId = "API-ADMIN-01",
                name = "Dr. S. K. Avtar (Super Admin)",
                mobile = "9999999999",
                email = "admin@avtarparamedical.in",
                registrationDate = "01 Jan 2026",
                targetCategoryId = "nursing_officer",
                isActive = true,
                isBlocked = false,
                role = UserRole.SUPER_ADMIN,
                enrolledTestIds = ParamedicalSeedData.mockTests.map { it.id }
            )
        )
    )
    val allStudents: StateFlow<List<Student>> = _allStudents.asStateFlow()

    // Categories
    private val _categories = MutableStateFlow<List<ExamCategory>>(ParamedicalSeedData.categories)
    val categories: StateFlow<List<ExamCategory>> = _categories.asStateFlow()

    // Mock Tests
    private val _mockTests = MutableStateFlow<List<MockTest>>(ParamedicalSeedData.mockTests)
    val mockTests: StateFlow<List<MockTest>> = _mockTests.asStateFlow()

    // Exam Attempts history
    private val _attempts = MutableStateFlow<List<ExamAttempt>>(emptyList())
    val attempts: StateFlow<List<ExamAttempt>> = _attempts.asStateFlow()

    // Study Materials
    private val _studyMaterials = MutableStateFlow<List<StudyMaterial>>(ParamedicalSeedData.studyMaterials)
    val studyMaterials: StateFlow<List<StudyMaterial>> = _studyMaterials.asStateFlow()

    // Job Notifications
    private val _jobNotifications = MutableStateFlow<List<JobNotification>>(ParamedicalSeedData.jobNotifications)
    val jobNotifications: StateFlow<List<JobNotification>> = _jobNotifications.asStateFlow()

    // Payment Transactions
    private val _transactions = MutableStateFlow<List<PaymentTransaction>>(
        listOf(
            PaymentTransaction(
                id = "tx_001",
                studentId = "API-2026-1024",
                studentName = "Pooja Verma",
                testId = "mlt_cbt_master_pack",
                testTitle = "ESIC & State Paramedical Lab Technician Mock 2026",
                amountInr = 349,
                razorpayOrderId = "order_Oq7Klx98V12a",
                razorpayPaymentId = "pay_Oq7M45p928",
                status = PaymentStatus.SUCCESS,
                timestamp = System.currentTimeMillis() - 86400000L * 2
            )
        )
    )
    val transactions: StateFlow<List<PaymentTransaction>> = _transactions.asStateFlow()

    // Selected Target Category for filtering
    private val _selectedCategoryId = MutableStateFlow<String>("radiographer")
    val selectedCategoryId: StateFlow<String> = _selectedCategoryId.asStateFlow()

    fun setSelectedCategory(catId: String) {
        _selectedCategoryId.value = catId
    }

    // AUTHENTICATION & STUDENT MANAGEMENT
    fun generateStudentId(): String {
        val rand = Random.nextInt(1000, 9999)
        return "API-2026-$rand"
    }

    fun registerStudent(name: String, mobile: String, targetCategoryId: String): Result<Student> {
        val existing = _allStudents.value.find { it.mobile == mobile }
        if (existing != null) {
            return Result.failure(Exception("Mobile number is already registered. Please login."))
        }
        val newStudent = Student(
            id = "stu_${UUID.randomUUID().toString().take(8)}",
            studentId = generateStudentId(),
            name = name.trim(),
            mobile = mobile.trim(),
            email = "",
            registrationDate = simpleDate.format(Date()),
            targetCategoryId = targetCategoryId,
            isActive = true,
            isBlocked = false,
            role = UserRole.STUDENT,
            enrolledTestIds = listOf("rad_free_demo", "mlt_free_demo", "pharm_free_demo", "nurs_free_demo")
        )
        _allStudents.value = _allStudents.value + newStudent
        _currentStudent.value = newStudent
        return Result.success(newStudent)
    }

    fun loginWithMobile(mobile: String, enteredOtp: String): Result<Student> {
        if (enteredOtp != "1234" && enteredOtp != "2026") {
            return Result.failure(Exception("Invalid OTP! For development/testing, please enter '1234' or '2026'."))
        }
        val student = _allStudents.value.find { it.mobile == mobile }
            ?: return Result.failure(Exception("Mobile number not found. Please register your account."))

        if (student.isBlocked || !student.isActive) {
            return Result.failure(Exception("Account is inactive or blocked by administrator. Please contact support."))
        }

        _currentStudent.value = student
        return Result.success(student)
    }

    fun logout() {
        _currentStudent.value = null
    }

    fun logoutFromAllDevices() {
        _currentStudent.value = null
    }

    fun switchRoleForTesting(role: UserRole) {
        val current = _currentStudent.value ?: defaultStudent
        val updated = current.copy(role = role)
        _currentStudent.value = updated
    }

    // CBT ENGINE OPERATIONS
    fun getTestById(testId: String): MockTest? {
        return _mockTests.value.find { it.id == testId }
    }

    fun submitExamAttempt(
        testId: String,
        userAnswers: Map<String, Int>,
        markedReviewIds: Set<String>,
        timeTakenSeconds: Long
    ): ExamAttempt {
        val test = getTestById(testId) ?: throw IllegalArgumentException("Test not found: $testId")
        val student = _currentStudent.value ?: defaultStudent

        var correct = 0
        var incorrect = 0
        var unattempted = 0
        var score = 0.0

        for (q in test.questions) {
            val chosen = userAnswers[q.id]
            if (chosen == null) {
                unattempted++
            } else if (chosen == q.correctOption) {
                correct++
                score += test.marksPerCorrect
            } else {
                incorrect++
                score -= test.negativeMarks
            }
        }
        if (score < 0) score = 0.0

        val totalQuestions = test.questions.size
        val accuracy = if ((correct + incorrect) > 0) {
            (correct.toDouble() / (correct + incorrect).toDouble()) * 100.0
        } else {
            0.0
        }

        val percentile = when {
            accuracy >= 90 -> 98.5
            accuracy >= 75 -> 89.2
            accuracy >= 50 -> 68.0
            else -> 42.0
        }

        val attempt = ExamAttempt(
            id = "att_${UUID.randomUUID().toString().take(8)}",
            testId = test.id,
            testTitle = test.title,
            categoryId = test.categoryId,
            studentId = student.studentId,
            studentName = student.name,
            startedAt = System.currentTimeMillis() - (timeTakenSeconds * 1000),
            completedAt = System.currentTimeMillis(),
            timeTakenSeconds = timeTakenSeconds,
            userAnswers = userAnswers,
            markedReviewIds = markedReviewIds,
            totalQuestions = totalQuestions,
            correctCount = correct,
            incorrectCount = incorrect,
            unattemptedCount = unattempted,
            scoreObtained = score,
            totalMarks = test.totalMarks,
            accuracy = accuracy,
            percentile = percentile,
            rank = Random.nextInt(1, 15),
            isSubmitted = true
        )

        _attempts.value = listOf(attempt) + _attempts.value

        // Also record attempt in student profile
        val updatedStudent = student.copy(
            completedAttemptIds = student.completedAttemptIds + attempt.id
        )
        _currentStudent.value = updatedStudent
        _allStudents.value = _allStudents.value.map {
            if (it.studentId == student.studentId) updatedStudent else it
        }

        return attempt
    }

    fun getAttemptById(attemptId: String): ExamAttempt? {
        return _attempts.value.find { it.id == attemptId }
    }

    // PAYMENT & ENROLLMENT
    fun verifyAndActivateTestPurchase(
        testId: String,
        amountInr: Int,
        razorpayOrderId: String,
        razorpayPaymentId: String
    ): Result<PaymentTransaction> {
        val student = _currentStudent.value ?: return Result.failure(Exception("Student not logged in"))
        val test = getTestById(testId) ?: return Result.failure(Exception("Test not found"))

        // Signature check simulation: In real production backend, HMAC SHA256 of orderId|paymentId with secret key
        val transaction = PaymentTransaction(
            id = "tx_${UUID.randomUUID().toString().take(8)}",
            studentId = student.studentId,
            studentName = student.name,
            testId = test.id,
            testTitle = test.title,
            amountInr = amountInr,
            razorpayOrderId = razorpayOrderId,
            razorpayPaymentId = razorpayPaymentId,
            status = PaymentStatus.SUCCESS,
            timestamp = System.currentTimeMillis()
        )

        _transactions.value = listOf(transaction) + _transactions.value

        // Grant access
        val enrolled = (student.enrolledTestIds + test.id).distinct()
        val updatedStudent = student.copy(enrolledTestIds = enrolled)
        _currentStudent.value = updatedStudent
        _allStudents.value = _allStudents.value.map {
            if (it.studentId == student.studentId) updatedStudent else it
        }

        return Result.success(transaction)
    }

    fun isTestPurchased(testId: String): Boolean {
        val student = _currentStudent.value ?: return false
        val test = getTestById(testId) ?: return false
        if (!test.isPaid) return true
        if (student.role == UserRole.ADMIN || student.role == UserRole.SUPER_ADMIN) return true
        return student.enrolledTestIds.contains(testId)
    }

    fun applyCoupon(code: String, testPrice: Int): Int {
        val coupon = ParamedicalSeedData.validCoupons.find { it.code.equals(code.trim(), ignoreCase = true) }
            ?: return testPrice
        val discount = (testPrice * coupon.discountPercent) / 100
        val finalDiscount = discount.coerceAtMost(coupon.maxDiscountInr)
        return (testPrice - finalDiscount).coerceAtLeast(0)
    }

    // ADMIN MANAGEMENT ACTIONS
    fun adminCreateCategory(name: String, shortCode: String, description: String) {
        val newCat = ExamCategory(
            id = shortCode.lowercase().replace(" ", "_"),
            name = name,
            shortCode = shortCode,
            description = description,
            totalTests = 0,
            totalVacancies = 0
        )
        _categories.value = _categories.value + newCat
    }

    fun adminCreateMockTest(
        categoryId: String,
        title: String,
        subtitle: String,
        durationMinutes: Int,
        isPaid: Boolean,
        price: Int,
        questions: List<Question>
    ) {
        val newTest = MockTest(
            id = "test_${UUID.randomUUID().toString().take(8)}",
            categoryId = categoryId,
            title = title,
            subtitle = subtitle,
            totalQuestions = questions.size,
            durationMinutes = durationMinutes,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = isPaid,
            price = price,
            originalPrice = if (isPaid) price * 2 else 0,
            isPublished = true,
            questions = questions
        )
        _mockTests.value = _mockTests.value + newTest
    }

    fun adminToggleStudentStatus(studentId: String) {
        _allStudents.value = _allStudents.value.map {
            if (it.studentId == studentId) {
                it.copy(isActive = !it.isActive, isBlocked = it.isActive)
            } else it
        }
        if (_currentStudent.value?.studentId == studentId) {
            val updated = _allStudents.value.find { it.studentId == studentId }
            _currentStudent.value = updated
        }
    }

    fun adminCreateStudent(name: String, mobile: String, targetCategoryId: String): Student {
        val newStudent = Student(
            id = "stu_${UUID.randomUUID().toString().take(8)}",
            studentId = generateStudentId(),
            name = name,
            mobile = mobile,
            email = "",
            registrationDate = simpleDate.format(Date()),
            targetCategoryId = targetCategoryId,
            isActive = true,
            isBlocked = false,
            role = UserRole.STUDENT,
            enrolledTestIds = listOf("rad_free_demo", "mlt_free_demo")
        )
        _allStudents.value = _allStudents.value + newStudent
        return newStudent
    }

    fun adminAddJobNotification(
        title: String,
        department: String,
        hospital: String,
        location: String,
        qualification: String,
        vacancies: Int,
        deadline: String,
        applyUrl: String,
        categoryId: String
    ) {
        val newJob = JobNotification(
            id = "job_${UUID.randomUUID().toString().take(8)}",
            title = title,
            department = department,
            hospitalOrganization = hospital,
            location = location,
            qualification = qualification,
            experience = "As per notification",
            salary = "Standard Gov Pay Scale",
            vacancies = vacancies,
            deadlineDate = deadline,
            officialApplyLink = applyUrl,
            notificationPdfUrl = applyUrl,
            isOfficialVerified = true,
            categoryId = categoryId,
            postedDate = simpleDate.format(Date())
        )
        _jobNotifications.value = listOf(newJob) + _jobNotifications.value
    }

    fun adminAddStudyMaterial(
        categoryId: String,
        title: String,
        subject: String,
        type: MaterialType,
        accessUrl: String,
        isFree: Boolean
    ) {
        val newMat = StudyMaterial(
            id = "mat_${UUID.randomUUID().toString().take(8)}",
            categoryId = categoryId,
            title = title,
            subject = subject,
            type = type,
            accessUrl = accessUrl,
            fileSizeBytes = "Cloud Drive / PDF",
            isFree = isFree,
            uploadedDate = simpleDate.format(Date())
        )
        _studyMaterials.value = listOf(newMat) + _studyMaterials.value
    }
}
