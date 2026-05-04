package com.example.smartroute.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartroute.data.model.RecentRoute
import com.example.smartroute.data.repo.RecentRoutesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecentRoutesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RecentRoutesRepository(application)

    private val _routes = MutableStateFlow<List<RecentRoute>>(emptyList())
    val routes: StateFlow<List<RecentRoute>> = _routes.asStateFlow()

    init {
        viewModelScope.launch {
            repository.recentRoutesFlow().collect {
                _routes.value = it
            }
        }
    }

    fun clearIfApkUpdated() {
        viewModelScope.launch {
            repository.clearRecentRoutesIfApkUpdated()
        }
    }

    fun addRecentRoute(
        routeName: String,
        start: String,
        destination: String,
        durationMin: Int
    ) {
        viewModelScope.launch {
            repository.addRecentRoute(routeName, start, destination, durationMin)
        }
    }
}
