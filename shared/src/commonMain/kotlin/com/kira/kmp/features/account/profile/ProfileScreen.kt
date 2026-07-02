package com.kira.kmp.features.account.profile

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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.kmp.model.User
import com.kira.kmp.model.enums.Role
import com.kira.kmp.ui.component.StaticBasicTextFieldWithIcon
import com.kira.kmp.utils.ColorUtils
import filipinorecipekmp.shared.generated.resources.Res
import filipinorecipekmp.shared.generated.resources.logo
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    onShowSnackbar: (String) -> Unit,
    onLogoutNavigate: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
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
                StaticBasicTextFieldWithIcon(
                    value = userProfile.username,
                    imageVector = Icons.Filled.Person
                )

                Spacer(modifier = Modifier.height(12.dp))

                StaticBasicTextFieldWithIcon(
                    value = userProfile.email,
                    imageVector = Icons.Filled.Email
                )

                if (userProfile.role == Role.ADMIN.name) {
                    Spacer(modifier = Modifier.height(12.dp))

                    StaticBasicTextFieldWithIcon(
                        value = userProfile.role,
                        imageVector = Icons.Filled.AdminPanelSettings
                    )
                }

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
}
