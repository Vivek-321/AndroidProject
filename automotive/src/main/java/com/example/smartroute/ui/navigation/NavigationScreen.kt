package com.example.smartroute.ui.navigation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartroute.data.model.TurnDirection
import com.example.smartroute.viewmodel.NavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
viewModel: NavigationViewModel,
onBackToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val step = uiState.currentStep

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Navigation",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = {
                            viewModel.resetNavigation()
                            onBackToHome()
                        },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            if (uiState.isCompleted) {
                Text(
                    text = "Destination reached",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "You have completed the route.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Button(
                    onClick = {
                        viewModel.resetNavigation()
                        onBackToHome()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to Home")
                }
            } else if (step != null) {
                Text(
                    text = directionIcon(step.direction),
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = step.instruction,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Distance to next turn: ${step.distanceToNextMeters} m",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Remaining distance: ${step.remainingDistanceKm} km",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Remaining time: ${step.remainingTimeMin} min",
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                Text(
                    text = "Preparing navigation...",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

private fun directionIcon(direction: TurnDirection): String {
    return when (direction) {
        TurnDirection.LEFT -> "⬅"
        TurnDirection.RIGHT -> "➡"
        TurnDirection.STRAIGHT -> "⬆"
        TurnDirection.ARRIVE -> "📍"
    }
}

