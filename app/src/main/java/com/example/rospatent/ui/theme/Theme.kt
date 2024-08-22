package com.example.rospatent.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val colorScheme = lightColorScheme(
    primary = Blue,
    secondary = BrightBlue,
    tertiary = DarkBlue,
    background = VeryBrightBLue

)

@Composable
fun RospatentTheme(
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit,
) {
    val colorScheme = colorScheme

    val systemUiController = rememberSystemUiController()

    systemUiController.setSystemBarsColor(
        color = Color.Transparent,
        darkIcons = true
    )
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}