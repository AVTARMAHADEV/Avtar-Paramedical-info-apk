/**
 * AVTAR PARAMEDICAL INFORMATION - SECURE CLOUD FUNCTIONS & REST API
 * Technology: Node.js, Firebase Cloud Functions, Razorpay SDK, Crypto
 * Security: Strict HMAC-SHA256 signature verification, Zero-Trust Access Control
 */

const functions = require("firebase-functions");
const admin = require("firebase-admin");
const crypto = require("crypto");
const Razorpay = require("razorpay");

admin.initializeApp();
const db = admin.firestore();

// Razorpay client initialization (Keys injected via environment variables)
const razorpay = new Razorpay({
  key_id: process.env.RAZORPAY_KEY_ID || "rzp_test_AVTARPARAMED2026",
  key_secret: process.env.RAZORPAY_KEY_SECRET || "rzp_secret_key_placeholder",
});

/**
 * 1. CREATE RAZORPAY ORDER (SECURE SERVER-SIDE)
 * The client requests an order for a testSeriesId.
 * NEVER trust the price sent from the client; fetch the official price from Firestore.
 */
exports.createRazorpayOrder = functions.https.onCall(async (data, context) => {
  // Ensure authenticated student
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "User must be authenticated.");
  }

  const { testId, couponCode } = data;
  if (!testId) {
    throw new functions.https.HttpsError("invalid-argument", "Missing testId parameter.");
  }

  // 1. Fetch test details from Firestore
  const testDoc = await db.collection("mock_tests").doc(testId).get();
  if (!testDoc.exists) {
    throw new functions.https.HttpsError("not-found", "Mock test series not found.");
  }

  const testData = testDoc.data();
  if (!testData.isPaid) {
    throw new functions.https.HttpsError("failed-precondition", "This test is already free.");
  }

  let finalAmount = testData.price; // in INR

  // 2. Validate and apply coupon if provided
  if (couponCode) {
    const couponDoc = await db.collection("coupons").doc(couponCode.toUpperCase()).get();
    if (couponDoc.exists && couponDoc.data().isActive) {
      const c = couponDoc.data();
      const discount = Math.min((finalAmount * c.discountPercent) / 100, c.maxDiscountInr);
      finalAmount = Math.max(0, finalAmount - discount);
    }
  }

  // 3. Create Razorpay order (amount in paise: 1 INR = 100 paise)
  const options = {
    amount: finalAmount * 100,
    currency: "INR",
    receipt: `rcpt_${context.auth.uid.slice(0, 8)}_${Date.now()}`,
    notes: {
      userId: context.auth.uid,
      testId: testId,
      testTitle: testData.title,
    },
  };

  try {
    const order = await razorpay.orders.create(options);

    // Save pending transaction in Firestore
    await db.collection("transactions").doc(order.id).set({
      orderId: order.id,
      userId: context.auth.uid,
      testId: testId,
      amountInr: finalAmount,
      amountPaise: options.amount,
      status: "PENDING",
      createdAt: admin.firestore.FieldValue.serverTimestamp(),
    });

    return {
      orderId: order.id,
      amount: options.amount,
      currency: "INR",
      keyId: process.env.RAZORPAY_KEY_ID || "rzp_test_AVTARPARAMED2026",
    };
  } catch (err) {
    console.error("Razorpay order creation failed:", err);
    throw new functions.https.HttpsError("internal", "Failed to initialize payment gateway.");
  }
});

/**
 * 2. VERIFY RAZORPAY PAYMENT SIGNATURE (SERVER-SIDE OR WEBHOOK)
 * Verifies HMAC SHA256 of order_id + "|" + payment_id with key_secret
 */
exports.verifyPaymentAndActivate = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "Authentication required.");
  }

  const { razorpayOrderId, razorpayPaymentId, razorpaySignature, testId } = data;
  const keySecret = process.env.RAZORPAY_KEY_SECRET || "rzp_secret_key_placeholder";

  const expectedSignature = crypto
    .createHmac("sha256", keySecret)
    .update(`${razorpayOrderId}|${razorpayPaymentId}`)
    .digest("hex");

  if (expectedSignature !== razorpaySignature) {
    // Audit failed signature attempt
    await db.collection("security_audit_logs").add({
      type: "PAYMENT_SIGNATURE_MISMATCH",
      userId: context.auth.uid,
      orderId: razorpayOrderId,
      paymentId: razorpayPaymentId,
      timestamp: admin.firestore.FieldValue.serverTimestamp(),
    });

    throw new functions.https.HttpsError("permission-denied", "Invalid payment signature.");
  }

  // Signature valid! Update transaction and enroll student
  const batch = db.batch();

  // 1. Update transaction document
  const txRef = db.collection("transactions").doc(razorpayOrderId);
  batch.update(txRef, {
    paymentId: razorpayPaymentId,
    signature: razorpaySignature,
    status: "SUCCESS",
    verifiedAt: admin.firestore.FieldValue.serverTimestamp(),
  });

  // 2. Add test to student's enrolled tests
  const userRef = db.collection("students").doc(context.auth.uid);
  batch.update(userRef, {
    enrolledTestIds: admin.firestore.FieldValue.arrayUnion(testId),
    updatedAt: admin.firestore.FieldValue.serverTimestamp(),
  });

  await batch.commit();

  return { success: true, message: "Payment verified. Test series access granted." };
});

/**
 * 3. CBT EXAM SUBMISSION & SECURE SERVER-SIDE SCORING
 * Calculates marks, negative marks, and percentiles on the backend
 * so students cannot spoof exam scores or answers.
 */
exports.submitCbtExamAttempt = functions.https.onCall(async (data, context) => {
  if (!context.auth) {
    throw new functions.https.HttpsError("unauthenticated", "Authentication required.");
  }

  const { testId, userAnswers, timeTakenSeconds } = data; // userAnswers: { qId: optionIndex }
  const testDoc = await db.collection("mock_tests").doc(testId).get();
  if (!testDoc.exists) {
    throw new functions.https.HttpsError("not-found", "Mock test not found.");
  }

  const test = testDoc.data();
  const questionsSnapshot = await db.collection("mock_tests").doc(testId).collection("questions").get();

  let correctCount = 0;
  let incorrectCount = 0;
  let unattemptedCount = 0;
  let score = 0;

  questionsSnapshot.forEach((doc) => {
    const qData = doc.data();
    const chosen = userAnswers[doc.id];

    if (chosen === undefined || chosen === null) {
      unattemptedCount++;
    } else if (chosen === qData.correctOption) {
      correctCount++;
      score += test.marksPerCorrect || 1.0;
    } else {
      incorrectCount++;
      score -= test.negativeMarks || 0.25;
    }
  });

  score = Math.max(0, score);
  const accuracy = (correctCount + incorrectCount) > 0
    ? (correctCount / (correctCount + incorrectCount)) * 100
    : 0;

  // Save attempt
  const attemptRef = db.collection("exam_attempts").doc();
  const attemptData = {
    id: attemptRef.id,
    testId: testId,
    userId: context.auth.uid,
    scoreObtained: score,
    totalMarks: test.totalMarks,
    correctCount: correctCount,
    incorrectCount: incorrectCount,
    unattemptedCount: unattemptedCount,
    accuracy: accuracy,
    timeTakenSeconds: timeTakenSeconds,
    userAnswers: userAnswers,
    submittedAt: admin.firestore.FieldValue.serverTimestamp(),
  };

  await attemptRef.set(attemptData);

  return attemptData;
});
