package com.bank.bareillycitybankofindia.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BankNavy = Color(0xFF0A2540)
val BankNavyDark = Color(0xFF051428)
val BankBlue = Color(0xFF1A5276)
val BankGold = Color(0xFFFFC107)
val BankDarkGold = Color(0xFFD4AC0D)
val BankLightBg = Color(0xFFF4F6F9)
val BankSuccess = Color(0xFF27AE60)
val BankDanger = Color(0xFFE74C3C)

private val LightColorScheme = lightColorScheme(
    primary = BankNavy,
    secondary = BankGold,
    tertiary = BankBlue,
    background = BankLightBg,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFF2C3E50),
    onSurface = Color(0xFF0A2540)
)

@Composable
fun BareillyBankTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
