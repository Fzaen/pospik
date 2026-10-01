package com.pos.pik.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pos.pik.R
import com.pos.pik.data.local.UserWithRole

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (UserWithRole, AppMode) -> Unit
) {
    val username by viewModel.usernameState.collectAsState()
    val password by viewModel.passwordState.collectAsState()
    val selectedMode by viewModel.selectedModeState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val settings by viewModel.settings.collectAsState()

    val warungName = settings?.setWarungName?.takeIf { it.isNotBlank() } ?: "POS PIK"
    val focusManager = LocalFocusManager.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUiState.Success -> {
                onLoginSuccess(state.user, state.mode)
                viewModel.resetState()
            }
            is LoginUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .widthIn(max = 400.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pospik),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = warungName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Sistem Kasir Mobile Sederhana",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = username,
                        onValueChange = { viewModel.usernameState.value = it },
                        label = { Text("Username") },
                        leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel.passwordState.value = it },
                        label = { Text("Password") },
                        leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            viewModel.login()
                        }),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Pilih Modul Akses:", fontSize = 11.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedMode == AppMode.POS,
                            onClick = { viewModel.selectedModeState.value = AppMode.POS },
                            label = { Text("POS (Kasir)", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = selectedMode == AppMode.INVENTORY,
                            onClick = { viewModel.selectedModeState.value = AppMode.INVENTORY },
                            label = { Text("Gudang (Inventory)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.login() },
                        enabled = uiState !is LoginUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState is LoginUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "MASUK",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    var showHelpDialog by remember { mutableStateOf(false) }

                    TextButton(onClick = { showHelpDialog = true }) {
                        Text("📖 Petunjuk Penggunaan (Panduan)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    if (showHelpDialog) {
                        AlertDialog(
                            onDismissRequest = { showHelpDialog = false },
                            title = { Text("Panduan Pengguna Baru") },
                            text = {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Ikuti 5 langkah operasional berikut untuk mulai menggunakan aplikasi:", fontSize = 12.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("1. Login ke Modul Gudang (Inventory).", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("2. Daftarkan Kategori produk terlebih dahulu.", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("3. Daftarkan Menu / Produk barang.", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("4. Login ke Modul POS (Kasir).", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("5. Belajar Membuat Penjualan (Checkout & cetak struk).", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            },
                            confirmButton = {
                                Button(onClick = { showHelpDialog = false }) {
                                    Text("MENGERTI")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
