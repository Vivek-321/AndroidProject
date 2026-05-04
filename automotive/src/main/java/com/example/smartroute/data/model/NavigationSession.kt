package com.example.smartroute.data.model

data class NavigationSession(
    val routeName: String,
    val start: String,
    val destination: String,
    val currentStepIndex: Int,
    val isNavigating: Boolean,
    val isCompleted: Boolean
)
