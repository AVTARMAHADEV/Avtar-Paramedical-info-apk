package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamAttempt
import com.example.model.MockTest
import com.example.ui.ParamedicalViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@Composable
fun CbtResultScreen(
    viewModel: ParamedicalViewModel,
    attemptId: String,
    onBackToHome: () -> Unit
) {
    val attempt = viewModel.repository.getAttemptById(attemptId)
    val test = attempt?.let { viewModel.repository.getTestById(it.testId) }

    if (attempt == null || test == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Examination attempt not found.")
        }
        return
    }

    val minutes = attempt.timeTakenSeconds / 60
    val seconds = attempt.timeTakenSeconds % 60
    val timeFormatted = String.format("%02dm %02ds", minutes, seconds)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Scorecard Hero Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .testTag("result_scorecard_container")
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(MedicalBlue, HealthcareTeal))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Trophy",
                            tint = Color.White,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "CBT Result & Performance Report",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = attempt.testTitle,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Marks Obtained Big Highlight
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = String.format("%.2f", attempt.scoreObtained),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MedicalBlue
                        )
                        Text(
                            text = " / ${attempt.totalMarks.toInt()} Marks",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SlateMedium,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Key Metrics Grid (Accuracy, Time Taken, Percentile, All India Rank)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ResultMetricItem(
                            label = "Accuracy",
                            value = "${attempt.accuracy.toInt()}%",
                            icon = Icons.Default.Speed,
                            tint = HealthcareTeal,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricItem(
                            label = "Time Taken",
                            value = timeFormatted,
                            icon = Icons.Default.Timer,
                            tint = MedicalCyan,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricItem(
                            label = "Percentile",
                            value = "${attempt.percentile}%",
                            icon = Icons.Default.TrendingUp,
                            tint = HealthcareGreen,
                            modifier = Modifier.weight(1f)
                        )
                        ResultMetricItem(
                            label = "Est. Rank",
                            value = "#${attempt.rank}",
                            icon = Icons.Default.MilitaryTech,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Questions breakdown bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${attempt.correctCount}",
                                fontWeight = FontWeight.Bold,
                                color = HealthcareGreen,
                                fontSize = 16.sp
                            )
                            Text("Correct", fontSize = 11.sp, color = SlateMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${attempt.incorrectCount}",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 16.sp
                            )
                            Text("Incorrect", fontSize = 11.sp, color = SlateMedium)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${attempt.unattemptedCount}",
                                fontWeight = FontWeight.Bold,
                                color = SlateLight,
                                fontSize = 16.sp
                            )
                            Text("Unattempted", fontSize = 11.sp, color = SlateMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = onBackToHome,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("result_back_home_btn")
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Dashboard")
                        }

                        Button(
                            onClick = { viewModel.startCbtExam(test.id) },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalBlue),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("result_retake_btn")
                        ) {
                            Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retake Test")
                        }
                    }
                }
            }
        }

        // Detailed Question Solutions Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FactCheck,
                    contentDescription = null,
                    tint = MedicalBlue,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Question-Wise Solutions & Medical Rationales",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Questions List with Student's Answer and Medical Explanation
        itemsIndexed(test.questions) { index, q ->
            val studentAnswer = attempt.userAnswers[q.id]
            val isCorrect = studentAnswer == q.correctOption
            val isUnattempted = studentAnswer == null

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        when {
                            isCorrect -> HealthcareGreen.copy(alpha = 0.5f)
                            isUnattempted -> CardBorder
                            else -> MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        },
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = "Q${index + 1} • ${q.section}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when {
                                isCorrect -> HealthcareGreenLight
                                isUnattempted -> MaterialTheme.colorScheme.surfaceVariant
                                else -> Color(0xFFFEE2E2)
                            }
                        ) {
                            Text(
                                text = when {
                                    isCorrect -> "CORRECT (+${test.marksPerCorrect})"
                                    isUnattempted -> "NOT ATTEMPTED (0)"
                                    else -> "INCORRECT (-${test.negativeMarks})"
                                },
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isCorrect -> HealthcareGreen
                                    isUnattempted -> SlateMedium
                                    else -> Color(0xFFDC2626)
                                },
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = q.text,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val options = q.getOptions()
                    val labels = listOf("A", "B", "C", "D")

                    options.forEachIndexed { optIdx, optText ->
                        val isChosen = studentAnswer == optIdx
                        val isRightAnswer = q.correctOption == optIdx

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when {
                                isRightAnswer -> HealthcareGreenLight
                                isChosen && !isRightAnswer -> Color(0xFFFEE2E2)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${labels[optIdx]}. $optText",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )

                                if (isRightAnswer) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = HealthcareGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else if (isChosen) {
                                    Icon(
                                        imageVector = Icons.Default.Cancel,
                                        contentDescription = "Your choice",
                                        tint = Color(0xFFDC2626),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Medical Explanation Box
                    Surface(
                        color = MedicalBlueLight,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    contentDescription = null,
                                    tint = MedicalBlue,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Explanation & Paramedical Rationale:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalBlueDark
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = q.explanation,
                                fontSize = 12.sp,
                                color = MedicalBlueDark,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ResultMetricItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Text(text = label, fontSize = 9.sp, color = SlateMedium)
        }
    }
}
