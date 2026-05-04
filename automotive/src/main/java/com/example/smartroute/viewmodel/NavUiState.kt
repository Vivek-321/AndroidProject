package com.example.smartroute.viewmodel

import com.example.smartroute.data.model.NavStep

data class NavUiState(
    val currentStep: NavStep? = null,
    val isNavigating: Boolean = false,
    val isCompleted: Boolean = false
)
