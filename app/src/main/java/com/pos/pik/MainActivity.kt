package com.pos.pik

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pos.pik.ui.login.LoginScreen
import com.pos.pik.ui.login.LoginViewModel
import com.pos.pik.ui.main.MainScreen
import com.pos.pik.ui.main.MainViewModel
import com.pos.pik.ui.theme.POSPIKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as PosApplication
        val repository = app.repository

        setContent {
            POSPIKTheme {
                val mainViewModel: MainViewModel = viewModel()
                val currentUser by mainViewModel.currentUser.collectAsState()

                if (currentUser == null) {
                    val loginViewModel: LoginViewModel = viewModel(
                        factory = LoginViewModel.Factory(repository)
                    )
                    LoginScreen(
                        viewModel = loginViewModel,
                        onLoginSuccess = { user ->
                            mainViewModel.setCurrentUser(user)
                        }
                    )
                } else {
                    MainScreen(
                        user = currentUser!!,
                        repository = repository,
                        onLogout = {
                            mainViewModel.setCurrentUser(null)
                        }
                    )
                }
            }
        }
    }
}
