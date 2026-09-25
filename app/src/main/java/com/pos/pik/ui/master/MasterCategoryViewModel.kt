package com.pos.pik.ui.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.CategoryEntity
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MasterCategoryViewModel(private val repository: PosRepository) : ViewModel() {

    val categories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveCategory(category: CategoryEntity?, name: String, subname: String) {
        viewModelScope.launch {
            if (category == null) {
                repository.addCategory(name, subname)
            } else {
                repository.updateCategory(category.catId, name, subname)
            }
        }
    }

    fun deleteCategory(catId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = repository.deleteCategory(catId)
            onResult(success)
        }
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MasterCategoryViewModel(repository) as T
        }
    }
}
