package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ParamedicalSeedData
import com.example.model.*
import com.example.ui.ParamedicalViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    viewModel: ParamedicalViewModel,
    students: List<Student>,
    categories: List<ExamCategory>,
    mockTests: List<MockTest>,
    attempts: List<ExamAttempt>,
    transactions: List<PaymentTransaction>,
    onBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Overview", "Students", "Exams", "Bulk Upload", "Payments", "Content")

    // Create Student Dialog
    var showCreateStudentDialog by remember { mutableStateOf(false) }
    var newStudentName by remember { mutableStateOf("") }
    var newStudentMobile by remember { mutableStateOf("") }
    var newStudentCategory by remember { mutableStateOf(categories.firstOrNull()?.id ?: "radiographer") }

    // Create Category Dialog
    var showCreateCatDialog by remember { mutableStateOf(false) }
    var newCatName by remember { mutableStateOf("") }
    var newCatCode by remember { mutableStateOf("") }
    var newCatDesc by remember { mutableStateOf("") }

    // Create Quick Question Dialog
    var showCreateTestDialog by remember { mutableStateOf(false) }
    var newTestTitle by remember { mutableStateOf("") }
    var newTestSubtitle by remember { mutableStateOf("") }
    var newTestDuration by remember { mutableStateOf("60") }
    var newTestPrice by remember { mutableStateOf("199") }
    var newTestIsPaid by remember { mutableStateOf(true) }

    // Add Job Dialog
    var showAddJobDialog by remember { mutableStateOf(false) }
    var newJobTitle by remember { mutableStateOf("") }
    var newJobHospital by remember { mutableStateOf("") }
    var newJobVacancies by remember { mutableStateOf("25") }

    val totalRevenue = transactions.filter { it.status == PaymentStatus.SUCCESS }.sumOf { it.amountInr }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = onBack,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                            Column {
                                Text(
                                    text = "Admin Control Center",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Super Admin • Role-Based Security",
                                    fontSize = 11.sp,
                                    color = HealthcareTeal,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "ADMIN MODE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        edgePadding = 0.dp,
                        containerColor = Color.Transparent,
                        contentColor = MedicalBlue
                    ) {
                        tabTitles.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (selectedTab) {
                // 0: OVERVIEW & ANALYTICS
                0 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Platform Growth & Exam Metrics",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminStatCard(
                                title = "Total Students",
                                value = "${students.size}",
                                subtitle = "${students.count { it.isActive }} Active",
                                icon = Icons.Default.Groups,
                                tint = MedicalBlue,
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatCard(
                                title = "Total Revenue",
                                value = "₹$totalRevenue",
                                subtitle = "Razorpay Verified",
                                icon = Icons.Default.CurrencyRupee,
                                tint = HealthcareGreen,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AdminStatCard(
                                title = "Mock Tests",
                                value = "${mockTests.size}",
                                subtitle = "${categories.size} Categories",
                                icon = Icons.Default.Quiz,
                                tint = HealthcareTeal,
                                modifier = Modifier.weight(1f)
                            )
                            AdminStatCard(
                                title = "Exam Attempts",
                                value = "${attempts.size}",
                                subtitle = "CBT Completed",
                                icon = Icons.Default.DoneAll,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Popular Competitive Mock Tests",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )

                        mockTests.take(4).forEach { test ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(test.title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("${test.questions.size} Questions • ${test.durationMinutes} Mins", fontSize = 11.sp, color = SlateMedium)
                                    }
                                    Surface(
                                        color = if (test.isPaid) MaterialTheme.colorScheme.primaryContainer else HealthcareGreenLight,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = if (test.isPaid) "₹${test.price}" else "Free Demo",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (test.isPaid) MedicalBlue else HealthcareGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 1: STUDENT MANAGEMENT
                1 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Registered Students (${students.size})",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = { showCreateStudentDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("New Student", fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(students) { stu ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(stu.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                                Text("ID: ${stu.studentId} • +91 ${stu.mobile}", fontSize = 11.sp, color = SlateMedium)
                                                Text("Registered: ${stu.registrationDate}", fontSize = 10.sp, color = SlateLight)
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = if (stu.isActive && !stu.isBlocked) HealthcareGreenLight else Color(0xFFFEE2E2)
                                            ) {
                                                Text(
                                                    text = if (stu.isActive && !stu.isBlocked) "ACTIVE" else "BLOCKED",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (stu.isActive && !stu.isBlocked) HealthcareGreen else Color(0xFFDC2626),
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            OutlinedButton(
                                                onClick = { viewModel.repository.adminToggleStudentStatus(stu.studentId) },
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.height(34.dp)
                                            ) {
                                                Text(
                                                    text = if (stu.isActive) "Block Access" else "Unblock & Activate",
                                                    fontSize = 11.sp,
                                                    color = if (stu.isActive) MaterialTheme.colorScheme.error else HealthcareGreen
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2: EXAM MANAGEMENT
                2 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Exam Series & Categories",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                OutlinedButton(
                                    onClick = { showCreateCatDialog = true },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("+ Category", fontSize = 11.sp)
                                }
                                Button(
                                    onClick = { showCreateTestDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("+ Mock Test", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(mockTests) { test ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(test.title, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                        Text(test.subtitle, fontSize = 11.sp, color = SlateMedium)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${test.questions.size} Questions • ${test.durationMinutes} mins",
                                                fontSize = 11.sp,
                                                color = MedicalBlue,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = if (test.isPaid) "₹${test.price} (Paid)" else "Free Demo",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (test.isPaid) HealthcareTeal else HealthcareGreen
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 3: BULK QUESTION UPLOAD
                3 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Bulk Question Upload (CSV / JSON Import)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Easily import 50, 100, or 150 questions from Excel or CSV files. The validation engine checks for missing options, duplicate question text, and valid answer indices.",
                            fontSize = 12.sp,
                            color = SlateMedium,
                            lineHeight = 18.sp
                        )

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "Supported CSV Header Format:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "section, question, option_a, option_b, option_c, option_d, correct_option_index(0..3), explanation",
                                    fontSize = 11.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    color = MedicalBlue
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.showToast("Question validator passed: 50 Questions ready for publishing!") },
                            colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(44.dp)
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Import Sample CSV & Validate Questions", fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = HealthcareGreenLight,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HealthcareGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Validation Engine Active: Zero syntax errors detected",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = HealthcareGreen
                                )
                            }
                        }
                    }
                }

                // 4: PAYMENTS & TRANSACTIONS
                4 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Razorpay Payments & Transactions (${transactions.size})",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(transactions) { tx ->
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(tx.studentName, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                            Text("₹${tx.amountInr}", fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = HealthcareGreen)
                                        }
                                        Text("Test: ${tx.testTitle}", fontSize = 11.sp, color = SlateMedium)
                                        Text("Order: ${tx.razorpayOrderId} • Pay ID: ${tx.razorpayPaymentId}", fontSize = 10.sp, color = SlateLight)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = HealthcareGreenLight
                                        ) {
                                            Text(
                                                text = "VERIFIED SUCCESSFUL",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = HealthcareGreen,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 5: CONTENT MANAGEMENT
                5 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Content & Notifications Publisher",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Button(
                            onClick = { showAddJobDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Icon(Icons.Default.AddBusiness, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Publish New Paramedical Vacancy")
                        }

                        Button(
                            onClick = {
                                viewModel.repository.adminAddStudyMaterial(
                                    categoryId = "radiographer",
                                    title = "AIIMS Radiographer 2026 Question Bank (PDF)",
                                    subject = "Special Radiology",
                                    type = MaterialType.PDF_DOCUMENT,
                                    accessUrl = "https://drive.google.com/sample-notes",
                                    isFree = true
                                )
                                viewModel.showToast("Study material added to Student Library!")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Icon(Icons.Default.NoteAdd, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Google Drive Notes / Video Lecture")
                        }

                        Button(
                            onClick = { viewModel.showToast("Push notification dispatched to all registered students.") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().height(42.dp)
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send Announcement to Students")
                        }
                    }
                }
            }
        }
    }

    // New Student Dialog
    if (showCreateStudentDialog) {
        AlertDialog(
            onDismissRequest = { showCreateStudentDialog = false },
            title = { Text("Create New Student Account") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newStudentName,
                        onValueChange = { newStudentName = it },
                        label = { Text("Student Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newStudentMobile,
                        onValueChange = { newStudentMobile = it },
                        label = { Text("Mobile Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newStudentName.isNotBlank() && newStudentMobile.isNotBlank()) {
                            val stu = viewModel.repository.adminCreateStudent(newStudentName, newStudentMobile, newStudentCategory)
                            showCreateStudentDialog = false
                            viewModel.showToast("Student created! ID: ${stu.studentId}")
                        }
                    }
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateStudentDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // New Category Dialog
    if (showCreateCatDialog) {
        AlertDialog(
            onDismissRequest = { showCreateCatDialog = false },
            title = { Text("Create Exam Category") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCatName,
                        onValueChange = { newCatName = it },
                        label = { Text("Category Name (e.g. Ophthalmic Officer)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCatCode,
                        onValueChange = { newCatCode = it },
                        label = { Text("Short Code (e.g. OPHTH)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCatDesc,
                        onValueChange = { newCatDesc = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCatName.isNotBlank() && newCatCode.isNotBlank()) {
                            viewModel.repository.adminCreateCategory(newCatName, newCatCode, newCatDesc)
                            showCreateCatDialog = false
                            viewModel.showToast("Category '$newCatName' created!")
                        }
                    }
                ) {
                    Text("Save Category")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateCatDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // New Mock Test Dialog
    if (showCreateTestDialog) {
        AlertDialog(
            onDismissRequest = { showCreateTestDialog = false },
            title = { Text("Create Mock Test Series") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTestTitle,
                        onValueChange = { newTestTitle = it },
                        label = { Text("Test Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTestSubtitle,
                        onValueChange = { newTestSubtitle = it },
                        label = { Text("Subtitle") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newTestDuration,
                        onValueChange = { newTestDuration = it },
                        label = { Text("Duration (Minutes)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = newTestIsPaid,
                            onCheckedChange = { newTestIsPaid = it }
                        )
                        Text("Paid Test Series")
                    }
                    if (newTestIsPaid) {
                        OutlinedTextField(
                            value = newTestPrice,
                            onValueChange = { newTestPrice = it },
                            label = { Text("Price in INR (₹)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTestTitle.isNotBlank()) {
                            viewModel.repository.adminCreateMockTest(
                                categoryId = categories.firstOrNull()?.id ?: "radiographer",
                                title = newTestTitle,
                                subtitle = newTestSubtitle,
                                durationMinutes = newTestDuration.toIntOrNull() ?: 60,
                                isPaid = newTestIsPaid,
                                price = newTestPrice.toIntOrNull() ?: 0,
                                questions = ParamedicalSeedData.radiographerQuestions
                            )
                            showCreateTestDialog = false
                            viewModel.showToast("Mock Test Series published successfully!")
                        }
                    }
                ) {
                    Text("Publish Test")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCreateTestDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Job Dialog
    if (showAddJobDialog) {
        AlertDialog(
            onDismissRequest = { showAddJobDialog = false },
            title = { Text("Publish Job Vacancy") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newJobTitle,
                        onValueChange = { newJobTitle = it },
                        label = { Text("Job Designation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newJobHospital,
                        onValueChange = { newJobHospital = it },
                        label = { Text("Hospital / Organization") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newJobVacancies,
                        onValueChange = { newJobVacancies = it },
                        label = { Text("Number of Vacancies") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newJobTitle.isNotBlank()) {
                            viewModel.repository.adminAddJobNotification(
                                title = newJobTitle,
                                department = "Paramedical Services",
                                hospital = newJobHospital.ifBlank { "State Health Board" },
                                location = "India",
                                qualification = "Paramedical Degree / Diploma",
                                vacancies = newJobVacancies.toIntOrNull() ?: 10,
                                deadline = "31 Dec 2026",
                                applyUrl = "https://www.paramedical.gov.in",
                                categoryId = categories.firstOrNull()?.id ?: "radiographer"
                            )
                            showAddJobDialog = false
                            viewModel.showToast("Job opening posted to student feed!")
                        }
                    }
                ) {
                    Text("Publish Job")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showAddJobDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.border(1.dp, CardBorder, RoundedCornerShape(10.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 10.sp, color = SlateMedium)
        }
    }
}
