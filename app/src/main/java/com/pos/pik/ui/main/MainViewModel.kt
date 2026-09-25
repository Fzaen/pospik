package com.pos.pik.ui.main

import androidx.lifecycle.ViewModel
import com.pos.pik.data.local.UserWithRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _currentUser = MutableStateFlow<UserWithRole?>(null)
    val currentUser: StateFlow<UserWithRole?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: UserWithRole?) {
        _currentUser.value = user
    }
}
