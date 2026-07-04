package com.kira.kmp.features.account.auth.forgotpassword.step

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kira.kmp.features.account.auth.forgotpassword.ForgotPasswordViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewPasswordStepContent(
    onReset: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {
    val passwordState = rememberTextFieldState()
    val confirmPasswordState = rememberTextFieldState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val passwordsMatch =
        viewModel.password == viewModel.confirmPassword || viewModel.confirmPassword.isEmpty()

    LaunchedEffect(passwordState.text, confirmPasswordState.text) {
        viewModel.password = passwordState.text.toString()
        viewModel.confirmPassword = confirmPasswordState.text.toString()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Choose a strong new password for your account.",
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
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
                        imageVector = Icons.Filled.Key,
                        contentDescription = null,
                        tint = Color.Gray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (passwordState.text.isEmpty()) {
                            Text(
                                text = "New Password",
                                color = Color.Gray
                            )
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
                if (passwordsMatch) onReset(passwordState.text.toString())
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
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Key,
                        contentDescription = null,
                        tint = Color.Gray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (confirmPasswordState.text.isEmpty()) {
                            Text(
                                text = "Confirm New Password",
                                color = Color.Gray
                            )
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
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { if (passwordsMatch) onReset(passwordState.text.toString()) },
            enabled = viewModel.isPasswordValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B5DB0),
                contentColor = Color.White
            )
        ) {
            Text("Update Password")
        }
    }
}