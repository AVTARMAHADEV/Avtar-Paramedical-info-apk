package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamCategory
import com.example.model.MaterialType
import com.example.model.StudyMaterial
import com.example.ui.ParamedicalViewModel
import com.example.ui.theme.*

@Composable
fun StudyMaterialsScreen(
    viewModel: ParamedicalViewModel,
    categories: List<ExamCategory>,
    studyMaterials: List<StudyMaterial>
) {
    val context = LocalContext.current
    var selectedCategoryFilter by remember { mutableStateOf("all") }

    val filteredMaterials = if (selectedCategoryFilter == "all") {
        studyMaterials
    } else {
        studyMaterials.filter { it.categoryId == selectedCategoryFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Paramedical Study Library & Google Drive",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Standard Operating Procedures (SOPs), High-Yield Handouts & Video Lectures",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryFilter == "all",
                            onClick = { selectedCategoryFilter = "all" },
                            label = { Text("All Disciplines") }
                        )
                    }
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategoryFilter == cat.id,
                            onClick = { selectedCategoryFilter = cat.id },
                            label = { Text(cat.shortCode) }
                        )
                    }
                }
            }
        }

        // List of Study Materials
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredMaterials) { material ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .testTag("study_material_card_${material.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = when (material.type) {
                                        MaterialType.PDF_DOCUMENT -> Color(0xFFFEE2E2)
                                        MaterialType.GOOGLE_DRIVE -> HealthcareTealLight
                                        MaterialType.VIDEO_LECTURE -> MaterialTheme.colorScheme.primaryContainer
                                        else -> HealthcareGreenLight
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = when (material.type) {
                                                MaterialType.PDF_DOCUMENT -> Icons.Default.PictureAsPdf
                                                MaterialType.GOOGLE_DRIVE -> Icons.Default.CloudQueue
                                                MaterialType.VIDEO_LECTURE -> Icons.Default.VideoLibrary
                                                else -> Icons.Default.MenuBook
                                            },
                                            contentDescription = null,
                                            tint = when (material.type) {
                                                MaterialType.PDF_DOCUMENT -> Color(0xFFDC2626)
                                                MaterialType.GOOGLE_DRIVE -> HealthcareTeal
                                                MaterialType.VIDEO_LECTURE -> MedicalBlue
                                                else -> HealthcareGreen
                                            },
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = material.subject,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = HealthcareTeal
                                    )
                                    Text(
                                        text = when (material.type) {
                                            MaterialType.PDF_DOCUMENT -> "Official PDF Document"
                                            MaterialType.GOOGLE_DRIVE -> "Google Drive Resource"
                                            MaterialType.VIDEO_LECTURE -> "Video Lecture"
                                            else -> "Question Bank"
                                        },
                                        fontSize = 10.sp,
                                        color = SlateMedium
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = material.fileSizeBytes,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = SlateMedium,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = material.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(material.accessUrl))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    viewModel.showToast("Opening material link: ${material.accessUrl}")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (material.type == MaterialType.GOOGLE_DRIVE) HealthcareTeal else MedicalBlue
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("open_material_btn_${material.id}")
                        ) {
                            Icon(
                                imageVector = if (material.type == MaterialType.GOOGLE_DRIVE) Icons.Default.CloudDownload else Icons.Default.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (material.type == MaterialType.GOOGLE_DRIVE) "Open Google Drive Folder" else "View & Download Document",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
