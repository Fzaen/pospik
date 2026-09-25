package com.pos.pik.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pos.pik.data.local.UserWithRole
import com.pos.pik.data.repository.PosRepository
import com.pos.pik.ui.master.*
import com.pos.pik.ui.pos.PosScreen
import com.pos.pik.ui.pos.PosViewModel
import com.pos.pik.ui.reports.ReportHubScreen
import com.pos.pik.ui.reports.ReportViewModel
import com.pos.pik.ui.settings.SettingsScreen
import com.pos.pik.ui.settings.SettingsViewModel
import com.pos.pik.util.Formatters
import kotlinx.coroutines.delay

data class NavItem(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    user: UserWithRole,
    repository: PosRepository,
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var currentTimeText by remember { mutableStateOf(Formatters.getCurrentTimeFormatted()) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Live Clock Timer
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeText = Formatters.getCurrentTimeFormatted()
            delay(1000)
        }
    }

    val navItems = remember(user.usrRoleId) {
        val list = mutableListOf(
            NavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
            NavItem("POS", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
        )
        if (user.usrRoleId == 1) {
            list.add(NavItem("Produk", Icons.Filled.Inventory2, Icons.Outlined.Inventory2))
            list.add(NavItem("User", Icons.Filled.People, Icons.Outlined.People))
            list.add(NavItem("Report", Icons.Filled.Assessment, Icons.Outlined.Assessment))
            list.add(NavItem("Setting", Icons.Filled.Settings, Icons.Outlined.Settings))
        }
        list
    }

    val currentTabLabel = navItems.getOrNull(selectedTab)?.label ?: "Home"
    val hideBottomBar = currentTabLabel == "POS" || currentTabLabel == "Produk"

    val homeViewModel = remember { HomeViewModel(repository) }
    val posViewModel = remember { PosViewModel(repository) }
    val masterProductViewModel = remember { MasterProductViewModel(repository) }
    val masterCategoryViewModel = remember { MasterCategoryViewModel(repository) }
    val masterUserViewModel = remember { MasterUserViewModel(repository) }
    val reportViewModel = remember { ReportViewModel(repository) }
    val settingsViewModel = remember { SettingsViewModel(repository) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Green, shape = androidx.compose.foundation.shape.CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${user.usrName.uppercase()}  |  $currentTimeText",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.DarkGray
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (selectedTab == 0) showLogoutDialog = true
                            else selectedTab = 0
                        }
                    ) {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Default.Logout else Icons.Default.Close,
                            contentDescription = "Action",
                            tint = if (selectedTab == 0) Color.Red else Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (!hideBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    navItems.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            label = { Text(item.label, fontSize = 10.sp) },
                            icon = {
                                Icon(
                                    imageVector = if (selectedTab == index) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTabLabel) {
                "Home" -> HomeScreen(user = user, viewModel = homeViewModel, onNavigateTab = { selectedTab = it })
                "POS" -> PosScreen(user = user, viewModel = posViewModel, repository = repository)
                "Produk" -> MasterProductScreen(productViewModel = masterProductViewModel, categoryViewModel = masterCategoryViewModel)
                "User" -> MasterUserScreen(viewModel = masterUserViewModel)
                "Report" -> ReportHubScreen(viewModel = reportViewModel, repository = repository)
                "Setting" -> SettingsScreen(viewModel = settingsViewModel)
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Keluar Aplikasi?") },
                text = { Text("Anda akan dialihkan ke halaman login.") },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text("KELUAR", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("BATAL")
                    }
                }
            )
        }
    }
}
