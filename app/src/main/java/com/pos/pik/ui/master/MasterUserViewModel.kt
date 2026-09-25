package com.pos.pik.ui.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.RoleEntity
import com.pos.pik.data.local.UserEntity
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MasterUserViewModel(private val repository: PosRepository) : ViewModel() {

    val users: StateFlow<List<UserWithRole>> = repository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _roles = MutableStateFlow<List<RoleEntity>>(emptyList())
    val roles: StateFlow<List<RoleEntity>> = _roles.asStateFlow()

    init {
        loadRoles()
    }

    private fun loadRoles() {
        viewModelScope.launch {
            _roles.value = repository.fetchRoles()
        }
    }

    fun saveUser(
        user: UserWithRole?,
        name: String,
        username: String,
        password: String,
        phone: String,
        roleId: Int,
        isActive: Int
    ) {
        viewModelScope.launch {
            if (user == null) {
                repository.addUser(
                    UserEntity(
                        usrRoleId = roleId,
                        usrName = name,
                        usrUsername = username,
                        usrPassword = password,
                        usrPhone = phone,
                        usrIsActive = isActive
                    )
                )
            } else {
                repository.updateUser(
                    UserEntity(
                        usrId = user.usrId,
                        usrRoleId = roleId,
                        usrName = name,
                        usrUsername = username,
                        usrPassword = password,
                        usrPhone = phone,
                        usrIsActive = isActive
                    )
                )
            }
        }
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MasterUserViewModel(repository) as T
        }
    }
}
