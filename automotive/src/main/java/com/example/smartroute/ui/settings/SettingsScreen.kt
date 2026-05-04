package com.example.smartroute.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartroute.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Back",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Voice Instructions",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (settings.voiceInstructionsEnabled) "Enabled" else "Disabled",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = settings.voiceInstructionsEnabled,
                    onCheckedChange = { viewModel.setVoiceInstructions(it) }
                )
            }

            Text(
                text = "Alert Intensity",
                style = MaterialTheme.typography.titleLarge
            )

            listOf("Low", "Medium", "High").forEach { level ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = settings.alertIntensity == level,
                        onClick = { viewModel.setAlertIntensity(level) }
                    )
                    Text(
                        text = level,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }

            // ✅ Theme mode section
            Text(
                text = "Theme Mode",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf("System", "Light", "Dark").forEach { mode ->
                    OutlinedButton(
                        onClick = { viewModel.setThemeMode(mode) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = mode,
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }

            Text(
                text = "Current Theme: ${settings.themeMode}",
                style = MaterialTheme.typography.bodyLarge
            )

            HorizontalDivider()

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "App Info",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "App Version: ${viewModel.appVersion}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Android Automotive Version: ${viewModel.automotiveVersion}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Build Type: ${viewModel.buildType}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.padding(bottom = 32.dp))
        }
    }
}

