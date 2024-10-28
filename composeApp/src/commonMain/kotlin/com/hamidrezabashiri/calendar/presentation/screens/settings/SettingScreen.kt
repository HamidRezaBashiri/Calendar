package com.hamidrezabashiri.calendar.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hamidrezabashiri.calendar.di.ViewModelProvider
import com.hamidrezabashiri.calendar.presentation.screens.base.BaseScreen

@Composable
fun SettingScreen() {
    val viewModel = ViewModelProvider.provideSettingViewModel()

    BaseScreen(
        viewModel = viewModel,
        effectHandler = { effect ->
            when (effect) {
                is SettingContract.Effect.ShowToast -> {
                    // TODO: Implement toast showing
                    println("Toast: ${effect.message}")
                }
                is SettingContract.Effect.ShowError -> {
                    // TODO: Implement error showing
                    println("Error: ${effect.message}")
                }
            }
        }
    ) { state, onIntent ->
        SettingsContent(
            state = state,
            onThemeChange = { theme ->
                onIntent(SettingContract.Intent.ChangeTheme(theme))
            },
            onLanguageChange = { language ->
                onIntent(SettingContract.Intent.ChangeLanguage(language))
            }
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingContract.State,
    onThemeChange: (SettingContract.Theme) -> Unit,
    onLanguageChange: (SettingContract.Language) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Text(
            text = "Settings",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Theme Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingContract.Theme.values().forEach { theme ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.currentTheme == theme,
                            onClick = { onThemeChange(theme) }
                        )
                        Text(
                            text = theme.name,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        // Language Selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Language",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                SettingContract.Language.values().forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = state.currentLanguage == language,
                            onClick = { onLanguageChange(language) }
                        )
                        Text(
                            text = language.name,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }

        state.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colors.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}