package com.pos.pik.ui.pos

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.pos.pik.data.local.CartItemWithProduct
import com.pos.pik.data.local.ProductWithCategory
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.data.repository.PosRepository
import com.pos.pik.ui.theme.SuccessGreen
import com.pos.pik.util.Formatters
import com.pos.pik.util.PrintHelper
import kotlinx.coroutines.launch
import android.graphics.Paint
import android.widget.TextView
import androidx.compose.ui.viewinterop.AndroidView



@Composable
fun PosScreen(
    user: UserWithRole,
    viewModel: PosViewModel,
    repository: PosRepository
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(user.usrId) {
        viewModel.loadData(user.usrId)
    }

    val mainCategories by viewModel.mainCategories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()

    val total = cartItems.sumOf { it.cartSubtotal }

    var showPaymentDialog by remember { mutableStateOf(false) }
    var lastInvoiceNumber by remember { mutableStateOf<String?>(null) }
    var lastChangeAmount by remember { mutableDoubleStateOf(0.0) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var cartItemForManualQty by remember { mutableStateOf<CartItemWithProduct?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category Filter & Search Bar
        Surface(
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { viewModel.selectCategory(null) },
                            label = { Text("Semua", fontSize = 11.sp) }
                        )
                    }
                    items(mainCategories) { catName ->
                        FilterChip(
                            selected = selectedCategory == catName,
                            onClick = { viewModel.selectCategory(catName) },
                            label = { Text(catName, fontSize = 11.sp) }
                        )
                    }
                }


                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    placeholder = { Text("Cari produk...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                    // Hapus .height(46.dp) agar tingginya menyesuaikan secara alami tanpa terpotong
                )
            }
        }

        Row(modifier = Modifier.weight(1f)) {
            // Products Grid
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                modifier = Modifier
                    .weight(1.3f)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products, key = { it.prdSku }) { p ->
                    ProductCard(product = p, onClick = { viewModel.addToCart(user.usrId, p) })
                }
            }

            VerticalDivider()

            // Cart Side Panel
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                Text(
                    text = "Keranjang",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp)
                )

                HorizontalDivider()

                if (cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Kosong", color = Color.Gray, fontSize = 13.sp)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp)
                    ) {
                        cartItems.forEach { item ->
                            CartRowItem(
                                item = item,
                                onIncrease = { viewModel.updateQty(user.usrId, item.cartId, item.cartQty + 1) },
                                onDecrease = {
                                    if (item.cartQty > 1) viewModel.updateQty(user.usrId, item.cartId, item.cartQty - 1)
                                },
                                onDelete = { viewModel.removeFromCart(user.usrId, item.cartId) },
                                onManualQtyClick = { cartItemForManualQty = item }
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }

                // Checkout Footer
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total", fontSize = 13.sp)
                            Text(
                                text = Formatters.formatRupiah(total),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { showPaymentDialog = true },
                            enabled = total > 0,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("BAYAR", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    // Payment Dialog
    if (showPaymentDialog) {
        PaymentModalDialog(
            total = total,
            onDismiss = { showPaymentDialog = false },
            onConfirm = { paidAmount, change ->
                coroutineScope.launch {
                    val invoice = repository.processPayment(
                        userId = user.usrId,
                        subtotal = total,
                        paidAmount = paidAmount,
                        changeAmount = change,
                        cartItems = cartItems
                    )
                    showPaymentDialog = false
                    lastInvoiceNumber = invoice
                    lastChangeAmount = change
                    showSuccessDialog = true
                }
            }
        )
    }

    // Manual Qty Dialog
    cartItemForManualQty?.let { item ->
        ManualQtyDialog(
            cartItem = item,
            onDismiss = { cartItemForManualQty = null },
            onSave = { newQty ->
                viewModel.updateQty(user.usrId, item.cartId, newQty)
                cartItemForManualQty = null
            }
        )
    }

    // Success & Print Dialog
    if (showSuccessDialog && lastInvoiceNumber != null) {
        val invoice = lastInvoiceNumber!!
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = { Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(48.dp)) },
            title = { Text("Transaksi Berhasil!") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (lastChangeAmount > 0) {
                        Text("Kembalian: ${Formatters.formatRupiah(lastChangeAmount)}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ActionIconButton(Icons.Default.Print, "CETAK", MaterialTheme.colorScheme.primary) {
                            coroutineScope.launch {
                                val sale = repository.getSaleByInvoice(invoice)
                                val items = repository.getSaleItemsByInvoice(invoice)
                                val settings = repository.getSettingsSync()
                                if (sale != null) {
                                    PrintHelper.printDirect(context, sale, items, settings)
                                }
                            }
                        }
                        ActionIconButton(Icons.Default.Visibility, "LIHAT", Color(0xFFFF9800)) {
                            coroutineScope.launch {
                                val sale = repository.getSaleByInvoice(invoice)
                                val items = repository.getSaleItemsByInvoice(invoice)
                                val settings = repository.getSettingsSync()
                                if (sale != null) {
                                    PrintHelper.printDirect(context, sale, items, settings)
                                }
                            }
                        }
                        ActionIconButton(Icons.Default.Share, "SHARE", SuccessGreen) {
                            coroutineScope.launch {
                                val sale = repository.getSaleByInvoice(invoice)
                                val items = repository.getSaleItemsByInvoice(invoice)
                                val settings = repository.getSettingsSync()
                                if (sale != null) {
                                    PrintHelper.shareReceipt(context, sale, items, settings)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("TUTUP")
                }
            }
        )
    }
}

val PriceGreen = Color(0xFF00E676) // Warna Hijau Cerah


@Composable
fun ProductCard(product: ProductWithCategory, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Gambar Produk
            if (!product.prdImage.isNullOrEmpty()) {
                AsyncImage(
                    model = product.prdImage,
                    contentDescription = product.prdName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Fastfood, contentDescription = null, tint = Color.Gray)
                }
            }

            // 2. Konten Teks di Bagian Bawah
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(start = 6.dp, end = 6.dp, bottom = 4.dp)
            ) {
                // BARIS 1: NAMA PRODUK
                NativeOutlinedText(
                    text = product.prdName,
                    textSizeSp = 12f,
                    textColor = android.graphics.Color.WHITE,
                    outlineColor = android.graphics.Color.BLACK,
                    strokeWidthPx = 2f, // UBAH TEBAL OUTLINE NAMA DI SINI
                    isBold = false
                )

                Spacer(modifier = Modifier.height(1.dp))

                // BARIS 2: SKU (KIRI) & HARGA (KANAN)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // SKU
                    NativeOutlinedText(
                        text = "[${product.prdSku}]",
                        textSizeSp = 9.5f,
                        textColor = android.graphics.Color.WHITE,
                        outlineColor = android.graphics.Color.BLACK,
                        strokeWidthPx = 1f, // UBAH TEBAL OUTLINE SKU DI SINI
                        isBold = false
                    )

                    // HARGA (HIJAU CERAH)
                    NativeOutlinedText(
                        text = Formatters.formatRupiah(product.prdSellingPrice),
                        textSizeSp = 11.5f,
                        textColor = android.graphics.Color.parseColor("#00E676"), // Warna Hijau
                        outlineColor = android.graphics.Color.BLACK,
                        strokeWidthPx = 1f, // UBAH TEBAL OUTLINE HARGA DI SINI
                        isBold = true
                    )
                }
            }
        }
    }
}

// Komponen Pembantu Menggunakan TextView Bawaan Android
@Composable
fun NativeOutlinedText(
    text: String,
    textSizeSp: Float,
    textColor: Int,
    outlineColor: Int,
    strokeWidthPx: Float = 4f,
    isBold: Boolean = false
) {
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                setSingleLine(true)
                ellipsize = android.text.TextUtils.TruncateAt.END
            }
        },
        update = { textView ->
            textView.text = text
            textView.textSize = textSizeSp
            textView.setTextColor(textColor)
            textView.paint.apply {
                style = Paint.Style.FILL_AND_STROKE
                strokeWidth = strokeWidthPx // Mengatur ketebalan outline bawaan Android
                setShadowLayer(strokeWidthPx, 0f, 0f, outlineColor) // Mengatur warna outline
                isFakeBoldText = isBold
            }
        }
    )
}

@Composable
fun CartRowItem(
    item: CartItemWithProduct,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit,
    onManualQtyClick: () -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.prdName,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 2,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Hapus", tint = Color.Red, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease, enabled = item.cartQty > 1, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.RemoveCircleOutline,
                        contentDescription = "Kurang",
                        tint = if (item.cartQty > 1) Color(0xFFFF9800) else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color.LightGray.copy(alpha = 0.2f),
                    modifier = Modifier
                        .clickable(onClick = onManualQtyClick)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${item.cartQty}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onIncrease, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.AddCircleOutline,
                        contentDescription = "Tambah",
                        tint = SuccessGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Text(
                text = Formatters.formatRupiah(item.cartSubtotal),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun PaymentModalDialog(
    total: Double,
    onDismiss: () -> Unit,
    onConfirm: (paidAmount: Double, change: Double) -> Unit
) {
    var rawDigits by remember { mutableStateOf("") }
    val paidAmount = rawDigits.toDoubleOrNull() ?: 0.0
    val change = paidAmount - total
    val isEnough = paidAmount >= total

    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = ""))
    }

    LaunchedEffect(rawDigits) {
        val num = rawDigits.toLongOrNull()
        val formatted = if (num != null) Formatters.formatNumber(num) else ""
        textFieldValue = TextFieldValue(
            text = formatted,
            selection = TextRange(formatted.length)
        )
    }

    AlertDialog(
        onDismissRequest = {},
        properties = androidx.compose.ui.window.DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = { Text("Proses Pembayaran") },
        text = {
            Column {
                Text(
                    text = "Total: ${Formatters.formatRupiah(total)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = textFieldValue,
                    onValueChange = { newValue ->
                        val clean = newValue.text.filter { c -> c.isDigit() }
                        rawDigits = clean
                    },
                    label = { Text("Uang Dibayar (Rp)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        if (isEnough && paidAmount > 0) {
                            onConfirm(paidAmount, change)
                        }
                    }),
                    trailingIcon = {
                        TextButton(onClick = {
                            rawDigits = total.toLong().toString()
                        }) {
                            Text("Uang Pas", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (!isEnough && paidAmount > 0) {
                    Text(
                        text = "Kurang: ${Formatters.formatRupiah(total - paidAmount)}",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
                if (isEnough && paidAmount > 0) {
                    Text(
                        text = "Kembalian: ${Formatters.formatRupiah(change)}",
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = isEnough && paidAmount > 0,
                onClick = { onConfirm(paidAmount, change) }
            ) {
                Text("KONFIRMASI")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("BATAL")
            }
        }
    )
}

@Composable
fun ManualQtyDialog(
    cartItem: CartItemWithProduct,
    onDismiss: () -> Unit,
    onSave: (Int) -> Unit
) {
    var qtyText by remember { mutableStateOf(cartItem.cartQty.toString()) }

    AlertDialog(
        onDismissRequest = {},
        properties = androidx.compose.ui.window.DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = { Text("Set Qty: ${cartItem.prdName}") },
        text = {
            OutlinedTextField(
                value = qtyText,
                onValueChange = { qtyText = it.filter { c -> c.isDigit() } },
                label = { Text("Kuantitas") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    val valInt = qtyText.toIntOrNull()
                    if (valInt != null && valInt > 0) {
                        onSave(valInt)
                    }
                }),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val valInt = qtyText.toIntOrNull()
                    if (valInt != null && valInt > 0) {
                        onSave(valInt)
                    }
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

@Composable
fun ActionIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.background(color.copy(alpha = 0.1f), shape = CircleShape)
        ) {
            Icon(icon, contentDescription = label, tint = color)
        }
        Text(text = label, fontSize = 10.sp, color = color, fontWeight = FontWeight.Bold)
    }
}
