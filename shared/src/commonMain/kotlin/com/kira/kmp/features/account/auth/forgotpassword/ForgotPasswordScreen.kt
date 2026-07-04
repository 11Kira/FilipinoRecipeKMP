package com.kira.kmp.features.account.auth.forgotpassword

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kira.kmp.features.account.auth.forgotpassword.step.EmailStepContent
import com.kira.kmp.features.account.auth.forgotpassword.step.NewPasswordStepContent
import com.kira.kmp.features.account.auth.forgotpassword.step.OtpStepContent
import com.kira.kmp.features.account.auth.forgotpassword.step.SuccessStepContent
import com.kira.kmp.model.request.ResetPasswordRequest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    onShowSnackbar: (String) -> Unit,
    onNavigateBackToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.forgotPasswordState.collect { state ->
            when (state) {
                is ForgotPasswordState.ShowError -> {
                    onShowSnackbar(
                        state.error.message ?: "An unexpected error occurred"
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val step = currentStep) {
                is ForgotPasswordStep.EnterEmail -> {
                    EmailStepContent(
                        onNext = { email -> viewModel.requestOtp(email) },
                        onBack = onNavigateBackToLogin
                    )
                }

                is ForgotPasswordStep.EnterOtp -> {
                    OtpStepContent(
                        email = step.email,
                        onVerify = { otp -> viewModel.verifyOtp(step.email, otp) },
                        onBack = onNavigateBackToLogin
                    )
                }

                is ForgotPasswordStep.CreateNewPassword -> {
                    NewPasswordStepContent(
                        onReset = { newPassword ->
                            viewModel.completeReset(
                                ResetPasswordRequest(
                                    email = step.email,
                                    resetToken = step.token,
                                    newPassword = newPassword
                                )
                            )
                        }
                    )
                }

                is ForgotPasswordStep.Success -> {
                    SuccessStepContent(onDone = onNavigateBackToLogin)
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}