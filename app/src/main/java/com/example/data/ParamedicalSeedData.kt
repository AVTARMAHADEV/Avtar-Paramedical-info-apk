package com.example.data

import com.example.model.*

object ParamedicalSeedData {

    val categories = listOf(
        ExamCategory(
            id = "radiographer",
            name = "Radiographer Exam",
            shortCode = "RAD",
            description = "X-Ray, CT Scan, MRI, Radiation Safety & Anatomy for AIIMS, RRB & State Paramedical boards",
            totalTests = 12,
            totalVacancies = 340
        ),
        ExamCategory(
            id = "lab_technician",
            name = "Lab Technician Exam",
            shortCode = "MLT",
            description = "Biochemistry, Clinical Pathology, Hematology, Microbiology & Histopathology (DMLT / BMLT)",
            totalTests = 15,
            totalVacancies = 620
        ),
        ExamCategory(
            id = "pharmacy_officer",
            name = "Pharmacy Officer Exam",
            shortCode = "PHARM",
            description = "Pharmacology, Pharmaceutics, Hospital Pharmacy, Clinical Toxicology & Drug Jurisprudence",
            totalTests = 10,
            totalVacancies = 285
        ),
        ExamCategory(
            id = "nursing_officer",
            name = "Nursing Officer Exam",
            shortCode = "NORCET",
            description = "Medical-Surgical, Pediatric, Obstetric, Psychiatric, Community Health & Pharmacology (NORCET/ESIC)",
            totalTests = 18,
            totalVacancies = 1450
        )
    )

    // Questions for Radiographer
    val radiographerQuestions = listOf(
        Question(
            id = "rad_q1",
            section = "Radiation Physics",
            text = "According to the ALARA principle and radiation safety standards, what is the minimum lead equivalence required for standard protective aprons worn during fluoroscopy procedures?",
            optionA = "0.15 mm Pb",
            optionB = "0.25 mm Pb",
            optionC = "0.50 mm Pb",
            optionD = "1.00 mm Pb",
            correctOption = 2,
            explanation = "As per AERB and ICRP recommendations, protective lead aprons worn during fluoroscopy must have a minimum lead equivalence of 0.5 mm Pb to attenuate over 90% of secondary scatter radiation.",
            subjectTopic = "Radiation Protection"
        ),
        Question(
            id = "rad_q2",
            section = "Radiographic Technique",
            text = "In diagnostic X-ray production, what percentage of the kinetic energy of striking electrons is converted into X-rays at 80 kVp?",
            optionA = "Less than 1%",
            optionB = "Approximately 10%",
            optionC = "Nearly 50%",
            optionD = "Over 90%",
            correctOption = 0,
            explanation = "At diagnostic energies (50-120 kVp), more than 99% of electron kinetic energy is dissipated as thermal heat in the anode target, and less than 1% is converted into Bremsstrahlung and Characteristic X-rays.",
            subjectTopic = "X-Ray Production"
        ),
        Question(
            id = "rad_q3",
            section = "Computed Tomography",
            text = "What is the CT Hounsfield Unit (HU) number designated for pure water at standard temperature and pressure?",
            optionA = "-1000 HU",
            optionB = "0 HU",
            optionC = "+50 HU",
            optionD = "+1000 HU",
            correctOption = 1,
            explanation = "By definition in the Hounsfield scale, pure water has a CT number of 0 HU, air has -1000 HU, and dense cortical bone ranges from +1000 to +3000 HU.",
            subjectTopic = "CT Scanning"
        ),
        Question(
            id = "rad_q4",
            section = "Magnetic Resonance Imaging",
            text = "In MRI, T1 relaxation time is also clinically known by which of the following terms?",
            optionA = "Spin-spin relaxation time",
            optionB = "Spin-lattice relaxation time",
            optionC = "Free induction decay (FID)",
            optionD = "Phase coherence recovery",
            correctOption = 1,
            explanation = "T1 relaxation is the spin-lattice relaxation time, representing the recovery of longitudinal magnetization back along the z-axis (parallel to the main magnetic field B0).",
            subjectTopic = "MRI Principles"
        ),
        Question(
            id = "rad_q5",
            section = "Radiographic Quality",
            text = "Increasing the Source-to-Image Distance (SID) from 100 cm to 180 cm has which of the following effects on the radiograph?",
            optionA = "Increases magnification and increases penumbra",
            optionB = "Decreases magnification and increases spatial resolution (detail)",
            optionC = "Increases optical density without changing exposure factors",
            optionD = "Distorts anatomical shape",
            correctOption = 1,
            explanation = "Increasing SID reduces geometric magnification and geometric unsharpness (penumbra), thereby enhancing spatial resolution. Chest radiographs are routinely taken at 180 cm SID for this reason.",
            subjectTopic = "Image Quality"
        ),
        Question(
            id = "rad_q6",
            section = "Radiation Units",
            text = "The SI unit for absorbed dose of ionizing radiation in tissue is:",
            optionA = "Sievert (Sv)",
            optionB = "Gray (Gy)",
            optionC = "Roentgen (R)",
            optionD = "Becquerel (Bq)",
            correctOption = 1,
            explanation = "The Gray (Gy) is the SI unit of absorbed dose (1 Gy = 1 Joule/kg). The Sievert (Sv) is the unit of equivalent and effective dose taking into account biological damage.",
            subjectTopic = "Dosimetry"
        )
    )

    // Questions for Lab Technician
    val labTechnicianQuestions = listOf(
        Question(
            id = "mlt_q1",
            section = "Clinical Hematology",
            text = "Which anticoagulant is universally preferred for routine complete blood count (CBC) and blood cell morphology examination?",
            optionA = "Sodium Citrate (3.2%)",
            optionB = "K2/K3 EDTA (Ethylenediaminetetraacetic acid)",
            optionC = "Heparin Lithium",
            optionD = "Sodium Fluoride / Potassium Oxalate",
            correctOption = 1,
            explanation = "EDTA acts by chelating calcium ions and preserves the morphological details of erythrocytes and leukocytes with minimal swelling or distortion, making it the anticoagulant of choice for hematology.",
            subjectTopic = "Anticoagulants"
        ),
        Question(
            id = "mlt_q2",
            section = "Clinical Biochemistry",
            text = "Why is Sodium Fluoride included in the vacutainer tube used for plasma blood glucose estimation?",
            optionA = "It prevents red cell hemolysis",
            optionB = "It inhibits the glycolytic enzyme enolase, preventing glucose consumption",
            optionC = "It accelerates clotting time for quick serum separation",
            optionD = "It stabilizes hemoglobin against oxidation",
            correctOption = 1,
            explanation = "Sodium fluoride is a glycolytic inhibitor that blocks enolase. Without it, in vitro glycolysis by erythrocytes consumes glucose at a rate of roughly 5-10 mg/dL per hour at room temperature.",
            subjectTopic = "Blood Chemistry"
        ),
        Question(
            id = "mlt_q3",
            section = "Medical Microbiology",
            text = "In the standard Gram staining procedure, what is the role of Gram's Iodine?",
            optionA = "Primary basic dye",
            optionB = "Mordant that forms an insoluble crystal violet-iodine complex",
            optionC = "Decolorizing agent",
            optionD = "Counterstain",
            correctOption = 1,
            explanation = "Gram's Iodine acts as a mordant. It enters the cell wall and binds with Crystal Violet to form large CV-I complexes trapped in the thick peptidoglycan layer of Gram-positive bacteria.",
            subjectTopic = "Bacteriology"
        ),
        Question(
            id = "mlt_q4",
            section = "Sterilization & Disinfection",
            text = "What are the standard operational parameters for autoclave sterilization of bacterial culture media and surgical instruments?",
            optionA = "100°C for 30 minutes at atmospheric pressure",
            optionB = "121°C (250°F) at 15 psi pressure for 15-20 minutes",
            optionC = "160°C dry heat for 10 minutes",
            optionD = "180°C for 2 hours",
            correctOption = 1,
            explanation = "Moist heat under pressure at 121°C, 15 pounds per square inch (psi) gauge pressure for 15-20 minutes ensures destruction of all vegetative bacteria, viruses, and resistant bacterial endospores.",
            subjectTopic = "Sterilization"
        ),
        Question(
            id = "mlt_q5",
            section = "Histopathology",
            text = "What is the most widely utilized universal fixative for routine surgical pathology specimens?",
            optionA = "10% Neutral Buffered Formalin (NBF)",
            optionB = "Bouin's Fluid",
            optionC = "100% Absolute Ethanol",
            optionD = "Glutaraldehyde 2.5%",
            correctOption = 0,
            explanation = "10% Neutral Buffered Formalin (which corresponds to 4% formaldehyde solution buffered to pH 7.2-7.4) cross-links proteins, prevents autolysis, and preserves tissue architecture with minimal shrinkage.",
            subjectTopic = "Histotechnique"
        )
    )

    // Questions for Pharmacy Officer
    val pharmacyQuestions = listOf(
        Question(
            id = "pharm_q1",
            section = "Clinical Pharmacology",
            text = "What is the specific biochemical antidote administered for acute acetaminophen (paracetamol) toxicity?",
            optionA = "Naloxone",
            optionB = "N-Acetylcysteine (NAC)",
            optionC = "Flumazenil",
            optionD = "Atropine Sulphate",
            correctOption = 1,
            explanation = "N-Acetylcysteine replenishes cellular glutathione stores and conjugates directly with the toxic metabolite NAPQI (N-acetyl-p-benzoquinone imine), preventing centrilobular hepatic necrosis.",
            subjectTopic = "Antidotes & Toxicology"
        ),
        Question(
            id = "pharm_q2",
            section = "Drug Regulatory Affairs",
            text = "Under the Drugs and Cosmetics Rules 1945 (India), which Schedule specifies prescription drugs that can only be dispensed by a registered pharmacist upon a medical practitioner's prescription?",
            optionA = "Schedule C",
            optionB = "Schedule H and H1",
            optionC = "Schedule M",
            optionD = "Schedule X",
            correctOption = 1,
            explanation = "Schedule H and H1 contain prescription drugs. Schedule M specifies Good Manufacturing Practices (GMP), Schedule C covers biological products, and Schedule X covers psychotropic substances.",
            subjectTopic = "Pharmaceutical Jurisprudence"
        ),
        Question(
            id = "pharm_q3",
            section = "Pharmaceutics",
            text = "A drug administered via the intravenous (IV) route has an absolute bioavailability (F) equal to:",
            optionA = "0%",
            optionB = "50%",
            optionC = "100% (F = 1.0)",
            optionD = "Dependent on gastric pH",
            correctOption = 2,
            explanation = "Intravenous injection delivers 100% of the administered dose directly into systemic circulation without encountering gastric degradation or hepatic first-pass metabolism, so F = 1.0 (100%).",
            subjectTopic = "Biopharmaceutics"
        ),
        Question(
            id = "pharm_q4",
            section = "Medicinal Chemistry",
            text = "Penicillins and cephalosporins exert their bactericidal action by inhibiting which essential bacterial structure?",
            optionA = "30S ribosomal protein synthesis",
            optionB = "DNA Gyrase enzyme",
            optionC = "Peptidoglycan bacterial cell wall cross-linking via transpeptidase",
            optionD = "Folic acid synthesis pathway",
            correctOption = 2,
            explanation = "Beta-lactam antibiotics bind to penicillin-binding proteins (PBPs) and inhibit the transpeptidase enzyme, preventing peptidoglycan cross-linking and causing osmotic bacterial cell lysis.",
            subjectTopic = "Antibacterial Agents"
        )
    )

    // Questions for Nursing Officer
    val nursingQuestions = listOf(
        Question(
            id = "nurs_q1",
            section = "Emergency & Critical Care",
            text = "What is the minimum and maximum score achievable on the standard Glasgow Coma Scale (GCS)?",
            optionA = "0 to 10",
            optionB = "3 to 15",
            optionC = "1 to 15",
            optionD = "0 to 15",
            correctOption = 1,
            explanation = "GCS assesses Eye opening (1-4), Verbal response (1-5), and Motor response (1-6). The lowest possible score is 3 (deep coma/unresponsive) and the maximum normal score is 15.",
            subjectTopic = "Neurological Assessment"
        ),
        Question(
            id = "nurs_q2",
            section = "Basic Life Support (BLS)",
            text = "According to AHA guidelines, what is the recommended chest compression-to-ventilation ratio for an adult victim in cardiac arrest handled by a single rescuer?",
            optionA = "15:2",
            optionB = "30:2",
            optionC = "5:1",
            optionD = "50:2",
            correctOption = 1,
            explanation = "For adult CPR by both single rescuers and pairs, the standard compression to ventilation ratio is 30 compressions followed by 2 breaths, at a rate of 100-120 compressions/min.",
            subjectTopic = "Cardiopulmonary Resuscitation"
        ),
        Question(
            id = "nurs_q3",
            section = "Obstetric Nursing",
            text = "Which of the following is considered the drug of choice for the prevention and management of seizures in severe pre-eclampsia and eclampsia?",
            optionA = "Diazepam",
            optionB = "Magnesium Sulphate (MgSO4)",
            optionC = "Phenytoin",
            optionD = "Sodium Nitroprusside",
            correctOption = 1,
            explanation = "Magnesium Sulphate is the internationally established drug of choice for eclampsia. Serum levels and deep tendon reflexes (patellar) must be monitored for signs of hypermagnesemia.",
            subjectTopic = "Maternal Health"
        ),
        Question(
            id = "nurs_q4",
            section = "Pediatric Nursing",
            text = "An APGAR score assessed 1 minute after birth yields a score of 8. What does this score indicate regarding the newborn's physiological status?",
            optionA = "Severe depression requiring immediate endotracheal resuscitation",
            optionB = "Moderate depression needing oxygen and stimulation",
            optionC = "Good condition with normal newborn adjustment",
            optionD = "Congenital cardiac abnormality",
            correctOption = 2,
            explanation = "APGAR score ranges: 7 to 10 indicates a newborn in good condition requiring routine post-delivery care; 4 to 6 indicates moderate depression; 0 to 3 indicates severe distress.",
            subjectTopic = "Neonatal Care"
        ),
        Question(
            id = "nurs_q5",
            section = "Intravenous Therapy",
            text = "Which of the following intravenous solutions is classified as an isotonic crystalloid solution?",
            optionA = "0.45% Sodium Chloride (Half-normal saline)",
            optionB = "0.9% Normal Saline (0.9% NaCl)",
            optionC = "3% Hypertonic Saline",
            optionD = "Dextrose 10% in Water",
            correctOption = 1,
            explanation = "0.9% Sodium Chloride (Normal Saline) and Ringer's Lactate (RL) have osmolarities approximately equal to normal serum osmolarity (around 275-295 mOsm/L), making them isotonic.",
            subjectTopic = "Fluid Management"
        )
    )

    val mockTests = listOf(
        // Radiographer
        MockTest(
            id = "rad_free_demo",
            categoryId = "radiographer",
            title = "Radiographer High-Yield Demo Mock Test",
            subtitle = "Free Diagnostic Test • X-Ray, CT, Radiation Safety",
            totalQuestions = 6,
            durationMinutes = 15,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = false,
            price = 0,
            originalPrice = 0,
            isPublished = true,
            maxRetakes = 5,
            questions = radiographerQuestions
        ),
        MockTest(
            id = "rad_mega_series_1",
            categoryId = "radiographer",
            title = "AIIMS & RRB Radiographer All-India CBT 01",
            subtitle = "Complete 100 Questions • Section Wise • Negative Marking",
            totalQuestions = 6,
            durationMinutes = 90,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = true,
            price = 299,
            originalPrice = 999,
            isPublished = true,
            maxRetakes = 2,
            questions = radiographerQuestions
        ),

        // Lab Technician
        MockTest(
            id = "mlt_free_demo",
            categoryId = "lab_technician",
            title = "Lab Technician (DMLT/BMLT) Starter CBT",
            subtitle = "Free Mock Test • Hematology, Pathology & Microbiology",
            totalQuestions = 5,
            durationMinutes = 15,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = false,
            price = 0,
            originalPrice = 0,
            isPublished = true,
            maxRetakes = 5,
            questions = labTechnicianQuestions
        ),
        MockTest(
            id = "mlt_cbt_master_pack",
            categoryId = "lab_technician",
            title = "ESIC & State Paramedical Lab Technician Mock 2026",
            subtitle = "10 Full-Length Test Series with Detailed Rationales",
            totalQuestions = 5,
            durationMinutes = 120,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = true,
            price = 349,
            originalPrice = 1199,
            isPublished = true,
            maxRetakes = 2,
            questions = labTechnicianQuestions
        ),

        // Pharmacy Officer
        MockTest(
            id = "pharm_free_demo",
            categoryId = "pharmacy_officer",
            title = "Pharmacy Officer Speed & Accuracy Demo",
            subtitle = "Free Evaluation • Pharmacology & Drug Jurisprudence",
            totalQuestions = 4,
            durationMinutes = 12,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = false,
            price = 0,
            originalPrice = 0,
            isPublished = true,
            maxRetakes = 5,
            questions = pharmacyQuestions
        ),
        MockTest(
            id = "pharm_officer_pro",
            categoryId = "pharmacy_officer",
            title = "DHS / ZP / CGHS Pharmacy Officer CBT Grand Test",
            subtitle = "High-Yield Clinical Pharmacy & Schedule Laws",
            totalQuestions = 4,
            durationMinutes = 90,
            marksPerCorrect = 1.0,
            negativeMarks = 0.25,
            isPaid = true,
            price = 299,
            originalPrice = 899,
            isPublished = true,
            maxRetakes = 2,
            questions = pharmacyQuestions
        ),

        // Nursing Officer
        MockTest(
            id = "nurs_free_demo",
            categoryId = "nursing_officer",
            title = "NORCET Nursing Officer Screening Demo",
            subtitle = "Free Practice • Med-Surg, OBG, Critical Care",
            totalQuestions = 5,
            durationMinutes = 15,
            marksPerCorrect = 1.0,
            negativeMarks = 0.33,
            isPaid = false,
            price = 0,
            originalPrice = 0,
            isPublished = true,
            maxRetakes = 5,
            questions = nursingQuestions
        ),
        MockTest(
            id = "norcet_super_pack",
            categoryId = "nursing_officer",
            title = "AIIMS NORCET 2026 All-India Rank Booster",
            subtitle = "15 Sectional + 5 Grand CBT Mocks with AIIMS Rank List",
            totalQuestions = 5,
            durationMinutes = 180,
            marksPerCorrect = 1.0,
            negativeMarks = 0.33,
            isPaid = true,
            price = 499,
            originalPrice = 1499,
            isPublished = true,
            maxRetakes = 2,
            questions = nursingQuestions
        )
    )

    val studyMaterials = listOf(
        StudyMaterial(
            id = "mat_rad_1",
            categoryId = "radiographer",
            title = "Radiation Safety & ALARA Guidelines Handout (PDF)",
            subject = "Radiation Protection",
            type = MaterialType.PDF_DOCUMENT,
            accessUrl = "https://drive.google.com/file/d/sample-radiographer-radiation-safety/view",
            fileSizeBytes = "3.8 MB",
            isFree = true,
            uploadedDate = "10 Nov 2026"
        ),
        StudyMaterial(
            id = "mat_rad_2",
            categoryId = "radiographer",
            title = "CT Scan Hounsfield Units & Contrast Protocols (Drive Link)",
            subject = "Advanced Imaging",
            type = MaterialType.GOOGLE_DRIVE,
            accessUrl = "https://drive.google.com/drive/folders/avtar-ct-scan-protocols",
            fileSizeBytes = "18.2 MB",
            isFree = false,
            uploadedDate = "05 Nov 2026"
        ),
        StudyMaterial(
            id = "mat_mlt_1",
            categoryId = "lab_technician",
            title = "DMLT Comprehensive Hematology & Normal Values Handbook",
            subject = "Hematology",
            type = MaterialType.PDF_DOCUMENT,
            accessUrl = "https://drive.google.com/file/d/sample-mlt-hematology-handbook/view",
            fileSizeBytes = "5.1 MB",
            isFree = true,
            uploadedDate = "08 Nov 2026"
        ),
        StudyMaterial(
            id = "mat_mlt_2",
            categoryId = "lab_technician",
            title = "Clinical Microbiology Culture Media & Stains Video Lecture",
            subject = "Microbiology",
            type = MaterialType.VIDEO_LECTURE,
            accessUrl = "https://www.youtube.com/watch?v=sample-mlt-lecture",
            fileSizeBytes = "45 Mins HD",
            isFree = true,
            uploadedDate = "01 Nov 2026"
        ),
        StudyMaterial(
            id = "mat_pharm_1",
            categoryId = "pharmacy_officer",
            title = "Drugs & Cosmetics Act 1940: High-Yield Schedules Summary",
            subject = "Jurisprudence",
            type = MaterialType.PDF_DOCUMENT,
            accessUrl = "https://drive.google.com/file/d/sample-pharmacy-schedules/view",
            fileSizeBytes = "2.9 MB",
            isFree = true,
            uploadedDate = "09 Nov 2026"
        ),
        StudyMaterial(
            id = "mat_nurs_1",
            categoryId = "nursing_officer",
            title = "NORCET Golden Points: Emergency Med-Surg & Drug Dosages",
            subject = "Med-Surg Nursing",
            type = MaterialType.GOOGLE_DRIVE,
            accessUrl = "https://drive.google.com/drive/folders/norcet-golden-points-2026",
            fileSizeBytes = "12.4 MB",
            isFree = true,
            uploadedDate = "07 Nov 2026"
        )
    )

    val jobNotifications = listOf(
        JobNotification(
            id = "job_1",
            title = "Senior Radiographer & X-Ray Technician Vacancies",
            department = "Department of Radiodiagnosis",
            hospitalOrganization = "AIIMS New Delhi",
            location = "New Delhi, India",
            qualification = "B.Sc (Hons) in Radiography / 3-Yr Diploma with 2 Yrs Hospital Experience",
            experience = "2 Years",
            salary = "Level 6 (₹35,400 - ₹1,12,400 / Month)",
            vacancies = 48,
            deadlineDate = "30 Nov 2026",
            officialApplyLink = "https://www.aiimsexams.ac.in",
            notificationPdfUrl = "https://drive.google.com/file/d/aiims-radiographer-official-pdf/view",
            isOfficialVerified = true,
            categoryId = "radiographer",
            postedDate = "10 Nov 2026"
        ),
        JobNotification(
            id = "job_2",
            title = "Medical Laboratory Technician Grade-II",
            department = "Central Laboratory Services",
            hospitalOrganization = "Employees' State Insurance Corporation (ESIC)",
            location = "Pan India (All Regions)",
            qualification = "Degree in Medical Laboratory Science (BMLT) or DMLT",
            experience = "1 Year preferred",
            salary = "Level 5 (₹29,200 - ₹92,300 / Month)",
            vacancies = 142,
            deadlineDate = "15 Dec 2026",
            officialApplyLink = "https://www.esic.gov.in",
            notificationPdfUrl = "https://drive.google.com/file/d/esic-lab-tech-recruitment/view",
            isOfficialVerified = true,
            categoryId = "lab_technician",
            postedDate = "09 Nov 2026"
        ),
        JobNotification(
            id = "job_3",
            title = "Pharmacy Officer / Hospital Pharmacist",
            department = "Directorate of Health Services (DHS)",
            hospitalOrganization = "State Health Mission",
            location = "Maharashtra & Gujarat Region",
            qualification = "B.Pharm or D.Pharm with State Pharmacy Council Registration",
            experience = "Freshers Eligible",
            salary = "₹28,000 - ₹85,000 / Month + Allowances",
            vacancies = 96,
            deadlineDate = "05 Dec 2026",
            officialApplyLink = "https://arogya.maharashtra.gov.in",
            notificationPdfUrl = "https://drive.google.com/file/d/dhs-pharmacist-advertisement/view",
            isOfficialVerified = true,
            categoryId = "pharmacy_officer",
            postedDate = "08 Nov 2026"
        ),
        JobNotification(
            id = "job_4",
            title = "Nursing Officer Recruitment (NORCET 2026)",
            department = "Nursing Cadre",
            hospitalOrganization = "All India Institutes of Medical Sciences (AIIMS)",
            location = "All AIIMS Institutes across India",
            qualification = "B.Sc (Hons.) Nursing / B.Sc Nursing or GNM with 2 Yrs 50-bedded hospital exp",
            experience = "0-2 Years as per qualification",
            salary = "Level 7 (₹44,900 - ₹1,42,400 / Month)",
            vacancies = 1250,
            deadlineDate = "20 Dec 2026",
            officialApplyLink = "https://norcet.aiimsexams.ac.in",
            notificationPdfUrl = "https://drive.google.com/file/d/norcet-2026-official-notification/view",
            isOfficialVerified = true,
            categoryId = "nursing_officer",
            postedDate = "07 Nov 2026"
        )
    )

    val validCoupons = listOf(
        Coupon(
            code = "AVTAR50",
            discountPercent = 50,
            maxDiscountInr = 200,
            description = "Special 50% Flat Discount for First 500 Students"
        ),
        Coupon(
            code = "PARAMED2026",
            discountPercent = 30,
            maxDiscountInr = 150,
            description = "30% Off on All Paramedical Full Mock Packs"
        ),
        Coupon(
            code = "FIRST100",
            discountPercent = 100,
            maxDiscountInr = 500,
            description = "100% Free scholarship access token"
        )
    )
}
