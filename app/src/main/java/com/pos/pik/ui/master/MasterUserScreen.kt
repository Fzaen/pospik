package com.pos.pik.ui.master

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pos.pik.data.local.RoleEntity
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.ui.theme.SuccessGreen

@Composable
fun MasterUserScreen(viewModel: MasterUserViewModel) {
    val users by viewModel.users.collectAsState()
    val roles by viewModel.roles.collectAsState()

    var showFormDialog by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<UserWithRole?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editingUser = null
                    showFormDialog = true
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah User")
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
            items(users, key = { it.usrId }) { u ->
                val isActive = u.usrIsActive == 1
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
                        Surface(
                            shape = CircleShape,
                            color = if (isActive) Color(0xFFBBDEFB) else Color.LightGray,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = u.usrName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(text = "${u.rolName} | @${u.usrUsername}", fontSize = 12.sp, color = Color.Gray)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isActive) SuccessGreen.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = if (isActive) "AKTIF" else "NON-AKTIF",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) SuccessGreen else Color.Red,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(onClick = {
                            editingUser = u
                            showFormDialog = true
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }

    if (showFormDialog) {
        UserFormDialog(
            user = editingUser,
            roles = roles,
            onDismiss = { showFormDialog = false },
            onSave = { name, username, pwd, phone, roleId, active ->
                viewModel.saveUser(editingUser, name, username, pwd, phone, roleId, active)
                showFormDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFormDialog(
    user: UserWithRole?,
    roles: List<RoleEntity>,
    onDismiss: () -> Unit,
    onSave: (name: String, username: String, pwd: String, phone: String, roleId: Int, isActive: Int) -> Unit
) {
    var nameText by remember { mutableStateOf(user?.usrName ?: "") }
    var usernameText by remember { mutableStateOf(user?.usrUsername ?: "") }
    var passwordText by remember { mutableStateOf(user?.usrPassword ?: "") }
    var phoneText by remember { mutableStateOf(user?.usrPhone ?: "") }
    var selectedRoleId by remember { mutableIntStateOf(user?.usrRoleId ?: roles.firstOrNull()?.rolId ?: 1) }
    var isActive by remember { mutableStateOf(user?.usrIsActive != 0) }
    var expandedRole by remember { mutableStateOf(false) }

    val selectedRole = roles.find { it.rolId == selectedRoleId }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Tambah User Baru" else "Edit User") },
        text = {
            Column {
                OutlinedTextField(
                    value = nameText,
                    onValueChange = { nameText = it },
                    label = { Text("Nama Lengkap") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = usernameText,
                    onValueChange = { usernameText = it },
                    label = { Text("Username") },
                    enabled = user == null,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = passwordText,
                    onValueChange = { passwordText = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = phoneText,
                    onValueChange = { phoneText = it },
                    label = { Text("No. Telepon") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = expandedRole,
                    onExpandedChange = { expandedRole = !expandedRole }
                ) {
                    OutlinedTextField(
                        value = selectedRole?.rolName ?: "Pilih Role",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role / Hak Akses") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRole) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedRole,
                        onDismissRequest = { expandedRole = false }
                    ) {
                        roles.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r.rolName) },
                                onClick = {
                                    selectedRoleId = r.rolId
                                    expandedRole = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Status Aktif")
                    Switch(
                        checked = isActive,
                        onCheckedChange = { isActive = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = nameText.isNotBlank() && usernameText.isNotBlank() && passwordText.isNotBlank(),
                onClick = {
                    onSave(
                        nameText,
                        usernameText,
                        passwordText,
                        phoneText,
                        selectedRoleId,
                        if (isActive) 1 else 0
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
