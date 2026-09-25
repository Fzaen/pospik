package com.pos.pik.ui.pos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.CartItemWithProduct
import com.pos.pik.data.local.ProductWithCategory
import com.pos.pik.data.repository.PosRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PosViewModel(private val repository: PosRepository) : ViewModel() {

    private val _mainCategories = MutableStateFlow<List<String>>(emptyList())
    val mainCategories: StateFlow<List<String>> = _mainCategories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _products = MutableStateFlow<List<ProductWithCategory>>(emptyList())
    val products: StateFlow<List<ProductWithCategory>> = _products.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithProduct>> = _cartItems.asStateFlow()

    private var searchJob: Job? = null

    fun loadData(userId: Int) {
        viewModelScope.launch {
            _mainCategories.value = repository.getMainCategories()
            observeProducts()
            observeCart(userId)
        }
    }

    private fun observeProducts() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            repository.getActiveProducts(_selectedCategory.value, _searchQuery.value)
                .collect { list ->
                    _products.value = list
                }
        }
    }

    private fun observeCart(userId: Int) {
        viewModelScope.launch {
            repository.getActiveCart(userId).collect { cart ->
                _cartItems.value = cart
            }
        }
    }

    fun selectCategory(catName: String?) {
        _selectedCategory.value = catName
        observeProducts()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(400)
            observeProducts()
        }
    }

    fun addToCart(userId: Int, product: ProductWithCategory) {
        viewModelScope.launch {
            repository.addToCart(
                userId = userId,
                sku = product.prdSku,
                qty = 1,
                price = product.prdSellingPrice,
                costPrice = product.prdCostPrice
            )
        }
    }

    fun updateQty(userId: Int, cartId: Int, newQty: Int) {
        viewModelScope.launch {
            repository.updateCartQty(userId, cartId, newQty)
        }
    }

    fun removeFromCart(userId: Int, cartId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(userId, cartId)
        }
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PosViewModel(repository) as T
        }
    }
}
