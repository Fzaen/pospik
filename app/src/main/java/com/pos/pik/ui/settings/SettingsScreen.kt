package com.pos.pik.ui.settings

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val context = LocalContext.current
    val currentSettings by viewModel.settings.collectAsState()

    var nameText by remember { mutableStateOf("") }
    var addressText by remember { mutableStateOf("") }
    var phoneText by remember { mutableStateOf("") }
    var marginText by remember { mutableStateOf("5.0") }
    var paperSize by remember { mutableIntStateOf(80) }

    var expandedPaperSize by remember { mutableStateOf(false) }

    LaunchedEffect(currentSettings) {
        currentSettings?.let { s ->
            nameText = s.setWarungName
            addressText = s.setAddress
            phoneText = s.setPhone
            marginText = s.setMargin.toString()
            paperSize = s.setPaperSize
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("INFORMASI WARUNG", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text("Nama Warung") },
            leadingIcon = { Icon(Icons.Default.Storefront, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = addressText,
            onValueChange = { addressText = it },
            label = { Text("Alamat") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = phoneText,
            onValueChange = { phoneText = it },
            label = { Text("No. Telp") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("PENGATURAN PRINTER & KERTAS", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedPaperSize,
                onExpandedChange = { expandedPaperSize = !expandedPaperSize },
                modifier = Modifier.weight(1f)
            ) {
                OutlinedTextField(
                    value = "$paperSize mm",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ukuran Kertas") },
                    leadingIcon = { Icon(Icons.Default.Print, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPaperSize) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expandedPaperSize,
                    onDismissRequest = { expandedPaperSize = false }
                ) {
                    DropdownMenuItem(text = { Text("58 mm") }, onClick = { paperSize = 58; expandedPaperSize = false })
                    DropdownMenuItem(text = { Text("80 mm") }, onClick = { paperSize = 80; expandedPaperSize = false })
                }
            }

            OutlinedTextField(
                value = marginText,
                onValueChange = { marginText = it },
                label = { Text("Margin (mm)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("KEAMANAN & CADANGAN DATA", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Fitur Cadangan Database Room Aktif", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("BACKUP")
            }

            OutlinedButton(
                onClick = {
                    Toast.makeText(context, "Fitur Restore Database Room Aktif", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("RESTORE")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.saveSettings(
                    warungName = nameText,
                    address = addressText,
                    phone = phoneText,
                    printer = null,
                    paperSize = paperSize,
                    margin = marginText.toDoubleOrNull() ?: 5.0
                )
                Toast.makeText(context, "Pengaturan berhasil disimpan!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("SIMPAN SEMUA PENGATURAN", fontWeight = FontWeight.Bold)
        }
    }
}
