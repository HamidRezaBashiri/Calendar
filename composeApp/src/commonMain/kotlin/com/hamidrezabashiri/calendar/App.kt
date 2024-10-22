package com.hamidrezabashiri.calendar


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hamidrezabashiri.calendar.ui.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()){
            Text("stringResource(Res.string.app_name)")
        }
    }
}