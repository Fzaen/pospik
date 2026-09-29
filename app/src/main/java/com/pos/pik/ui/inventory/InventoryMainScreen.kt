package com.pos.pik.ui.inventory

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.data.repository.PosRepository
import com.pos.pik.util.Formatters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryMainScreen(
    user: UserWithRole,
    repository: PosRepository,
    onLogout: () -> Unit
) {
    val viewModel: InventoryViewModel = viewModel(
        factory = InventoryViewModel.Factory(repository)
    )

    var currentTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("GUDANG INVENTORY - ${user.usrName}", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("Role: ${user.rolName}", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Keluar / Switch Mode", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    label = { Text("Stok", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Warehouse, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    label = { Text("Masuk", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.MoveToInbox, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    label = { Text("Rusak", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.BrokenImage, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    label = { Text("Khusus", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Outbox, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = currentTab == 4,
                    onClick = { currentTab = 4 },
                    label = { Text("Laporan", fontSize = 11.sp) },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (currentTab) {
                0 -> InventoryStockTab(viewModel)
                1 -> InventoryIncomingTab(user, viewModel)
                2 -> InventoryDamagedTab(user, viewModel)
                3 -> InventoryInternalUseTab(user, viewModel)
                4 -> InventoryReportTab(viewModel)
            }
        }
    }
}

@Composable
fun InventoryStockTab(viewModel: InventoryViewModel) {
    val stockList by viewModel.masterStock.collectAsState()
    val year by viewModel.selectedYear.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current

    var showTutupBukuDialog by remember { mutableStateOf(false) }
    var nextYearText by remember { mutableStateOf((year + 1).toString()) }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(color = Color.White, shadowElevation = 1.dp) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Stok Gudang Tahun $year", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { viewModel.recalculateStock() },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Hitung Stok", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { showTutupBukuDialog = true },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tutup Buku", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Cari produk gudang...", fontSize = 12.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(stockList, key = { it.stId }) { st ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(st.prdName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Awal: ${st.stInitialQty}", fontSize = 12.sp, color = Color.Gray)
                        }
                        Text("Kat: ${st.catName} | SKU: ${st.stPrdSku}", fontSize = 11.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Masuk: +${st.stIncomingQty}", fontSize = 11.sp, color = Color(0xFF2E7D32))
                            Text("Terjual: -${st.stSalesQty}", fontSize = 11.sp, color = Color(0xFFC62828))
                            Text("Rusak: -${st.stDamagedQty}", fontSize = 11.sp, color = Color(0xFFD84315))
                            Text("Khusus: -${st.stInternalUseQty}", fontSize = 11.sp, color = Color(0xFF6A1B9A))
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("HPP: ${Formatters.formatRupiah(st.prdCostPrice)}", fontSize = 11.sp, color = Color.Gray)
                            Text("STOK AKHIR: ${st.stFinalQty}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }

    if (showTutupBukuDialog) {
        AlertDialog(
            onDismissRequest = {},
            properties = androidx.compose.ui.window.DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            title = { Text("Tutup Buku Tahun $year") },
            text = {
                Column {
                    Text("Proses Tutup Buku akan mentransfer Stok Akhir tahun $year menjadi Stok Awal untuk tahun baru:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = nextYearText,
                        onValueChange = { nextYearText = it.filter { c -> c.isDigit() } },
                        label = { Text("Tahun Baru") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetYr = nextYearText.toIntOrNull()
                        if (targetYr != null && targetYr > year) {
                            viewModel.performTutupBuku(targetYr) {
                                showTutupBukuDialog = false
                            }
                        }
                    }
                ) {
                    Text("PROSES TUTUP BUKU")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTutupBukuDialog = false }) { Text("BATAL") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryIncomingTab(user: UserWithRole, viewModel: InventoryViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val products by viewModel.products.collectAsState()

    var selectedSku by remember { mutableStateOf(products.firstOrNull()?.prdSku ?: "") }
    var packageQtyText by remember { mutableStateOf("1") }
    var fractionText by remember { mutableStateOf("10") }
    var totalCostText by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var expandedProduct by remember { mutableStateOf(false) }

    val packageQty = packageQtyText.toIntOrNull() ?: 1
    val fraction = fractionText.toIntOrNull() ?: 1
    val totalQty = packageQty * fraction
    val totalCost = totalCostText.toDoubleOrNull() ?: 0.0
    val unitCost = if (totalQty > 0) totalCost / totalQty else 0.0

    val selectedProduct = products.find { it.prdSku == selectedSku }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("INPUT BARANG MASUK", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))

        // Product Selector Dropdown
        ExposedDropdownMenuBox(
            expanded = expandedProduct,
            onExpandedChange = { expandedProduct = !expandedProduct }
        ) {
            OutlinedTextField(
                value = selectedProduct?.let { "${it.prdName} [${it.prdSku}]" } ?: "Pilih Produk",
                onValueChange = {},
                readOnly = true,
                label = { Text("Produk") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProduct) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedProduct,
                onDismissRequest = { expandedProduct = false }
            ) {
                products.forEach { p ->
                    DropdownMenuItem(
                        text = { Text("${p.prdName} [${p.prdSku}]") },
                        onClick = {
                            selectedSku = p.prdSku
                            expandedProduct = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = packageQtyText,
                onValueChange = { packageQtyText = it.filter { c -> c.isDigit() } },
                label = { Text("Jumlah Dus/Renceng") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = fractionText,
                onValueChange = { fractionText = it.filter { c -> c.isDigit() } },
                label = { Text("Isi per Pack (Fraction)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = totalCostText,
            onValueChange = { totalCostText = it.filter { c -> c.isDigit() } },
            label = { Text("Total Harga Beli / Kulakan (Rp)") },
            placeholder = { Text("Contoh: 10850") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Catatan / Suplier (Opsional)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculations Display
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Kalkulasi Otomatis:", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("• Total Jumlah Fisik Masuk: $totalQty pcs", fontSize = 12.sp)
                Text("• HPP Satuan Baru per Pcs: ${Formatters.formatRupiah(unitCost)}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                Text("* Memasukkan barang masuk ini akan otomatis meng-update HPP produk!", fontSize = 10.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            enabled = selectedSku.isNotBlank() && totalQty > 0 && totalCost > 0,
            onClick = {
                viewModel.recordIncoming(
                    userId = user.usrId,
                    sku = selectedSku,
                    packageQty = packageQty,
                    fraction = fraction,
                    totalCost = totalCost,
                    note = noteText.ifBlank { null },
                    onSuccess = {
                        Toast.makeText(context, "Barang Masuk Berhasil Diinput & HPP Diperbarui!", Toast.LENGTH_SHORT).show()
                        totalCostText = ""
                        noteText = ""
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("SIMPAN BARANG MASUK", fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryDamagedTab(user: UserWithRole, viewModel: InventoryViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val products by viewModel.products.collectAsState()

    var selectedSku by remember { mutableStateOf(products.firstOrNull()?.prdSku ?: "") }
    var qtyText by remember { mutableStateOf("1") }
    var reasonText by remember { mutableStateOf("") }
    var expandedProduct by remember { mutableStateOf(false) }

    val qty = qtyText.toIntOrNull() ?: 0
    val selectedProduct = products.find { it.prdSku == selectedSku }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("INPUT BARANG MUSNAH / RUSAK", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Red)
        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expandedProduct,
            onExpandedChange = { expandedProduct = !expandedProduct }
        ) {
            OutlinedTextField(
                value = selectedProduct?.let { "${it.prdName} [${it.prdSku}]" } ?: "Pilih Produk",
                onValueChange = {},
                readOnly = true,
                label = { Text("Produk") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProduct) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedProduct,
                onDismissRequest = { expandedProduct = false }
            ) {
                products.forEach { p ->
                    DropdownMenuItem(
                        text = { Text("${p.prdName} [${p.prdSku}]") },
                        onClick = {
                            selectedSku = p.prdSku
                            expandedProduct = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = qtyText,
            onValueChange = { qtyText = it.filter { c -> c.isDigit() } },
            label = { Text("Jumlah Rusak / Busuk (Pcs)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = reasonText,
            onValueChange = { reasonText = it },
            label = { Text("Alasan Kerusakan (cth: Pecah / Busuk / Kadaluarsa)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            enabled = selectedSku.isNotBlank() && qty > 0,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick = {
                viewModel.recordDamaged(
                    userId = user.usrId,
                    sku = selectedSku,
                    qty = qty,
                    reason = reasonText.ifBlank { null },
                    onSuccess = {
                        Toast.makeText(context, "Barang Rusak Berhasil Dicatat!", Toast.LENGTH_SHORT).show()
                        qtyText = "1"
                        reasonText = ""
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("SIMPAN BARANG RUSAK", fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryInternalUseTab(user: UserWithRole, viewModel: InventoryViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val products by viewModel.products.collectAsState()

    var selectedSku by remember { mutableStateOf(products.firstOrNull()?.prdSku ?: "") }
    var qtyText by remember { mutableStateOf("1") }
    var noteText by remember { mutableStateOf("") }
    var expandedProduct by remember { mutableStateOf(false) }

    val qty = qtyText.toIntOrNull() ?: 0
    val selectedProduct = products.find { it.prdSku == selectedSku }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("PENGELUARAN KHUSUS (KONSUMSI SENDIRI/ANAK)", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF6A1B9A))
        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expandedProduct,
            onExpandedChange = { expandedProduct = !expandedProduct }
        ) {
            OutlinedTextField(
                value = selectedProduct?.let { "${it.prdName} [${it.prdSku}]" } ?: "Pilih Produk",
                onValueChange = {},
                readOnly = true,
                label = { Text("Produk") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedProduct) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedProduct,
                onDismissRequest = { expandedProduct = false }
            ) {
                products.forEach { p ->
                    DropdownMenuItem(
                        text = { Text("${p.prdName} [${p.prdSku}]") },
                        onClick = {
                            selectedSku = p.prdSku
                            expandedProduct = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = qtyText,
            onValueChange = { qtyText = it.filter { c -> c.isDigit() } },
            label = { Text("Jumlah Fisik Dikeluarkan (Pcs)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Keterangan (cth: Dimakan sendiri, Anak, Konsumsi warung)") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            enabled = selectedSku.isNotBlank() && qty > 0,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
            onClick = {
                viewModel.recordInternalUse(
                    userId = user.usrId,
                    sku = selectedSku,
                    qty = qty,
                    note = noteText.ifBlank { null },
                    onSuccess = {
                        Toast.makeText(context, "Pengeluaran Khusus Berhasil Dicatat!", Toast.LENGTH_SHORT).show()
                        qtyText = "1"
                        noteText = ""
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text("SIMPAN PENGELUARAN KHUSUS", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun InventoryReportTab(viewModel: InventoryViewModel) {
    var subTab by remember { mutableIntStateOf(0) }
    val incomingList by viewModel.incomingHistory.collectAsState()
    val damagedList by viewModel.damagedHistory.collectAsState()
    val internalUseList by viewModel.internalUseHistory.collectAsState()

    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        com.pos.pik.ui.reports.DateRangeFilterBar(
            startDate = startDate,
            endDate = endDate,
            onDateRangeSelected = { start, end ->
                viewModel.onDateRangeChanged(start, end)
            }
        )

        TabRow(selectedTabIndex = subTab) {
            Tab(selected = subTab == 0, onClick = { subTab = 0 }, text = { Text("Barang Masuk", fontSize = 11.sp) })
            Tab(selected = subTab == 1, onClick = { subTab = 1 }, text = { Text("Barang Rusak", fontSize = 11.sp) })
            Tab(selected = subTab == 2, onClick = { subTab = 2 }, text = { Text("Pengeluaran Khusus", fontSize = 11.sp) })
        }

        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            when (subTab) {
                0 -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(incomingList) { item ->
                            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(item.prdName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Tanggal: ${Formatters.formatDateDisplay(item.incDate)} | Oleh: ${item.usrName}", fontSize = 11.sp, color = Color.Gray)
                                    Text("Total Qty: ${item.incTotalQty} pcs (${item.incPackageQty} x ${item.incFraction})", fontSize = 12.sp)
                                    Text("Total Beli: ${Formatters.formatRupiah(item.incTotalCost)} (HPP: ${Formatters.formatRupiah(item.incUnitCost)}/pcs)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    if (!item.incNote.isNullOrBlank()) {
                                        Text("Catatan: ${item.incNote}", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(damagedList) { item ->
                            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(item.prdName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Tanggal: ${Formatters.formatDateDisplay(item.dmgDate)} | Oleh: ${item.usrName}", fontSize = 11.sp, color = Color.Gray)
                                    Text("Qty Rusak: ${item.dmgQty} pcs", fontSize = 12.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                                    if (!item.dmgReason.isNullOrBlank()) {
                                        Text("Alasan: ${item.dmgReason}", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(internalUseList) { item ->
                            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(item.prdName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Tanggal: ${Formatters.formatDateDisplay(item.useDate)} | Oleh: ${item.usrName}", fontSize = 11.sp, color = Color.Gray)
                                    Text("Qty Khusus: ${item.useQty} pcs", fontSize = 12.sp, color = Color(0xFF6A1B9A), fontWeight = FontWeight.Bold)
                                    if (!item.useNote.isNullOrBlank()) {
                                        Text("Keterangan: ${item.useNote}", fontSize = 11.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
