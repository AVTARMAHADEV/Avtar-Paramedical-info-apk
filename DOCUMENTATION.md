# AVTAR PARAMEDICAL INFORMATION
### Android CBT Examination Platform • Education • Career • Healthcare

---

## 1. System Architecture Overview

AVTAR PARAMEDICAL INFORMATION is a production-grade Computer Based Test (CBT) examination and recruitment platform built specifically for paramedical aspirants across India preparing for:
1. **Radiographer Exam** (X-Ray, CT Scan, MRI, Radiation Safety, ALARA)
2. **Lab Technician Exam** (Biochemistry, Hematology, Clinical Pathology, Microbiology, Histopathology)
3. **Pharmacy Officer Exam** (Pharmacology, Pharmaceutics, Hospital Pharmacy, Drug Jurisprudence)
4. **Nursing Officer Exam** (AIIMS NORCET, ESIC, Critical Care, Med-Surg, OBG, Pediatrics)

### Technology Stack
- **Client (Android):** Kotlin, Jetpack Compose (M3), MVVM, StateFlow, Coroutines, Navigation Compose
- **Backend & APIs:** Firebase Cloud Functions / Node.js with Crypto HMAC-SHA256 signature verification
- **Database:** Google Cloud Firestore NoSQL Database with Zero-Trust Security Rules
- **Authentication:** Phone/OTP-based verification, unique Student ID (`API-2026-XXXX`), session management
- **Payment Gateway:** Razorpay India (UPI, NetBanking, Cards, Debit) with backend webhook validation
- **Web Admin Portal:** Responsive HTML5/CSS3/JavaScript Dashboard in `/web-admin/`
- **Cloud Storage:** Google Drive integration & secure PDF storage

---

## 2. Student & Admin Demo Testing Accounts

Use these pre-configured credentials to instantly test student and administrative workflows:

| Role | Name | Mobile Number | OTP Code | Student ID | Access Level |
|---|---|---|---|---|---|
| **Student** | Vikram Sharma | `9876543210` | `1234` | `API-2026-4892` | Radiographer & MLT tests enrolled |
| **Student** | Pooja Verma | `9812345678` | `1234` | `API-2026-1024` | Lab Technician Master Pack |
| **Super Admin** | Dr. S. K. Avtar | `9999999999` | `1234` | `API-ADMIN-01` | Full Administrative & Academic Control |

*Note: For testing and local evaluation, both `1234` and `2026` act as valid OTP codes.*

---

## 3. CBT Examination Engine Workflow

The CBT mock exam engine matches the official NTA / AIIMS examination standards:
- **Real-Time Countdown Timer:** Synchronized timer with visual warnings (amber under 5 minutes, red under 2 minutes).
- **Auto-Submission:** Triggers automatically when timer reaches `00:00:00`.
- **Question Palette:** 5 distinct visual states:
  - 🟢 **Answered** (Green)
  - 🔴 **Unanswered / Not Attempted** (Red)
  - 🟣 **Marked for Review** (Purple)
  - 🔵 **Answered & Marked for Review** (Indigo)
  - ⚪ **Not Visited** (Gray)
- **Controls:** Previous, Clear Response, Mark for Review & Next, Save & Next.
- **Negative Marking:** Configurable positive marks (+1.0) and negative marking (-0.25, -0.33).
- **Instant Result & Medical Solutions:** Shows marks, accuracy %, time taken, percentile, rank, and detailed clinical rationale for every question.

---

## 4. Razorpay Payment Gateway Integration

### Backend Verification Workflow
1. Client requests order creation from Cloud Functions (`createRazorpayOrder`).
2. Server verifies product price from Firestore and creates a Razorpay Order ID.
3. Client opens Razorpay Checkout dialog with amount in paise.
4. On transaction completion, Razorpay returns `razorpay_payment_id`, `razorpay_order_id`, and `razorpay_signature`.
5. Server verifies signature using HMAC-SHA256:
   ```javascript
   const generatedSignature = crypto
     .createHmac("sha256", process.env.RAZORPAY_KEY_SECRET)
     .update(orderId + "|" + paymentId)
     .digest("hex");
   if (generatedSignature === razorpaySignature) {
     // Activate test series for student in Firestore
   }
   ```
6. **Promotional Coupons Configured:**
   - `AVTAR50`: 50% discount (capped at ₹200)
   - `PARAMED2026`: 30% discount (capped at ₹150)
   - `FIRST100`: 100% scholarship token

---

## 5. Web Admin Dashboard (`/web-admin/`)

The standalone responsive web dashboard includes:
- **Overview & Growth Analytics:** Total students, revenue, test attempts, category distribution.
- **Student Database:** Search student ID, activate/block accounts, inspect purchase history.
- **Exam Series Manager:** Create mock tests, set timer, positive/negative marks, publish/unpublish.
- **Bulk Question Upload:** CSV and Excel upload validator checking option completeness and answer indices.
- **Payment Log:** Audit Razorpay order IDs, payment IDs, and transaction statuses.
- **Content & Jobs Manager:** Add Google Drive folders, PDF handouts, and publish hospital vacancies.

To run the Web Admin locally:
```bash
cd web-admin
# Open index.html in any modern browser or host via Firebase Hosting:
# firebase deploy --only hosting
```

---

## 6. Firebase Setup Instructions

1. Go to [Firebase Console](https://console.firebase.google.com/) and create a project.
2. Enable **Authentication** with Phone / Google Sign-In.
3. Enable **Cloud Firestore** and deploy `/backend/firestore.rules`:
   ```bash
   firebase deploy --only firestore:rules
   ```
4. Deploy Cloud Functions:
   ```bash
   cd backend/functions
   npm install
   firebase deploy --only functions
   ```
5. Place the downloaded `google-services.json` inside `/app/`.

---

## 7. Build & Deployment Instructions

### Debug APK Build
```bash
gradle :app:assembleDebug
```
The generated APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### Signed Production Android App Bundle (AAB)
```bash
gradle :app:bundleRelease
```
The generated bundle will be located at:
`app/build/outputs/bundle/release/app-release.aab`

---

## 8. Security Testing & Audit Checklist

- [x] **Zero-Trust Client Access:** Student ID, payments, and exam scores are never trusted from client inputs.
- [x] **Signature Verification:** All Razorpay transactions validated using HMAC-SHA256.
- [x] **Role-Based Access Control (RBAC):** Admin routes locked behind `ADMIN` and `SUPER_ADMIN` claims.
- [x] **No Hardcoded Secrets:** Credentials accessed via environment variables and BuildConfig.
- [x] **Session Expiry & Multi-Device Logout:** Implemented in `ParamedicalRepository`.
- [x] **Negative Marking Safeguard:** Total score bounded at zero to prevent integer underflow.
