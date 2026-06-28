package com.kira.kmp.features.account.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.kira.kmp.ui.component.CircularIconButton
import com.kira.kmp.ui.navigation.LoginRoute
import com.kira.kmp.ui.navigation.RegisterRoute
import com.kira.kmp.utils.ColorUtils
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    onShowSnackbar: (String) -> Unit,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.registerState.collect { state ->
                when (state) {
                    is RegisterState.OnRegister -> {
                        onShowSnackbar("Account created! Please sign in.")
                        navController.navigate(LoginRoute) {
                            popUpTo(RegisterRoute) { inclusive = true }
                        }
                    }

                    is RegisterState.ShowError -> {
                        onShowSnackbar(state.error.message ?: "Registration failed")
                    }
                }
            }
        }
    }
    val isLoading by viewModel.isLoading.collectAsState()
    PopulateRegisterScreen(
        viewModel = viewModel,
        navController = navController,
        isLoading = isLoading,
        onRegisterClick = { email, password, username ->
            viewModel.register(
                email,
                password,
                username
            )
        }
    )
}

@Composable
fun PopulateRegisterScreen(
    viewModel: RegisterViewModel,
    isLoading: Boolean,
    navController: NavController,
    onRegisterClick: (String, String, String) -> Unit
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val passwordState = rememberTextFieldState()
    val confirmPasswordState = rememberTextFieldState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val passwordsMatch =
        viewModel.password == viewModel.confirmPassword || viewModel.confirmPassword.isEmpty()

    LaunchedEffect(username, email) {
        viewModel.username = username
        viewModel.email = email
    }

    LaunchedEffect(passwordState.text, confirmPasswordState.text) {
        viewModel.password = passwordState.text.toString()
        viewModel.confirmPassword = confirmPasswordState.text.toString()
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
            Box(
                modifier = Modifier.fillMaxWidth().wrapContentSize()
            ) {
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
                        .size(350.dp)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BasicTextField(
                    value = username,
                    onValueChange = { username = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = Color.Gray,
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (username.isEmpty()) {
                                    Text("Username", color = Color.Gray)
                                }
                                innerTextField()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                    textObfuscationMode = if (isPasswordVisible) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.RevealLastTyped
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
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
                                .padding(horizontal = 16.dp),
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

                            IconButton(
                                onClick = { isPasswordVisible = !isPasswordVisible },
                                modifier = Modifier.focusable(false)
                            ) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                BasicSecureTextField(
                    state = confirmPasswordState,
                    textObfuscationMode = if (isConfirmPasswordVisible) {
                        TextObfuscationMode.Visible
                    } else {
                        TextObfuscationMode.RevealLastTyped
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = {
                        keyboardController?.hide()
                        onRegisterClick(
                            email,
                            passwordState.text.toString(),
                            username
                        )
                    },
                    modifier = Modifier.height(50.dp),
                    decorator = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(24.dp))
                                .border(
                                    width = 1.dp,
                                    color = if (passwordsMatch) Color.Gray.copy(alpha = 0.5f) else Color.Red,
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = null,
                                tint = Color.Gray,
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(modifier = Modifier.weight(1f)) {
                                if (confirmPasswordState.text.isEmpty()) {
                                    Text("Confirm Password", color = Color.Gray)
                                }
                                innerTextField()
                            }

                            IconButton(
                                onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible },
                                modifier = Modifier.focusable(false)
                            ) {
                                Icon(
                                    imageVector = if (isConfirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (isConfirmPasswordVisible) "Hide password" else "Show password",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                )

                AnimatedVisibility(visible = !passwordsMatch) {
                    Text(
                        text = "Passwords do not match",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        keyboardController?.hide()
                        onRegisterClick(email, passwordState.text.toString(), username)
                    },
                    enabled = !isLoading && viewModel.isInputValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B5DB0),
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    )
                ) {
                    Text("Register")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Sign in",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B5DB0),
                        modifier = Modifier
                            .clickable(onClick = {
                                navController.navigate(LoginRoute) {
                                    popUpTo(RegisterRoute) { inclusive = true }
                                }
                            })
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
