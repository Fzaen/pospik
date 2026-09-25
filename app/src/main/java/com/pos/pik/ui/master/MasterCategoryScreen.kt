package com.pos.pik.ui.master

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pos.pik.data.local.CategoryEntity

@Composable
fun MasterCategoryScreen(viewModel: MasterCategoryViewModel) {
    val context = LocalContext.current
    val categories by viewModel.categories.collectAsState()

    var showFormDialog by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<CategoryEntity?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingCategory = null
                    showFormDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Kategori")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories, key = { it.catId }) { cat ->
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = cat.catName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "Sub: ${cat.catSubname}", fontSize = 12.sp, color = Color.Gray)
                        }

                        IconButton(onClick = {
                            editingCategory = cat
                            showFormDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                        }

                        IconButton(onClick = {
                            viewModel.deleteCategory(cat.catId) { success ->
                                if (!success) {
                                    Toast.makeText(context, "Gagal: Kategori masih digunakan oleh produk!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }

    if (showFormDialog) {
        CategoryFormDialog(
            category = editingCategory,
            onDismiss = { showFormDialog = false },
            onSave = { name, subname ->
                viewModel.saveCategory(editingCategory, name, subname)
                showFormDialog = false
            }
        )
    }
}

@Composable
fun CategoryFormDialog(
    category: CategoryEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, subname: String) -> Unit
) {
    var nameText by remember { mutableStateOf(category?.catName ?: "") }
    var subnameText by remember { mutableStateOf(category?.catSubname ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Tambah Kategori" else "Edit Kategori") },
        text = {
            Column {
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("Nama Kategori (cth: Makanan)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = subnameText,
                    onValueChange = { subnameText = it },
                    label = { Text("Sub-Kategori (cth: Soto & Sop)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                enabled = nameText.isNotBlank() && subnameText.isNotBlank(),
                onClick = { onSave(nameText, subnameText) }
            ) {
                Text("SIMPAN")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("BATAL") }
        }
    )
}
