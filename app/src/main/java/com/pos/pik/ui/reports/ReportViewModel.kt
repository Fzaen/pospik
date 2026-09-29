package com.pos.pik.ui.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pos.pik.data.local.*
import com.pos.pik.data.repository.PosRepository
import com.pos.pik.util.Formatters
import kotlinx.coroutines.flow.*

class ReportViewModel(private val repository: PosRepository) : ViewModel() {

    private val today = Formatters.getCurrentDateFormatted()
    private val fiveDaysAgo = Formatters.getFiveDaysAgoFormatted()

    // Date range for Laba Rugi (Default: 5 days ago to today)
    private val _profitStartDate = MutableStateFlow(fiveDaysAgo)
    val profitStartDate: StateFlow<String> = _profitStartDate.asStateFlow()

    private val _profitEndDate = MutableStateFlow(today)
    val profitEndDate: StateFlow<String> = _profitEndDate.asStateFlow()

    fun updateProfitDateRange(start: String, end: String) {
        _profitStartDate.value = start
        _profitEndDate.value = end
    }

    // Date range for other reports (Default: Today only)
    private val _startDate = MutableStateFlow(today)
    val startDate: StateFlow<String> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow(today)
    val endDate: StateFlow<String> = _endDate.asStateFlow()

    fun updateDateRange(start: String, end: String) {
        _startDate.value = start
        _endDate.value = end
    }

    val profitReport: StateFlow<List<ProfitReportRow>> = combine(_profitStartDate, _profitEndDate) { s, e -> Pair(s, e) }
        .flatMapLatest { (s, e) -> repository.getProfitReport(s, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val itemSalesReport: StateFlow<List<ItemSalesReportRow>> = combine(_startDate, _endDate) { s, e -> Pair(s, e) }
        .flatMapLatest { (s, e) -> repository.getSalesByItemReport(s, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categorySalesReport: StateFlow<List<CategorySalesReportRow>> = combine(_startDate, _endDate) { s, e -> Pair(s, e) }
        .flatMapLatest { (s, e) -> repository.getSalesByCategoryReport(s, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subCategorySalesReport: StateFlow<List<SubCategorySalesReportRow>> = combine(_startDate, _endDate) { s, e -> Pair(s, e) }
        .flatMapLatest { (s, e) -> repository.getSalesBySubCategoryReport(s, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val salesHistory: StateFlow<List<SaleWithUser>> = combine(_startDate, _endDate) { s, e -> Pair(s, e) }
        .flatMapLatest { (s, e) -> repository.getSalesHistory(s, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<PosLogWithDetails>> = combine(_startDate, _endDate) { s, e -> Pair(s, e) }
        .flatMapLatest { (s, e) -> repository.getAuditLogs(s, e) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    class Factory(private val repository: PosRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReportViewModel(repository) as T
        }
    }
}
