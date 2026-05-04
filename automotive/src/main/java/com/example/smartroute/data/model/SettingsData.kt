package com.example.smartroute.data.model

data class SettingsData(
    val voiceInstructionsEnabled: Boolean = true,
    val alertIntensity: String = "Medium",
    val themeMode: String = "System"   // System, Light, Dark
)
