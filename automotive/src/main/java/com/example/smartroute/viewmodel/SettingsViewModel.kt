package com.example.smartroute.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartroute.data.model.SettingsData
import com.example.smartroute.data.repo.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)

    private val _settings = MutableStateFlow(SettingsData())
    val settings: StateFlow<SettingsData> = _settings.asStateFlow()

    val appVersion: String = repository.getAppVersion()
    val buildType: String = repository.getBuildType()
    val automotiveVersion: String = repository.getAutomotiveVersion()

    init {
        viewModelScope.launch {
            repository.settingsFlow().collect {
                _settings.value = it
            }
        }
    }

    suspend fun resetThemeIfApkUpdated() {
        repository.resetThemeIfApkUpdated()
    }

    fun setVoiceInstructions(enabled: Boolean) {
        viewModelScope.launch {
            repository.setVoiceInstructions(enabled)
        }
    }

    fun setAlertIntensity(value: String) {
        viewModelScope.launch {
            repository.setAlertIntensity(value)
        }
    }

    fun setThemeMode(value: String) {
        viewModelScope.launch {
            repository.setThemeMode(value)
        }
    }
}
