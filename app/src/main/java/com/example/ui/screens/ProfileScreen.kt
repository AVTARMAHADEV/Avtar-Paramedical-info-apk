package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Student
import com.example.model.UserRole
import com.example.ui.ParamedicalViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: ParamedicalViewModel,
    student: Student?
) {
    val context = LocalContext.current
    var showPolicyDialog by remember { mutableStateOf<String?>(null) }
    var showFeedbackDialog by remember { mutableStateOf(false) }
    var feedbackText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Digital Student Identity Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                .testTag("student_id_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "AVTAR PARAMEDICAL INFORMATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "OFFICIAL STUDENT IDENTITY CARD",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = HealthcareTeal
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = HealthcareGreenLight
                    ) {
                        Text(
                            text = if (student?.isBlocked == true) "BLOCKED" else "VERIFIED ACTIVE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (student?.isBlocked == true) MaterialTheme.colorScheme.error else HealthcareGreen,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(MedicalBlue, HealthcareTeal))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (student?.name?.firstOrNull() ?: 'A').toString().uppercase(),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Column {
                        Text(
                            text = student?.name ?: "Student Name",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Student ID: ${student?.studentId ?: "API-2026-0000"}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MedicalBlue
                        )
                        Text(
                            text = "Mobile: +91 ${student?.mobile ?: ""}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Reg Date: ${student?.registrationDate ?: ""}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Test Role Switcher (For convenient evaluation of Student vs Admin features)
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Role & Account Access Control",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Current Role: ${student?.role ?: UserRole.STUDENT}",
                    fontSize = 12.sp,
                    color = HealthcareTeal,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.repository.switchRoleForTesting(UserRole.STUDENT) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Student Role", fontSize = 11.sp)
                    }
                    Button(
                        onClick = {
                            viewModel.repository.switchRoleForTesting(UserRole.ADMIN)
                            viewModel.navigateTo(Screen.AdminPanel)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = HealthcareTeal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Admin Dashboard", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Help & Support section
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Help, Support & Contact",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Chat,
                    title = "WhatsApp Student Helpline",
                    subtitle = "Instant test and payment assistance (+91 98765 43210)",
                    tint = HealthcareGreen,
                    onClick = {
                        try {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://wa.me/919876543210?text=Hello%20Avtar%20Paramedical%20Support,%20I%20need%20help%20with%20my%20Student%20ID%20${student?.studentId}")
                            )
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            viewModel.showToast("WhatsApp support: +91 9876543210")
                        }
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = CardBorder)

                ProfileMenuItem(
                    icon = Icons.Default.Email,
                    title = "Official Support Email",
                    subtitle = "support@avtarparamedical.in",
                    tint = MedicalBlue,
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:support@avtarparamedical.in?subject=Paramedical%20Student%20Inquiry%20(${student?.studentId})")
                        }
                        try {
                            context.startActivity(emailIntent)
                        } catch (e: Exception) {
                            viewModel.showToast("Email us at: support@avtarparamedical.in")
                        }
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = CardBorder)

                ProfileMenuItem(
                    icon = Icons.Default.Feedback,
                    title = "Student Feedback & Query",
                    subtitle = "Suggest new questions or report CBT issues",
                    tint = Color(0xFFD97706),
                    onClick = { showFeedbackDialog = true }
                )
            }
        }

        // Platform Policies
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Legal & Platform Policies",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = { showPolicyDialog = "About Us" }) {
                        Text("About Us", fontSize = 11.sp)
                    }
                    TextButton(onClick = { showPolicyDialog = "Privacy Policy" }) {
                        Text("Privacy Policy", fontSize = 11.sp)
                    }
                    TextButton(onClick = { showPolicyDialog = "Refund Policy" }) {
                        Text("Refund Policy", fontSize = 11.sp)
                    }
                }
            }
        }

        // Session Management & Logout
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.repository.logoutFromAllDevices()
                        viewModel.navigateTo(Screen.AuthScreen)
                        viewModel.showToast("Logged out from all active devices.")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Devices, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Logout from All Devices")
                }

                Button(
                    onClick = { viewModel.handleLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth().testTag("logout_button"),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Logout from Account")
                }
            }
        }
    }

    if (showPolicyDialog != null) {
        AlertDialog(
            onDismissRequest = { showPolicyDialog = null },
            title = { Text(showPolicyDialog ?: "") },
            text = {
                Text(
                    when (showPolicyDialog) {
                        "About Us" -> "AVTAR PARAMEDICAL INFORMATION is a dedicated examination preparation and career portal offering Computer Based Test (CBT) mock exams, study materials, and healthcare job alerts for Radiographers, Medical Laboratory Technicians, Pharmacy Officers, and Nursing Officers across India."
                        "Privacy Policy" -> "Your personal details (Name, Mobile, Student ID) and exam performance logs are strictly encrypted and securely stored. We never share student details with unauthorized third parties."
                        else -> "Refund Policy: Course purchases and test series subscriptions can be refunded within 24 hours of purchase if the mock tests were not attempted or due to technical transaction failure."
                    },
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(onClick = { showPolicyDialog = null }) {
                    Text("Close")
                }
            }
        )
    }

    if (showFeedbackDialog) {
        AlertDialog(
            onDismissRequest = { showFeedbackDialog = false },
            title = { Text("Submit Student Feedback") },
            text = {
                Column {
                    Text("Your suggestions help us improve test questions and rationales.", fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = feedbackText,
                        onValueChange = { feedbackText = it },
                        placeholder = { Text("Write your feedback here...") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    showFeedbackDialog = false
                    viewModel.showToast("Thank you! Feedback received by Academic Team.")
                    feedbackText = ""
                }) {
                    Text("Submit")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showFeedbackDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    tint: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SlateLight, modifier = Modifier.size(18.dp))
    }
}
