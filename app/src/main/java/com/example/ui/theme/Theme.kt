package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color(0xFF002B5C),
    primaryContainer = Color(0xFF0A4080),
    onPrimaryContainer = Color(0xFFD6E4FF),
    secondary = DarkSecondary,
    onSecondary = Color(0xFF003732),
    secondaryContainer = Color(0xFF005048),
    onSecondaryContainer = Color(0xFF6FF7E8),
    background = DarkBackground,
    onBackground = Color(0xFFE2E8F0),
    surface = DarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = Color(0xFFF87171)
)

private val LightColorScheme = lightColorScheme(
    primary = MedicalBlue,
    onPrimary = Color.White,
    primaryContainer = MedicalBlueLight,
    onPrimaryContainer = MedicalBlueDark,
    secondary = HealthcareTeal,
    onSecondary = Color.White,
    secondaryContainer = HealthcareTealLight,
    onSecondaryContainer = Color(0xFF042F2E),
    tertiary = MedicalCyan,
    background = Color(0xFFF8FAFC),
    onBackground = SlateDark,
    surface = Color.White,
    onSurface = SlateDark,
    surfaceVariant = SlateCardBg,
    onSurfaceVariant = SlateMedium,
    outline = CardBorder,
    error = Color(0xFFDC2626)
)

@Composable
fun AvtarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Use our branded medical theme by default
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AvtarTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
