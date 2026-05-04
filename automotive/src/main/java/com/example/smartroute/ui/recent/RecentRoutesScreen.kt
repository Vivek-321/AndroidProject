package com.example.smartroute.ui.recent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.smartroute.viewmodel.RecentRoutesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentRoutesScreen(
    viewModel: RecentRoutesViewModel,
    onBack: () -> Unit
) {
    val routes by viewModel.routes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Recent Routes",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onBack,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        TextButton(
                            onClick = onBack,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Back",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (routes.isEmpty()) {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "No recent routes available",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Complete one navigation route first.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            } else {
                items(routes) { route ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = route.routeName,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "${route.start} → ${route.destination}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Date: ${route.date}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Duration: ${route.durationMin} min",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}


