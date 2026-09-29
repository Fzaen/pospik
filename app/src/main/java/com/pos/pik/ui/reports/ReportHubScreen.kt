package com.pos.pik.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pos.pik.data.repository.PosRepository
import com.pos.pik.ui.theme.SuccessGreen
import com.pos.pik.util.ExcelExportUtil
import com.pos.pik.util.Formatters
import com.pos.pik.util.PrintHelper
import kotlinx.coroutines.launch

data class SubNavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun ReportHubScreen(
    viewModel: ReportViewModel,
    repository: PosRepository
) {
    var selectedSubIndex by remember { mutableIntStateOf(0) }

    val subNavItems = listOf(
        SubNavItem("Laba Rugi", Icons.Default.BarChart),
        SubNavItem("Per Item", Icons.Default.ListAlt),
        SubNavItem("Per Kategori", Icons.Default.Category),
        SubNavItem("Per Sub-Kat", Icons.Default.AccountTree),
        SubNavItem("Reprint", Icons.Default.Print),
        SubNavItem("Audit", Icons.Default.HistoryEdu)
    )

    val profitStartDate by viewModel.profitStartDate.collectAsState()
    val profitEndDate by viewModel.profitEndDate.collectAsState()

    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (selectedSubIndex == 0) {
            DateRangeFilterBar(
                startDate = profitStartDate,
                endDate = profitEndDate,
                onDateRangeSelected = { start, end ->
                    viewModel.updateProfitDateRange(start, end)
                }
            )
        } else {
            DateRangeFilterBar(
                startDate = startDate,
                endDate = endDate,
                onDateRangeSelected = { start, end ->
                    viewModel.updateDateRange(start, end)
                }
            )
        }

        HorizontalDivider()

        Surface(color = Color.White, shadowElevation = 1.dp) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(subNavItems.size) { index ->
                    val item = subNavItems[index]
                    val isSelected = selectedSubIndex == index
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { selectedSubIndex = index }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .height(2.dp)
                                    .width(20.dp)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider()

        Box(modifier = Modifier.weight(1f)) {
            when (selectedSubIndex) {
                0 -> ProfitReportTab(viewModel)
                1 -> ItemSalesReportTab(viewModel)
                2 -> CategorySalesReportTab(viewModel)
                3 -> SubCategorySalesReportTab(viewModel)
                4 -> ReprintTab(viewModel, repository)
                5 -> AuditLogTab(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeFilterBar(
    startDate: String,
    endDate: String,
    onDateRangeSelected: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }

    Surface(
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedCard(
                onClick = { showPicker = true },
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Periode Tanggal Laporan",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$startDate  s/d  $endDate",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Button(
                        onClick = { showPicker = true },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Pilih Tgl 📅", fontSize = 11.sp)
                    }
                }
            }
        }
    }

    if (showPicker) {
        val dateRangePickerState = rememberDateRangePickerState()

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val startMillis = dateRangePickerState.selectedStartDateMillis
                        val endMillis = dateRangePickerState.selectedEndDateMillis
                        if (startMillis != null) {
                            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).apply {
                                timeZone = java.util.TimeZone.getTimeZone("UTC")
                            }
                            val startStr = sdf.format(java.util.Date(startMillis))
                            val endStr = if (endMillis != null) sdf.format(java.util.Date(endMillis)) else startStr
                            onDateRangeSelected(startStr, endStr)
                        }
                        showPicker = false
                    }
                ) {
                    Text("TERAPKAN", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) {
                    Text("BATAL")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = {
                    Text("Pilih Rentang Tanggal", modifier = Modifier.padding(start = 24.dp, top = 16.dp), fontWeight = FontWeight.Bold)
                },
                headline = {
                    Text("Sentuh tanggal awal & akhir pada kalender", modifier = Modifier.padding(start = 24.dp, top = 8.dp), fontSize = 12.sp, color = Color.Gray)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            )
        }
    }
}

@Composable
fun ProfitReportTab(viewModel: ReportViewModel) {
    val reportData by viewModel.profitReport.collectAsState()

    val totalRevenue = reportData.sumOf { it.totalRevenue }
    val totalCost = reportData.sumOf { it.totalCost }
    val totalProfit = totalRevenue - totalCost

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SummaryBox("Omzet", totalRevenue, MaterialTheme.colorScheme.primary, modifier = Modifier.weight(1f))
            SummaryBox("Laba Bersih", totalProfit, SuccessGreen, modifier = Modifier.weight(1f))
        }

        Surface(color = Color.LightGray.copy(alpha = 0.2f)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text("Tanggal", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2.2f))
                Text("Trx", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.9f))
                Text("Qty", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.9f))
                Text("Omzet", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                Text("Laba", fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reportData) { row ->
                val revenue = row.totalRevenue
                val profit = row.totalRevenue - row.totalCost
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(Formatters.formatDateShort(row.date), fontSize = 11.sp, modifier = Modifier.weight(2.2f))
                    Text("${row.totalInvoices}", fontSize = 11.sp, modifier = Modifier.weight(0.9f))
                    Text("${row.totalQty}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(0.9f))
                    Text(Formatters.formatRupiah(revenue), fontSize = 11.sp, modifier = Modifier.weight(2f))
                    Text(
                        Formatters.formatRupiah(profit),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = SuccessGreen,
                        modifier = Modifier.weight(2f)
                    )
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
fun SummaryBox(title: String, value: Double, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 11.sp, color = color, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(Formatters.formatRupiah(value), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun ItemSalesReportTab(viewModel: ReportViewModel) {
    val context = LocalContext.current
    val reportData by viewModel.itemSalesReport.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Laporan Penjualan Per Item", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            IconButton(onClick = {
                ExcelExportUtil.exportItemReportToCsv(context, reportData, "Laporan Penjualan Per Item")
            }) {
                Icon(Icons.Default.FileDownload, contentDescription = "Export CSV", tint = SuccessGreen)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reportData) { row ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(row.prdName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Kat: ${row.catName} (${row.catSubname})", fontSize = 11.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Qty: ${row.totalQty}  Trx: ${row.totalTrx}", fontSize = 11.sp)
                            Text("Sales: ${Formatters.formatRupiah(row.totalSales)}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text("Margin: ${Formatters.formatRupiah(row.totalProfit)}", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySalesReportTab(viewModel: ReportViewModel) {
    val context = LocalContext.current
    val reportData by viewModel.categorySalesReport.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Laporan Penjualan Per Kategori", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            IconButton(onClick = {
                ExcelExportUtil.exportCategoryReportToCsv(context, reportData, "Laporan Penjualan Per Kategori")
            }) {
                Icon(Icons.Default.FileDownload, contentDescription = "Export CSV", tint = SuccessGreen)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reportData) { row ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(row.catName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Qty: ${row.totalQty}  Trx: ${row.totalTrx}", fontSize = 12.sp)
                            Text("Sales: ${Formatters.formatRupiah(row.totalSales)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text("Margin: ${Formatters.formatRupiah(row.totalProfit)}", fontSize = 12.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SubCategorySalesReportTab(viewModel: ReportViewModel) {
    val context = LocalContext.current
    val reportData by viewModel.subCategorySalesReport.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Laporan Penjualan Per Sub-Kategori", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            IconButton(onClick = {
                ExcelExportUtil.exportSubCategoryReportToCsv(context, reportData, "Laporan Penjualan Per Sub-Kategori")
            }) {
                Icon(Icons.Default.FileDownload, contentDescription = "Export CSV", tint = SuccessGreen)
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(reportData) { row ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${row.catName} - ${row.catSubname}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Qty: ${row.totalQty}  Trx: ${row.totalTrx}", fontSize = 12.sp)
                            Text("Sales: ${Formatters.formatRupiah(row.totalSales)}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text("Margin: ${Formatters.formatRupiah(row.totalProfit)}", fontSize = 12.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReprintTab(viewModel: ReportViewModel, repository: PosRepository) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val salesHistory by viewModel.salesHistory.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(salesHistory, key = { it.slsInvoiceNumber }) { s ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(s.slsInvoiceNumber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            "${Formatters.formatDateDisplay(s.slsTransactionDate)} | ${s.usrUsername}",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            Formatters.formatRupiah(s.slsGrandTotal),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    val sale = repository.getSaleByInvoice(s.slsInvoiceNumber)
                                    val items = repository.getSaleItemsByInvoice(s.slsInvoiceNumber)
                                    val settings = repository.getSettingsSync()
                                    if (sale != null) {
                                        PrintHelper.printDirect(context, sale, items, settings, isReprint = true)
                                    }
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("REPRINT", fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AuditLogTab(viewModel: ReportViewModel) {
    val logs by viewModel.auditLogs.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(logs, key = { it.logId }) { log ->
            val isDelete = log.logAction == "DELETE"
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${log.prdName} (${log.logPrdSku})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Aksi: ${log.logAction} | Oleh: ${log.usrName}", fontSize = 11.sp, color = Color.Gray)
                        Text("Qty: ${log.logOldQty} -> ${log.logNewQty}", fontSize = 11.sp)
                        Text(Formatters.formatDateDisplay(log.logTimestamp), fontSize = 10.sp, color = Color.LightGray)
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isDelete) Color.Red.copy(alpha = 0.1f) else Color(0xFFFF9800).copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = log.logAction,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDelete) Color.Red else Color(0xFFFF9800),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
