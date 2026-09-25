package com.pos.pik.ui.master

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.CategoryEntity
import com.pos.pik.data.local.ProductEntity
import com.pos.pik.data.local.ProductWithCategory
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MasterProductViewModel(private val repository: PosRepository) : ViewModel() {

    private val _selectedMainCategory = MutableStateFlow<String?>(null)
    val selectedMainCategory: StateFlow<String?> = _selectedMainCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val products: StateFlow<List<ProductWithCategory>> = combine(_selectedMainCategory, _searchQuery) { mainCat, query ->
        Pair(mainCat, query)
    }.flatMapLatest { (mainCat, query) ->
        repository.getAllProducts(mainCat, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mainCategories: StateFlow<List<String>> = flow {
        emit(repository.getMainCategories())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectMainCategory(catName: String?) {
        _selectedMainCategory.value = catName
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun toggleProductStatus(product: ProductWithCategory) {
        viewModelScope.launch {
            repository.updateProduct(
                ProductEntity(
                    prdSku = product.prdSku,
                    prdCategoryId = product.prdCategoryId,
                    prdName = product.prdName,
                    prdCostPrice = product.prdCostPrice,
                    prdSellingPrice = product.prdSellingPrice,
                    prdImage = product.prdImage,
                    prdIsActive = if (product.prdIsActive == 1) 0 else 1
                )
            )
        }
    }

    fun saveProduct(
        product: ProductWithCategory?,
        catId: Int,
        name: String,
        costPrice: Double,
        sellingPrice: Double,
        imagePath: String?
    ) {
        viewModelScope.launch {
            if (product == null) {
                val cat = categories.value.find { it.catId == catId }
                val sku = repository.generateNextSku(cat?.catName ?: "Makanan")
                repository.addProduct(
                    ProductEntity(
                        prdSku = sku,
                        prdCategoryId = catId,
                        prdName = name.uppercase(),
                        prdCostPrice = costPrice,
                        prdSellingPrice = sellingPrice,
                        prdImage = imagePath,
                        prdIsActive = 1
                    )
                )
            } else {
                repository.updateProduct(
                    ProductEntity(
                        prdSku = product.prdSku,
                        prdCategoryId = catId,
                        prdName = name.uppercase(),
                        prdCostPrice = costPrice,
                        prdSellingPrice = sellingPrice,
                        prdImage = imagePath,
                        prdIsActive = product.prdIsActive
                    )
                )
            }
        }
    }

    fun deleteProduct(product: ProductWithCategory) {
        viewModelScope.launch {
            repository.deleteProduct(product.prdSku)
            val imagePath = product.prdImage
            if (imagePath != null && imagePath.startsWith("file://") && !imagePath.startsWith("file:///android_asset/")) {
                try {
                    val filePath = imagePath.removePrefix("file://")
                    val file = java.io.File(filePath)
                    if (file.exists()) {
                        file.delete()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun deleteAllProducts(adminPassword: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val isValidAdmin = repository.verifyAdminPassword(adminPassword)
            if (isValidAdmin) {
                repository.deleteAllProducts()
                onResult(true, "Semua produk berhasil dihapus.")
            } else {
                onResult(false, "Password Admin salah!")
            }
        }
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MasterProductViewModel(repository) as T
        }
    }
}
