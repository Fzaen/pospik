package com.pos.pik.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.TodayStats
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(private val repository: PosRepository) : ViewModel() {

    val stats: StateFlow<TodayStats> = repository.getTodayStatsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TodayStats(0, 0.0))

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repository) as T
        }
    }
}
