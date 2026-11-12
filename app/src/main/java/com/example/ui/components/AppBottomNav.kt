package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.Screen
import com.example.ui.theme.HealthcareTeal
import com.example.ui.theme.MedicalBlue

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("app_bottom_nav")
    ) {
        val isHome = currentScreen is Screen.Dashboard
        val isExams = currentScreen is Screen.Exams
        val isStudy = currentScreen is Screen.Study
        val isJobs = currentScreen is Screen.Jobs
        val isProfile = currentScreen is Screen.Profile

        NavigationBarItem(
            selected = isHome,
            onClick = { onNavigate(Screen.Dashboard) },
            icon = {
                Icon(
                    imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Home",
                    fontSize = 11.sp,
                    fontWeight = if (isHome) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MedicalBlue,
                selectedTextColor = MedicalBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_home")
        )

        NavigationBarItem(
            selected = isExams,
            onClick = { onNavigate(Screen.Exams) },
            icon = {
                Icon(
                    imageVector = if (isExams) Icons.Filled.Quiz else Icons.Outlined.Quiz,
                    contentDescription = "Exams",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Exams",
                    fontSize = 11.sp,
                    fontWeight = if (isExams) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MedicalBlue,
                selectedTextColor = MedicalBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_exams")
        )

        NavigationBarItem(
            selected = isStudy,
            onClick = { onNavigate(Screen.Study) },
            icon = {
                Icon(
                    imageVector = if (isStudy) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                    contentDescription = "Study",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Study",
                    fontSize = 11.sp,
                    fontWeight = if (isStudy) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HealthcareTeal,
                selectedTextColor = HealthcareTeal,
                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier.testTag("nav_item_study")
        )

        NavigationBarItem(
            selected = isJobs,
            onClick = { onNavigate(Screen.Jobs) },
            icon = {
                Icon(
                    imageVector = if (isJobs) Icons.Filled.Work else Icons.Outlined.WorkOutline,
                    contentDescription = "Jobs",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Jobs",
                    fontSize = 11.sp,
                    fontWeight = if (isJobs) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MedicalBlue,
                selectedTextColor = MedicalBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_jobs")
        )

        NavigationBarItem(
            selected = isProfile,
            onClick = { onNavigate(Screen.Profile) },
            icon = {
                Icon(
                    imageVector = if (isProfile) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "Profile",
                    fontSize = 11.sp,
                    fontWeight = if (isProfile) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MedicalBlue,
                selectedTextColor = MedicalBlue,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.testTag("nav_item_profile")
        )
    }
}
