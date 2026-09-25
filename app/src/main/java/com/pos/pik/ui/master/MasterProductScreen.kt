package com.pos.pik.ui.master

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pos.pik.data.local.CategoryEntity
import com.pos.pik.data.local.ProductWithCategory
import com.pos.pik.util.Formatters

@Composable
fun MasterProductScreen(
    productViewModel: MasterProductViewModel,
    categoryViewModel: MasterCategoryViewModel
) {
    var subTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = subTab == 0,
                    onClick = { subTab = 0 },
                    label = { Text("Item Produk", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Fastfood, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = subTab == 1,
                    onClick = { subTab = 1 },
                    label = { Text("Kategori", fontSize = 12.sp) },
                    icon = { Icon(Icons.Default.Category, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (subTab == 0) {
                ProductListView(viewModel = productViewModel)
            } else {
                MasterCategoryScreen(viewModel = categoryViewModel)
            }
        }
    }
}

@Composable
fun ProductListView(viewModel: MasterProductViewModel) {
    val products by viewModel.products.collectAsState()
    val mainCategories by viewModel.mainCategories.collectAsState()
    val selectedMainCategory by viewModel.selectedMainCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val categories by viewModel.categories.collectAsState()

    var showFormDialog by remember { mutableStateOf(false) }
    var editingProduct by remember { mutableStateOf<ProductWithCategory?>(null) }
    var deletingProduct by remember { mutableStateOf<ProductWithCategory?>(null) }

    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var adminPasswordText by remember { mutableStateOf("") }
    var passwordErrorMsg by remember { mutableStateOf<String?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingProduct = null
                    showFormDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Produk")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar & Filter
            Surface(color = Color.White, shadowElevation = 1.dp) {
                Column(modifier = Modifier.padding(12.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Cari nama atau SKU...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = null)
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LazyRow(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedMainCategory == null,
                                    onClick = { viewModel.selectMainCategory(null) },
                                    label = { Text("Semua") }
                                )
                            }
                            items(mainCategories) { cat ->
                                FilterChip(
                                    selected = selectedMainCategory == cat,
                                    onClick = { viewModel.selectMainCategory(cat) },
                                    label = { Text(cat) }
                                )
                            }
                        }

                        if (products.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    adminPasswordText = ""
                                    passwordErrorMsg = null
                                    showDeleteAllDialog = true
                                }
                            ) {
                                Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(2.dp))
                                Text("Hapus All", fontSize = 11.sp, color = Color.Red, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products, key = { it.prdSku }) { p ->
                    ProductRowCard(
                        product = p,
                        onToggleStatus = { viewModel.toggleProductStatus(p) },
                        onEdit = {
                            editingProduct = p
                            showFormDialog = true
                        },
                        onDelete = {
                            deletingProduct = p
                        }
                    )
                }
            }
        }
    }

    if (showFormDialog) {
        ProductFormDialog(
            product = editingProduct,
            categories = categories,
            onDismiss = { showFormDialog = false },
            onSave = { catId, customSku, name, cost, sell, img ->
                viewModel.saveProduct(editingProduct, catId, customSku, name, cost, sell, img)
                showFormDialog = false
            }
        )
    }

    // Confirm Single Product Delete Dialog
    if (deletingProduct != null) {
        val prod = deletingProduct!!
        AlertDialog(
            onDismissRequest = { deletingProduct = null },
            title = { Text("Konfirmasi Hapus Produk") },
            text = { Text("Apakah Anda yakin ingin menghapus produk \"${prod.prdName}\"? Data dan foto produk akan terhapus.") },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        viewModel.deleteProduct(prod)
                        deletingProduct = null
                    }
                ) {
                    Text("HAPUS")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingProduct = null }) {
                    Text("BATAL")
                }
            }
        )
    }

    // Confirm Delete All Products Dialog (Admin Password Required)
    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = { Text("Hapus Semua Produk") },
            text = {
                Column {
                    Text("PERINGATAN: Seluruh data produk akan dihapus permanen!\nMasukkan Password Admin untuk konfirmasi:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = adminPasswordText,
                        onValueChange = {
                            adminPasswordText = it
                            passwordErrorMsg = null
                        },
                        label = { Text("Password Admin") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (passwordErrorMsg != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(passwordErrorMsg!!, color = Color.Red, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                Button(
                    enabled = adminPasswordText.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    onClick = {
                        viewModel.deleteAllProducts(adminPasswordText) { success, msg ->
                            if (success) {
                                showDeleteAllDialog = false
                            } else {
                                passwordErrorMsg = msg ?: "Password Admin salah!"
                            }
                        }
                    }
                ) {
                    Text("HAPUS SEMUA")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text("BATAL")
                }
            }
        )
    }
}

@Composable
fun ProductRowCard(
    product: ProductWithCategory,
    onToggleStatus: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (product.prdImage != null && product.prdImage.isNotEmpty()) {
                    AsyncImage(
                        model = product.prdImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.Fastfood, contentDescription = null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.prdName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(
                    text = "[${product.prdSku}]  Jual: ${Formatters.formatRupiah(product.prdSellingPrice)}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Kat: ${product.catName} (${product.catSubname})",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Switch(
                checked = product.prdIsActive == 1,
                onCheckedChange = { onToggleStatus() }
            )

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormDialog(
    product: ProductWithCategory?,
    categories: List<CategoryEntity>,
    onDismiss: () -> Unit,
    onSave: (catId: Int, customSku: String?, name: String, cost: Double, sell: Double, image: String?) -> Unit
) {
    var selectedCatId by remember { mutableStateOf(product?.prdCategoryId ?: categories.firstOrNull()?.catId ?: 0) }
    var skuText by remember { mutableStateOf(product?.prdSku ?: "") }
    var nameText by remember { mutableStateOf(product?.prdName ?: "") }
    var costText by remember { mutableStateOf(product?.prdCostPrice?.toInt()?.toString() ?: "") }
    var sellText by remember { mutableStateOf(product?.prdSellingPrice?.toInt()?.toString() ?: "") }
    var imageText by remember { mutableStateOf(product?.prdImage ?: "") }
    var expandedCat by remember { mutableStateOf(false) }
    var expandedAssets by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            imageText = uri.toString()
        }
    }

    val builtInAssets = listOf(
        Pair("-- Pilih Gambar Offline (Assets) --", ""),
        Pair("Soto Banjar (Soto_Banjar.jpg)", "file:///android_asset/img/Soto_Banjar.jpg"),
        Pair("Nasi Sop Banjar (nasi_sop_Banjar.jpg)", "file:///android_asset/img/nasi_sop_Banjar.jpg"),
        Pair("Rawon Banjar (rawon_banjar.jpg)", "file:///android_asset/img/rawon_banjar.jpg"),
        Pair("Sate Banjar (sate_banjar.jpg)", "file:///android_asset/img/sate_banjar.jpg"),
        Pair("Es Teh (es_teh.jpeg)", "file:///android_asset/img/es_teh.jpeg"),
        Pair("Es Jeruk (es_jeruk.jpeg)", "file:///android_asset/img/es_jeruk.jpeg"),
        Pair("Air Es (air_es.jpeg)", "file:///android_asset/img/air_es.jpeg"),
        Pair("Air Putih (air_putih.jpeg)", "file:///android_asset/img/air_putih.jpeg"),
        Pair("Le Minerale (leminerale_600ml.jpeg)", "file:///android_asset/img/leminerale_600ml.jpeg"),
        Pair("Prof 600ml (prof_600ml.jpeg)", "file:///android_asset/img/prof_600ml.jpeg"),
        Pair("Sirup (sirup.jpeg)", "file:///android_asset/img/sirup.jpeg"),
        Pair("Kerupuk Udang (kerupuk_udang.jpeg)", "file:///android_asset/img/kerupuk_udang.jpeg"),
        Pair("Kacang Putih (kacang_putih.jpeg)", "file:///android_asset/img/kacang_putih.jpeg")
    )

    val selectedCat = categories.find { it.catId == selectedCatId }
    val currentAssetLabel = builtInAssets.find { it.second == imageText }?.first ?: "Gambar Kustom / Dari Galeri"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Tambah Produk" else "Edit Produk") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Image Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageText.isNotBlank()) {
                        AsyncImage(
                            model = imageText,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.Gray)
                            Text("Belum ada gambar", fontSize = 11.sp, color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons for Image
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pilih Galeri", fontSize = 12.sp)
                    }

                    if (imageText.isNotBlank()) {
                        OutlinedButton(
                            onClick = { imageText = "" },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("Hapus", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Built-in Assets Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedAssets,
                    onExpandedChange = { expandedAssets = !expandedAssets }
                ) {
                    OutlinedTextField(
                        value = currentAssetLabel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Pilih Gambar Bawaan (Offline Assets)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedAssets) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedAssets,
                        onDismissRequest = { expandedAssets = false }
                    ) {
                        builtInAssets.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item.first, fontSize = 13.sp) },
                                onClick = {
                                    if (item.second.isNotBlank()) {
                                        imageText = item.second
                                    }
                                    expandedAssets = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedCat,
                    onExpandedChange = { expandedCat = !expandedCat }
                ) {
                    OutlinedTextField(
                        value = if (selectedCat != null) "${selectedCat.catName} - ${selectedCat.catSubname}" else "Pilih Kategori",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Kategori") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCat,
                        onDismissRequest = { expandedCat = false }
                    ) {
                        categories.forEach { c ->
                            DropdownMenuItem(
                                text = { Text("${c.catName} - ${c.catSubname}") },
                                onClick = {
                                    selectedCatId = c.catId
                                    expandedCat = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (product == null) {
                    OutlinedTextField(
                        value = skuText,
                        onValueChange = { skuText = it },
                        label = { Text("SKU / Barcode (Kosongkan jika otomatis)") },
                        placeholder = { Text("Otomatis jika kosong...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("Nama Produk") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = costText,
                    onValueChange = { costText = it.filter { c -> c.isDigit() } },
                    label = { Text("HPP / Modal (Rp)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = sellText,
                    onValueChange = { sellText = it.filter { c -> c.isDigit() } },
                    label = { Text("Harga Jual (Rp)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                enabled = selectedCatId > 0 && nameText.isNotBlank(),
                onClick = {
                    onSave(
                        selectedCatId,
                        skuText.ifBlank { null },
                        nameText,
                        costText.toDoubleOrNull() ?: 0.0,
                        sellText.toDoubleOrNull() ?: 0.0,
                        imageText.ifBlank { null }
                    )
                }
            ) {
                Text("SIMPAN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("BATAL") }
        }
    )
}
