package com.example.smartroute.ui.home

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.smartroute.ui.navigation.NavigationScreen
import com.example.smartroute.viewmodel.NavigationViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    navigationViewModel: NavigationViewModel,
    onOpenRecentRoutes: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var navigating by rememberSaveable { mutableStateOf(false) }

    val route by navigationViewModel.routeState.collectAsState()
    var startText by rememberSaveable { mutableStateOf(route.start) }
    var destinationText by rememberSaveable { mutableStateOf(route.destination) }

    if (navigating) {
        NavigationScreen(
            viewModel = navigationViewModel,
            onBackToHome = { navigating = false }
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "${startText} to ${destinationText}",
                style = MaterialTheme.typography.headlineLarge
            )

            // Compact editable start/destination fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CompactLocationField(
                    label = "Start",
                    value = startText,
                    onValueChange = { startText = it },
                    modifier = Modifier.weight(1f)
                )

                CompactLocationField(
                    label = "Destination",
                    value = destinationText,
                    onValueChange = { destinationText = it },
                    modifier = Modifier.weight(1f)
                )
            }

            // Quick mock locations for demo
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    "Home",
                    "Office"
                ).forEach { place ->
                    OutlinedButton(
                        onClick = {
                            if (startText.isBlank() || startText == route.start) {
                                startText = place
                            } else {
                                destinationText = place
                            }
                        }
                    ) {
                        Text(place)
                    }
                }

                OutlinedButton(
                    onClick = {
                        val temp = startText
                        startText = destinationText
                        destinationText = temp
                    }
                ) {
                    Text("Swap")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = "Distance",
                    value = "${route.distanceKm} km",
                    modifier = Modifier.weight(1f)
                )

                StatCard(
                    title = "ETA",
                    value = "${route.etaMinutes} min",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    navigationViewModel.updateRoute(startText, destinationText)
                    navigationViewModel.startNavigation()
                    navigating = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
            ) {
                Text(
                    text = "Start Navigation",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Bring back Recent Routes + Settings buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenRecentRoutes,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text(
                        text = "Recent Routes",
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                OutlinedButton(
                    onClick = onOpenSettings,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactLocationField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = modifier
    )
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}