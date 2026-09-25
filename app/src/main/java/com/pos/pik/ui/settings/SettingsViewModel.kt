package com.pos.pik.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.AppSettingEntity
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: PosRepository) : ViewModel() {

    val settings: StateFlow<AppSettingEntity?> = repository.getSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun saveSettings(
        warungName: String,
        address: String,
        phone: String,
        printer: String?,
        paperSize: Int,
        margin: Double
    ) {
        viewModelScope.launch {
            repository.updateSettings(
                AppSettingEntity(
                    setId = 1,
                    setWarungName = warungName.uppercase(),
                    setAddress = address,
                    setPhone = phone,
                    setDefaultPrinter = printer,
                    setPaperSize = paperSize,
                    setMargin = margin
                )
            )
        }
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repository) as T
        }
    }
}
