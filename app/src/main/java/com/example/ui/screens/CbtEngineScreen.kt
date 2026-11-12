package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import com.example.model.Question
import com.example.model.QuestionStatus
import com.example.ui.CbtUiState
import com.example.ui.ParamedicalViewModel
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CbtEngineScreen(
    viewModel: ParamedicalViewModel,
    cbtState: CbtUiState
) {
    val test = cbtState.test ?: return
    val currentQuestion = cbtState.currentQuestion ?: return
    val currentIndex = cbtState.currentQuestionIndex
    val totalQuestions = test.questions.size

    val selectedOption = cbtState.selectedOptions[currentQuestion.id]
    val isMarkedForReview = cbtState.markedReviewIds.contains(currentQuestion.id)

    // Formatted timer (HH:MM:SS or MM:SS)
    val minutes = cbtState.remainingSeconds / 60
    val seconds = cbtState.remainingSeconds % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)
    val isUrgentTime = cbtState.remainingSeconds < 300 // Under 5 minutes

    // Summary counts for palette and submit
    val answeredCount = cbtState.selectedOptions.size
    val reviewCount = cbtState.markedReviewIds.size
    val unattemptedCount = (totalQuestions - answeredCount).coerceAtLeast(0)

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = test.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = currentQuestion.section,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Marks: +${test.marksPerCorrect} | -${test.negativeMarks}",
                                    fontSize = 11.sp,
                                    color = SlateMedium
                                )
                            }
                        }

                        // Countdown Timer Clock
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isUrgentTime) Color(0xFFFEE2E2) else HealthcareTealLight,
                            modifier = Modifier.testTag("exam_timer_badge")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = if (isUrgentTime) Color(0xFFDC2626) else HealthcareTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = timerString,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUrgentTime) Color(0xFFDC2626) else HealthcareTeal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Submit Button
                        Button(
                            onClick = { viewModel.toggleSubmitConfirm(true) },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("top_submit_exam_btn")
                        ) {
                            Text("Submit", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        },
        bottomBar = {
            // CBT Action Controls Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Previous
                        OutlinedButton(
                            onClick = { viewModel.previousQuestion() },
                            enabled = currentIndex > 0,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("cbt_prev_button")
                        ) {
                            Text("Previous", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }

                        // Clear Response
                        OutlinedButton(
                            onClick = { viewModel.clearResponse() },
                            enabled = selectedOption != null,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SlateMedium),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("cbt_clear_button")
                        ) {
                            Text("Clear", fontSize = 11.sp)
                        }

                        // Mark for Review & Next
                        Button(
                            onClick = {
                                viewModel.toggleMarkForReview()
                                viewModel.nextQuestion()
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CbtMarkedReview),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(44.dp)
                                .testTag("cbt_review_next_button")
                        ) {
                            Text(
                                text = if (isMarkedForReview) "Unmark & Next" else "Mark & Next",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Save & Next (Primary)
                        Button(
                            onClick = {
                                if (currentIndex < totalQuestions - 1) {
                                    viewModel.nextQuestion()
                                } else {
                                    viewModel.toggleSubmitConfirm(true)
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                            modifier = Modifier
                                .weight(1.4f)
                                .height(44.dp)
                                .testTag("cbt_save_next_button")
                        ) {
                            Text(
                                text = if (currentIndex < totalQuestions - 1) "Save & Next" else "Save & Submit",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Question Header & Question Palette Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(MedicalBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${currentIndex + 1}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Question ${currentIndex + 1} of $totalQuestions",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Question Palette Quick Button
                AssistChip(
                    onClick = { viewModel.toggleQuestionPalette(true) },
                    label = { Text("Question Palette", fontSize = 11.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MedicalBlue
                        )
                    },
                    modifier = Modifier.testTag("open_question_palette_btn")
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Question Text Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentQuestion.text,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )

                    if (currentQuestion.subjectTopic.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Topic: ${currentQuestion.subjectTopic}",
                            fontSize = 11.sp,
                            color = HealthcareTeal,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Options List (A, B, C, D)
            Text(
                text = "Select one answer option:",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            val options = currentQuestion.getOptions()
            val optionLabels = listOf("A", "B", "C", "D")

            options.forEachIndexed { optIndex, optionText ->
                val isSelected = selectedOption == optIndex
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    tonalElevation = if (isSelected) 3.dp else 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MedicalBlue else CardBorder,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.selectOption(optIndex) }
                        .testTag("cbt_option_${optIndex}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MedicalBlue else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = optionLabels[optIndex],
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = optionText,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f),
                            lineHeight = 20.sp
                        )

                        RadioButton(
                            selected = isSelected,
                            onClick = { viewModel.selectOption(optIndex) }
                        )
                    }
                }
            }
        }
    }

    // Question Palette Bottom Sheet / Modal
    if (cbtState.showQuestionPalette) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleQuestionPalette(false) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Question Navigation Palette",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Status Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PaletteLegendItem(color = CbtAnswered, label = "Answered ($answeredCount)")
                    PaletteLegendItem(color = CbtUnanswered, label = "Unanswered ($unattemptedCount)")
                    PaletteLegendItem(color = CbtMarkedReview, label = "Review ($reviewCount)")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 5-Column Grid of Question Badges
                LazyVerticalGrid(
                    columns = GridCells.Fixed(5),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    itemsIndexed(test.questions) { index, q ->
                        val status = cbtState.getStatusForQuestion(q.id)
                        val isCurrent = index == currentIndex

                        val bg = when (status) {
                            QuestionStatus.ANSWERED -> CbtAnswered
                            QuestionStatus.MARKED_FOR_REVIEW -> CbtMarkedReview
                            QuestionStatus.ANSWERED_AND_MARKED -> CbtAnsweredMarked
                            else -> if (cbtState.selectedOptions.containsKey(q.id)) CbtAnswered else CbtNotVisited
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(bg)
                                .border(
                                    width = if (isCurrent) 2.dp else 0.dp,
                                    color = if (isCurrent) Color.Yellow else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.jumpToQuestion(index) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Final Submission Confirmation Dialog
    if (cbtState.showSubmitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleSubmitConfirm(false) },
            title = {
                Text(
                    text = "Submit CBT Mock Exam?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Are you sure you want to finish and submit your exam? You cannot modify answers after submission.",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Total Questions: $totalQuestions", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text("Answered: $answeredCount", fontSize = 12.sp, color = HealthcareGreen, fontWeight = FontWeight.Bold)
                            Text("Unanswered: $unattemptedCount", fontSize = 12.sp, color = MaterialTheme.colorScheme.error)
                            Text("Marked for Review: $reviewCount", fontSize = 12.sp, color = CbtMarkedReview)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.confirmManualSubmission() },
                    colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                    modifier = Modifier.testTag("confirm_submit_btn")
                ) {
                    Text("Confirm & Submit")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { viewModel.toggleSubmitConfirm(false) }) {
                    Text("Resume Test")
                }
            }
        )
    }
}

@Composable
fun PaletteLegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 11.sp, color = SlateMedium)
    }
}
