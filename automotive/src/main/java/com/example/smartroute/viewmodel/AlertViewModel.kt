package com.example.smartroute.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartroute.data.model.RoadAlert
import com.example.smartroute.data.repo.AlertRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel : ViewModel() {

    private val repository = AlertRepository()

    private val _alert = MutableStateFlow<RoadAlert?>(null)
    val alert: StateFlow<RoadAlert?> = _alert

    private var alertJob: Job? = null

    fun startListening() {
        if (alertJob?.isActive == true) return

        alertJob = viewModelScope.launch {
            repository.alerts().collect {
                _alert.value = it
            }
        }
    }

    fun stopListening() {
        alertJob?.cancel()
        alertJob = null
        _alert.value = null
    }

    fun dismiss() {
        _alert.value = null
    }
}

