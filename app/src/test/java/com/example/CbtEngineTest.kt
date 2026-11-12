package com.example

import com.example.data.ParamedicalRepository
import com.example.data.ParamedicalSeedData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CbtEngineTest {

    private lateinit var repository: ParamedicalRepository

    @Before
    fun setUp() {
        repository = ParamedicalRepository()
    }

    @Test
    fun testStudentRegistrationAndUniqueIdGeneration() {
        val uniqueId = repository.generateStudentId()
        assertTrue("Student ID must start with API-2026-", uniqueId.startsWith("API-2026-"))
        assertEquals("Student ID length must be 13", 13, uniqueId.length)

        val regResult = repository.registerStudent(
            name = "Aman Kumar",
            mobile = "9988776655",
            targetCategoryId = "radiographer"
        )
        assertTrue("Registration must succeed", regResult.isSuccess)
        val student = regResult.getOrNull()
        assertNotNull(student)
        assertEquals("Aman Kumar", student?.name)
        assertTrue(student?.studentId?.startsWith("API-2026-") == true)
    }

    @Test
    fun testCbtScoringAndNegativeMarking() {
        val test = ParamedicalSeedData.mockTests.first { it.id == "rad_free_demo" }
        assertNotNull(test)

        // rad_q1: correct is option 2
        // rad_q2: correct is option 0
        // Simulate: 1 correct, 1 incorrect
        val userAnswers = mapOf(
            "rad_q1" to 2, // Correct (+1.0)
            "rad_q2" to 3  // Incorrect (-0.25)
        )

        val attempt = repository.submitExamAttempt(
            testId = test.id,
            userAnswers = userAnswers,
            markedReviewIds = emptySet(),
            timeTakenSeconds = 120
        )

        assertEquals("Correct count must be 1", 1, attempt.correctCount)
        assertEquals("Incorrect count must be 1", 1, attempt.incorrectCount)
        assertEquals(
            "Score must be 1.0 - 0.25 = 0.75",
            0.75,
            attempt.scoreObtained,
            0.001
        )
        assertEquals("Accuracy must be 50.0%", 50.0, attempt.accuracy, 0.01)
        assertTrue("Attempt must be marked submitted", attempt.isSubmitted)
    }

    @Test
    fun testCouponDiscounts() {
        val basePrice = 499

        // AVTAR50 gives 50% capped at 200 => 499 - 200 = 299
        val discountedAvtar = repository.applyCoupon("AVTAR50", basePrice)
        assertEquals(299, discountedAvtar)

        // FIRST100 gives 100% capped at 500 => 499 - 499 = 0
        val discountedFree = repository.applyCoupon("FIRST100", basePrice)
        assertEquals(0, discountedFree)

        // Invalid coupon returns original price
        val invalidCoupon = repository.applyCoupon("INVALID99", basePrice)
        assertEquals(basePrice, invalidCoupon)
    }

    @Test
    fun testPaymentActivationAndAccessGrant() {
        val paidTestId = "rad_mega_series_1"
        assertFalse(
            "Initially student shouldn't have access to paid test",
            repository.isTestPurchased(paidTestId)
        )

        val result = repository.verifyAndActivateTestPurchase(
            testId = paidTestId,
            amountInr = 299,
            razorpayOrderId = "order_test_9921",
            razorpayPaymentId = "pay_test_0012"
        )

        assertTrue("Payment verification must succeed", result.isSuccess)
        assertTrue(
            "Student must now have access to paid test",
            repository.isTestPurchased(paidTestId)
        )
    }
}
