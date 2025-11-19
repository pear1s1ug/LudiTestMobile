package com.example.luditestmobilefinal.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val RoundedFontFamily = FontFamily.SansSerif

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp
    ),

    displayMedium = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.25).sp
    ),

    displaySmall = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),

    headlineLarge = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    headlineMedium = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.1.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp
    ),

    bodySmall = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.3.sp
    ),

    labelLarge = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),

    labelMedium = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp
    ),

    labelSmall = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,  // +2
        lineHeight = 16.sp,
        letterSpacing = 0.3.sp
    ),

    titleLarge = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,  // +4
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),

    titleMedium = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,  // +4
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    titleSmall = TextStyle(
        fontFamily = RoundedFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,  // +2
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    )
)