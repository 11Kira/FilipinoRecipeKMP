package com.kira.kmp.features.account.profile

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.kmp.model.User
import com.kira.kmp.utils.ColorUtils
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.ic_account
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel(),
    onShowSnackbar: (String) -> Unit,
    onLogoutNavigate: () -> Unit
) {
    val uiState by viewModel.profileUiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = null) {
        viewModel.getUserProfile()
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            onShowSnackbar(it)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        uiState.profile?.let { profile ->
            PopulateProfileScreen(
                viewModel = viewModel,
                userProfile = profile,
                onLogoutNavigate = onLogoutNavigate
            )
        }

        if (uiState.isLoading && uiState.profile == null) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun PopulateProfileScreen(
    viewModel: ProfileViewModel,
    userProfile: User,
    onLogoutNavigate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = ColorUtils().recipeListBackgroundGradient)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_account),
                contentDescription = "Profile",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            BasicTextField(
                value = userProfile.username,
                enabled = false,
                onValueChange = {},
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
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            tint = Color.Gray,
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            BasicTextField(
                value = userProfile.email,
                enabled = false,
                onValueChange = {},
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
                            innerTextField()
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.logout(
                        onLogout = onLogoutNavigate
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7B5DB0),
                    contentColor = Color.White
                )
            ) {
                Text("Log out")
            }
        }
    }
}
