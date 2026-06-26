package com.kira.kmp.features.account.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.kira.kmp.ui.component.CircularIconButton
import com.kira.kmp.ui.navigation.LoginRoute
import com.kira.kmp.ui.navigation.RecipeListRoute
import com.kira.kmp.ui.navigation.RegisterRoute
import com.kira.kmp.utils.ColorUtils
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    navController: NavController,
    onShowSnackbar: (String) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.OnLogin -> {
                        navController.navigate(RecipeListRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }

                    is LoginState.ShowError -> {
                        onShowSnackbar(state.error.message ?: "Login failed")
                    }
                }
            }
        }
    }
    val isLoading by viewModel.isLoading.collectAsState()
    PopulateLoginScreen(
        viewModel = viewModel,
        navController = navController,
        isLoading = isLoading,
        onLoginClick = { email, password -> viewModel.login(email, password) }
    )
}

@Composable
fun PopulateLoginScreen(
    viewModel: LoginViewModel,
    isLoading: Boolean,
    navController: NavController,
    onLoginClick: (String, String) -> Unit,
) {
    var email by remember { mutableStateOf("") }
    val passwordState = rememberTextFieldState()
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(passwordState.text) {
        snapshotFlow { passwordState.text }.collect {
            viewModel.updatePassword(it.toString())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = ColorUtils().recipeListBackgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CircularIconButton(
                    icon = Icons.Default.ArrowBack,
                    onClick = { navController.popBackStack() })
            }
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Project drawable image",
                modifier = Modifier
                    .size(350.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BasicTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        viewModel.updateEmail(it)
                    },
                    modifier = Modifier
                        .height(50.dp),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(24.dp))
                                .border(
                                    1.dp,
                                    Color.Gray.copy(alpha = 0.5f),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Email,
                                contentDescription = null,
                                tint = Color.Gray,
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (email.isEmpty()) {
                                    Text("Email", color = Color.Gray)
                                }
                                innerTextField()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                BasicSecureTextField(
                    state = passwordState,
                    textObfuscationMode = if (isVisible) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.RevealLastTyped
                    },
                    modifier = Modifier.height(50.dp),
                    decorator = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(24.dp))
                                .border(
                                    1.dp,
                                    Color.Gray.copy(alpha = 0.5f),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(24.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color.Gray,
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (passwordState.text.isEmpty()) {
                                    Text("Password", color = Color.Gray)
                                }
                                innerTextField()
                            }

                            IconButton(onClick = { isVisible = !isVisible }) {
                                Icon(
                                    imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isVisible) "Hide password" else "Show password",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text(
                        text = "Forgot Password?",
                        color = Color(0xFF7B5DB0),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { /* Navigate to a ForgotPasswordRoute later */ }
                    )
                }

                Button(
                    onClick = {
                        onLoginClick(email, passwordState.text.toString())
                    },
                    enabled = !isLoading && viewModel.isInputValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B5DB0),
                        contentColor = Color.White
                    )
                ) {
                    Text("Login")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don't have an account? ",
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Register",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B5DB0),
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    navController.navigate(RegisterRoute) {
                                        popUpTo(LoginRoute) { inclusive = true }
                                    }
                                }
                            )
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
