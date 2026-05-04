package com.example.smartroute.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartroute.data.model.NavigationSession
import com.example.smartroute.data.model.Route
import com.example.smartroute.data.repo.NavigationRepository
import com.example.smartroute.data.repo.NavigationSessionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NavigationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = NavigationRepository()
    private val sessionRepository = NavigationSessionRepository(application)

    private val _routeState = MutableStateFlow(repository.getRoute())
    val routeState: StateFlow<Route> = _routeState.asStateFlow()

    private val _uiState = MutableStateFlow(NavUiState())
    val uiState: StateFlow<NavUiState> = _uiState.asStateFlow()

    private var navigationJob: Job? = null
    private var currentStepIndex: Int = 0

    init {
        restoreNavigationSession()
    }

    fun updateRoute(start: String, destination: String) {
        val safeStart = start.ifBlank { "Home" }
        val safeDestination = destination.ifBlank { "Office" }

        val current = _routeState.value
        _routeState.value = current.copy(
            name = "$safeStart to $safeDestination",
            start = safeStart,
            destination = safeDestination
        )
    }

    fun startNavigation(fromStepIndex: Int = 0) {
        if (_uiState.value.isNavigating) return

        _uiState.value = NavUiState(
            currentStep = _uiState.value.currentStep,
            isNavigating = true,
            isCompleted = false
        )

        navigationJob = viewModelScope.launch {
            repository.simulateNavigation(fromStepIndex).collect { (index, step) ->
                currentStepIndex = index

                _uiState.value = NavUiState(
                    currentStep = step,
                    isNavigating = !step.isArrival,
                    isCompleted = step.isArrival
                )

                saveSession()

                if (step.isArrival) {
                    sessionRepository.clearSession()
                }
            }
        }
    }

    fun resetNavigation() {
        navigationJob?.cancel()
        navigationJob = null
        currentStepIndex = 0
        _uiState.value = NavUiState()

        viewModelScope.launch {
            sessionRepository.clearSession()
        }
    }

    private fun restoreNavigationSession() {
        viewModelScope.launch {
            val session = sessionRepository.getSession() ?: return@launch

            _routeState.value = _routeState.value.copy(
                name = session.routeName,
                start = session.start,
                destination = session.destination
            )

            currentStepIndex = session.currentStepIndex

            if (session.isNavigating && !session.isCompleted) {
                startNavigation(fromStepIndex = currentStepIndex)
            } else if (session.isCompleted) {
                _uiState.value = NavUiState(
                    currentStep = repository.getSteps().lastOrNull(),
                    isNavigating = false,
                    isCompleted = true
                )
            }
        }
    }

    private suspend fun saveSession() {
        val route = _routeState.value
        val session = NavigationSession(
            routeName = route.name,
            start = route.start,
            destination = route.destination,
            currentStepIndex = currentStepIndex,
            isNavigating = _uiState.value.isNavigating,
            isCompleted = _uiState.value.isCompleted
        )
        sessionRepository.saveSession(session)
    }
}
