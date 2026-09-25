package com.kira.kmp.features.account.auth.forgotpassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.kmp.features.account.auth.forgotpassword.step.EmailStepContent
import com.kira.kmp.features.account.auth.forgotpassword.step.NewPasswordStepContent
import com.kira.kmp.features.account.auth.forgotpassword.step.OtpStepContent
import com.kira.kmp.features.account.auth.forgotpassword.step.SuccessStepContent
import com.kira.kmp.ui.component.CircularIconButton
import com.kira.kmp.utils.ColorUtils
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    onShowSnackbar: (String) -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: ForgotPasswordViewModel = koinViewModel()
) {
    val currentStep by viewModel.currentStep.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = uiState.isLoading

    LaunchedEffect(Unit) {
        viewModel.forgotPasswordEffect.collect { state ->
            when (state) {
                is ForgotPasswordUiEffect.ShowSnackbar -> {
                    onShowSnackbar(
                        state.message
                    )
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            onClick = onBackToLogin
                        )
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

                when (val step = currentStep) {
                    is ForgotPasswordStep.EnterEmail -> {
                        EmailStepContent(
                            isLoading = isLoading,
                            onBack = onBackToLogin,
                            viewModel = viewModel
                        )
                    }

                    is ForgotPasswordStep.EnterOtp -> {
                        OtpStepContent(
                            email = step.email,
                            isLoading = isLoading,
                            onBack = onBackToLogin,
                            viewModel = viewModel
                        )
                    }

                    is ForgotPasswordStep.CreateNewPassword -> {
                        NewPasswordStepContent(
                            isLoading = isLoading,
                            onBack = onBackToLogin,
                            viewModel = viewModel
                        )
                    }

                    is ForgotPasswordStep.Success -> {
                        SuccessStepContent(onDone = onBackToLogin)
                    }
                }
            }
        }

    }
}