package com.example.rospatent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.rospatent.ui.theme.RospatentTheme
import kotlinx.coroutines.launch
import java.security.Permissions

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    installSplashScreen()
    enableEdgeToEdge()
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      RospatentTheme {
        Scaffold { innerPadding ->
          AppScreen(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@Composable
private fun AppScreen(modifier: Modifier = Modifier, viewModel: AppViewModel = AppViewModel()) {

  var search by rememberSaveable { mutableStateOf("") }
  var isSearched by rememberSaveable { mutableStateOf(false) }
  val coroutineScope = rememberCoroutineScope()
  when (isSearched) {
    false -> MainScreen(modifier = modifier,
      search,
      onSearchChange = { search = it },
      onSubmit = {
        isSearched = true
        coroutineScope.launch { patents = AppViewModel().searchPatents(search).toMutableList()
        }
      })

    true -> FoundScreen(
      modifier = modifier.background(MaterialTheme.colorScheme.background),
      search,
      onSearchChange = { search = it },
      onSubmit = {
        coroutineScope.launch { patents = AppViewModel().searchPatents(search).toMutableList() }
      },
      onBack = {
        isSearched = false
        search = ""
      }
    )
  }
}

@Composable
private fun MainScreen(
  modifier: Modifier = Modifier,
  search: String,
  onSearchChange: (String) -> Unit,
  onSubmit: () -> Unit,
) {
  Column(
    modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
  ) {
    val isFocused by keyboardAsState()
    Text(
      text = "Роспатент.",
      color = Color.Black,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.W500,
      modifier = Modifier.padding(start = 30.dp, top = 15.dp)
    )
    Box {
      Image(
        painter = painterResource(id = R.drawable.search_screen_background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        alignment = Alignment.BottomCenter
      )
      Column(
        modifier = modifier
          .align(if (isFocused) Alignment.Center else Alignment.BottomCenter)
          .padding(
            bottom = if (isFocused) 200.dp else 100.dp
          )
          .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally

      ) {
        AnimatedVisibility(!isFocused) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = 30.dp, bottom = 40.dp)
          ) {
            Text(
              color = Color.White,
              text = "Самый простой",
              style = MaterialTheme.typography.headlineLarge,
            )
            Text(
              color = Color.White,
              text = "поиск патентов",
              style = MaterialTheme.typography.headlineLarge,
            )
          }
        }
        TextField(
          search = search,
          isDark = isFocused,
          onSearchChange = { onSearchChange(it) },
          onSubmit = { onSubmit() },
          darkColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedBorderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            focusedBorderColor = Color.Gray,
            focusedPlaceholderColor = Color.Gray,
          ),
          lightColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedBorderColor = Color.White,
            unfocusedPlaceholderColor = Color.White,
            focusedBorderColor = Color.White,
          ),
          trailingIcon = {
            Icon(
              modifier = Modifier.clickable(onClick = { onSubmit() }),
              imageVector = Icons.Outlined.Search,
              contentDescription = null,
              tint = if (isFocused) Color.Gray else Color.White
            )
          }
        )
      }
    }
  }
}

//TODO: Переделать это неоптимизированное говно
@Composable
fun keyboardAsState(): State<Boolean> {
  val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
  return rememberUpdatedState(isImeVisible)
}

@Composable
fun TextField(
  modifier: Modifier = Modifier,
  search: String,
  onSearchChange: (String) -> Unit,
  onSubmit: () -> Unit,
  isDark: Boolean = false,
  darkColors: TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.DarkGray,
    unfocusedBorderColor = Color.DarkGray,
    unfocusedPlaceholderColor = Color.DarkGray,
    focusedBorderColor = Color.DarkGray,
    focusedPlaceholderColor = Color.DarkGray,
  ),
  lightColors: TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedBorderColor = Color.White,
    unfocusedPlaceholderColor = Color.White,
    focusedBorderColor = Color.White,
    focusedPlaceholderColor = Color.White,
  ),
  trailingIcon: @Composable (() -> Unit)? = null,
  shape: Shape = RoundedCornerShape(10.dp),
) {
  OutlinedTextField(modifier = modifier, colors = when (isDark) {
    true -> darkColors
    false -> lightColors
  },
    value = search,
    onValueChange = { onSearchChange(it) },
    keyboardActions = KeyboardActions(
      onDone = { onSubmit() }
    ),
    singleLine = true,
    shape = shape,
    placeholder = {
      Text(text = "Искать патент")
    },
    trailingIcon = trailingIcon
  )
}

@Preview
@Composable
fun AppPreview() {
  Scaffold { innerPadding ->
    AppScreen(modifier = Modifier.padding(innerPadding))
  }
}