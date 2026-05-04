package com.example.smartroute.ui.alerts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartroute.viewmodel.AlertViewModel
import kotlinx.coroutines.delay

@Composable
fun AlertBanner(alertViewModel: AlertViewModel) {

    val alert by alertViewModel.alert.collectAsState()

    alert?.let {
        LaunchedEffect(it) {
            delay(4000) // auto-dismiss
            alertViewModel.dismiss()
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )
        ) {
            Text(
                text = it.message,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
