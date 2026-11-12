package com.example.model

enum class UserRole {
    STUDENT,
    ADMIN,
    SUPER_ADMIN,
    CONTENT_MANAGER
}

data class Student(
    val id: String = "",
    val studentId: String = "", // e.g., API-2026-4891
    val name: String = "",
    val mobile: String = "",
    val email: String = "",
    val registrationDate: String = "",
    val targetCategoryId: String = "radiographer",
    val isActive: Boolean = true,
    val isBlocked: Boolean = false,
    val role: UserRole = UserRole.STUDENT,
    val enrolledTestIds: List<String> = emptyList(),
    val completedAttemptIds: List<String> = emptyList()
)

data class ExamCategory(
    val id: String,
    val name: String,
    val shortCode: String,
    val description: String,
    val totalTests: Int = 0,
    val totalVacancies: Int = 0
)

data class Question(
    val id: String,
    val section: String = "Core Subject",
    val text: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: Int, // 0 for A, 1 for B, 2 for C, 3 for D
    val explanation: String,
    val subjectTopic: String = ""
) {
    fun getOptions(): List<String> = listOf(optionA, optionB, optionC, optionD)
}

data class MockTest(
    val id: String,
    val categoryId: String,
    val title: String,
    val subtitle: String,
    val totalQuestions: Int = 100,
    val durationMinutes: Int = 90,
    val marksPerCorrect: Double = 1.0,
    val negativeMarks: Double = 0.25,
    val isPaid: Boolean = false,
    val price: Int = 0, // In INR
    val originalPrice: Int = 0,
    val isPublished: Boolean = true,
    val maxRetakes: Int = 1,
    val questions: List<Question> = emptyList()
) {
    val totalMarks: Double
        get() = totalQuestions * marksPerCorrect
}

enum class QuestionStatus {
    NOT_VISITED,
    NOT_ANSWERED,
    ANSWERED,
    MARKED_FOR_REVIEW,
    ANSWERED_AND_MARKED
}

data class ExamAttempt(
    val id: String,
    val testId: String,
    val testTitle: String,
    val categoryId: String,
    val studentId: String,
    val studentName: String,
    val startedAt: Long,
    val completedAt: Long = 0L,
    val timeTakenSeconds: Long = 0L,
    val userAnswers: Map<String, Int> = emptyMap(), // questionId -> selectedOptionIndex (0..3)
    val markedReviewIds: Set<String> = emptySet(),
    val totalQuestions: Int = 0,
    val correctCount: Int = 0,
    val incorrectCount: Int = 0,
    val unattemptedCount: Int = 0,
    val scoreObtained: Double = 0.0,
    val totalMarks: Double = 0.0,
    val accuracy: Double = 0.0,
    val percentile: Double = 0.0,
    val rank: Int = 1,
    val isSubmitted: Boolean = false
)

enum class MaterialType {
    PDF_DOCUMENT,
    GOOGLE_DRIVE,
    VIDEO_LECTURE,
    QUESTION_BANK
}

data class StudyMaterial(
    val id: String,
    val categoryId: String,
    val title: String,
    val subject: String,
    val type: MaterialType,
    val accessUrl: String,
    val fileSizeBytes: String = "2.4 MB",
    val isFree: Boolean = true,
    val uploadedDate: String = "Nov 2026"
)

data class JobNotification(
    val id: String,
    val title: String,
    val department: String,
    val hospitalOrganization: String,
    val location: String,
    val qualification: String,
    val experience: String,
    val salary: String,
    val vacancies: Int,
    val deadlineDate: String,
    val officialApplyLink: String,
    val notificationPdfUrl: String,
    val isOfficialVerified: Boolean = true,
    val categoryId: String,
    val postedDate: String = "Recent"
)

enum class PaymentStatus {
    SUCCESS,
    PENDING,
    FAILED,
    REFUNDED
}

data class PaymentTransaction(
    val id: String,
    val studentId: String,
    val studentName: String,
    val testId: String,
    val testTitle: String,
    val amountInr: Int,
    val razorpayOrderId: String,
    val razorpayPaymentId: String,
    val status: PaymentStatus,
    val timestamp: Long = System.currentTimeMillis()
)

data class Coupon(
    val code: String,
    val discountPercent: Int,
    val maxDiscountInr: Int,
    val description: String,
    val isActive: Boolean = true
)
