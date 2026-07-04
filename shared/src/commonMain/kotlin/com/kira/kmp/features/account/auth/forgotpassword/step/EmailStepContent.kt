package com.kira.kmp.features.account.auth.forgotpassword.step

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
fun EmailStepContent(
    onNext: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Forgot Password",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your registered email address to get a 6-digit verification code.",
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.height(24.dp))
        BasicTextField(
            value = email,
            onValueChange = {
                email = it
                viewModel.updateEmail(it)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onNext(email.trim())
                }
            ),
            modifier = Modifier.height(50.dp),
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
                            Text("Email address", color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onNext(email.trim()) },
            enabled = viewModel.isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7B5DB0),
                contentColor = Color.White
            )
        ) {
            Text("Send Code")
        }
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back to Login")
        }
    }
}