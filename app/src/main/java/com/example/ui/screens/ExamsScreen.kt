package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ExamCategory
import com.example.model.MockTest
import com.example.model.Student
import com.example.ui.ParamedicalViewModel
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue

@Composable
fun ExamsScreen(
    viewModel: ParamedicalViewModel,
    student: Student?,
    categories: List<ExamCategory>,
    mockTests: List<MockTest>
) {
    var selectedFilter by remember { mutableStateOf("all") }

    val filteredTests = mockTests.filter { test ->
        when (selectedFilter) {
            "all" -> true
            "free" -> !test.isPaid
            "paid" -> test.isPaid
            "enrolled" -> viewModel.repository.isTestPurchased(test.id)
            else -> test.categoryId == selectedFilter
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Category / Filter Scrollable Chips
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(vertical = 12.dp)
        ) {
            Text(
                text = "CBT Test Series Catalog",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Select an exam category or filter to begin your practice test",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedFilter == "all",
                        onClick = { selectedFilter = "all" },
                        label = { Text("All Exams (${mockTests.size})") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "free",
                        onClick = { selectedFilter = "free" },
                        label = { Text("Free Demos") }
                    )
                }
                item {
                    FilterChip(
                        selected = selectedFilter == "enrolled",
                        onClick = { selectedFilter = "enrolled" },
                        label = { Text("My Tests") }
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedFilter == cat.id,
                        onClick = { selectedFilter = cat.id },
                        label = { Text(cat.shortCode) }
                    )
                }
            }
        }

        // Test list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredTests.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No mock tests found in this category.",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredTests) { test ->
                    val isPurchased = viewModel.repository.isTestPurchased(test.id)
                    ExamCard(
                        test = test,
                        isEnrolled = isPurchased,
                        onStartTest = { viewModel.startCbtExam(test.id) },
                        onUnlock = { viewModel.openPaymentCheckout(test) }
                    )
                }
            }
        }
    }
}
