package com.pos.pik.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    data class Success(val user: UserWithRole) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

class LoginViewModel(private val repository: PosRepository) : ViewModel() {

    var usernameState = MutableStateFlow("admin")
    var passwordState = MutableStateFlow("123")

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login() {
        val username = usernameState.value.trim()
        val password = passwordState.value.trim()

        if (username.isEmpty() || password.isEmpty()) {
            _uiState.value = LoginUiState.Error("Username dan password tidak boleh kosong")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            try {
                val user = repository.login(username, password)
                if (user != null) {
                    _uiState.value = LoginUiState.Success(user)
                } else {
                    _uiState.value = LoginUiState.Error("Login Gagal! Username atau Password salah.")
                }
            } catch (e: Exception) {
                _uiState.value = LoginUiState.Error("Error: ${e.localizedMessage}")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(repository) as T
        }
    }
}
