package com.pos.pik.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.*
import com.pos.pik.data.repository.PosRepository
import com.pos.pik.util.Formatters
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class InventoryViewModel(private val repository: PosRepository) : ViewModel() {

    private val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date()).toInt()

    private val _selectedYear = MutableStateFlow(currentYear)
    val selectedYear: StateFlow<Int> = _selectedYear.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val masterStock: StateFlow<List<MasterStockWithProduct>> = combine(_selectedYear, _searchQuery) { year, query ->
        Pair(year, query)
    }.flatMapLatest { (year, query) ->
        repository.getMasterStock(year, query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val products: StateFlow<List<ProductWithCategory>> = repository.getAllProducts(null, null)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Date range for reports
    private val _startDate = MutableStateFlow(Formatters.getCurrentDateFormatted())
    val startDate: StateFlow<String> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(Formatters.getCurrentDateFormatted())
    val endDate: StateFlow<String> = _endDate.asStateFlow()

    val incomingHistory: StateFlow<List<InventoryIncomingWithDetails>> = combine(_startDate, _endDate) { start, end ->
        Pair(start, end)
    }.flatMapLatest { (start, end) ->
        repository.getIncomingHistory(start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val damagedHistory: StateFlow<List<InventoryDamagedWithDetails>> = combine(_startDate, _endDate) { start, end ->
        Pair(start, end)
    }.flatMapLatest { (start, end) ->
        repository.getDamagedHistory(start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val internalUseHistory: StateFlow<List<InventoryInternalUseWithDetails>> = combine(_startDate, _endDate) { start, end ->
        Pair(start, end)
    }.flatMapLatest { (start, end) ->
        repository.getInternalUseHistory(start, end)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onDateRangeChanged(start: String, end: String) {
        _startDate.value = start
        _endDate.value = end
    }

    fun recalculateStock() {
        viewModelScope.launch {
            repository.recalculateAllStock(_selectedYear.value)
        }
    }

    fun recordIncoming(
        userId: Int,
        sku: String,
        packageQty: Int,
        fraction: Int,
        totalCost: Double,
        note: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.recordIncomingInventory(userId, sku, packageQty, fraction, totalCost, note)
            onSuccess()
        }
    }

    fun recordDamaged(
        userId: Int,
        sku: String,
        qty: Int,
        reason: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.recordDamagedInventory(userId, sku, qty, reason)
            onSuccess()
        }
    }

    fun recordInternalUse(
        userId: Int,
        sku: String,
        qty: Int,
        note: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.recordInternalUseInventory(userId, sku, qty, note)
            onSuccess()
        }
    }

    fun performTutupBuku(toYear: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val fromYear = _selectedYear.value
            repository.performTutupBuku(fromYear, toYear)
            _selectedYear.value = toYear
            onSuccess()
        }
    }

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return InventoryViewModel(repository) as T
        }
    }
}
