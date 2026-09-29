package com.pos.pik.ui.main

import androidx.lifecycle.ViewModel
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.ui.login.AppMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<UserWithRole?>(null)
    val currentUser: StateFlow<UserWithRole?> = _currentUser.asStateFlow()

    private val _currentMode = MutableStateFlow(AppMode.POS)
    val currentMode: StateFlow<AppMode> = _currentMode.asStateFlow()

    fun setCurrentSession(user: UserWithRole?, mode: AppMode = AppMode.POS) {
        _currentUser.value = user
        _currentMode.value = mode
    }
}
