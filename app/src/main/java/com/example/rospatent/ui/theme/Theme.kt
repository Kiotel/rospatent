package com.example.rospatent.ui.theme

import android.app.Activity
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
import com.google.accompanist.systemuicontroller.rememberSystemUiController


private val LightColorScheme = lightColorScheme(
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
  val colorScheme = LightColorScheme

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