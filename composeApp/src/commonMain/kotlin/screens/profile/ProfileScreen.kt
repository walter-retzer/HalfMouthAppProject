package screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import components.LoadingWithLine
import components.ProfileToolbar
import data.UserPreferences
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_account
import halfmouthappproject.composeapp.generated.resources.icon_email
import halfmouthappproject.composeapp.generated.resources.icon_equipaments
import halfmouthappproject.composeapp.generated.resources.icon_logout
import halfmouthappproject.composeapp.generated.resources.icon_phone
import halfmouthappproject.composeapp.generated.resources.splashscreenlogo
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import util.ConstantsApp
import util.RestartApp
import util.formattedAsPhone
import util.snackBarOnlyMessage
import util.snackBarWithActionButton
import viewmodel.ProfileViewModel
import viewmodel.ProfileViewState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit
) {

    val viewModel = getViewModel(
        key = "profile-screen",
        factory = viewModelFactory {
            ProfileViewModel()
        }
    )
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val settingsPref = Settings()
    val uid: String? = settingsPref[UserPreferences.UID]
    val name: String? = settingsPref[UserPreferences.NAME]
    val email: String? = settingsPref[UserPreferences.EMAIL]
    val phone: String? = settingsPref[UserPreferences.PHONE]
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost =  { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            ProfileToolbar(
                title = "Perfil",
                onNavigationIconBack = { onNavigateBack() },
                onNavigationIconClose = { onNavigateBack() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(Res.drawable.splashscreenlogo),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape)
                        .background(Color.White, CircleShape)
                        .padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name.toString(),
                    style = TextStyle(
                        color = Color.Yellow,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.icon_email),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = email.toString(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.icon_phone),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = phone.toString().formattedAsPhone(),
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(Res.drawable.icon_equipaments),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    text = "ID: $uid",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }

            LoadingWithLine(isLoading)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            snackBarWithActionButton(
                                coroutineScope = scope,
                                snackBarHostState = snackBarHostState,
                                message = ConstantsApp.MESSAGE_DELETE_ACCOUNT,
                                actionLabel = "Excluir",
                                onAction = { viewModel.onDeleteAccount() }
                            )
                        },
                    painter = painterResource(Res.drawable.icon_account),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    modifier = Modifier.clickable {
                        snackBarWithActionButton(
                            coroutineScope = scope,
                            snackBarHostState = snackBarHostState,
                            message = ConstantsApp.MESSAGE_DELETE_ACCOUNT,
                            actionLabel = "Excluir",
                            onAction = { viewModel.onDeleteAccount() }
                        )
                    },
                    text = "Excluir Conta",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            snackBarWithActionButton(
                                coroutineScope = scope,
                                snackBarHostState = snackBarHostState,
                                message = ConstantsApp.MESSAGE_SIGN_OUT_ACCOUNT,
                                actionLabel = "Sair",
                                onAction = { viewModel.onSignOut() }
                            )
                        },
                    painter = painterResource(Res.drawable.icon_logout),
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    modifier = Modifier.clickable {
                        snackBarWithActionButton(
                            coroutineScope = scope,
                            snackBarHostState = snackBarHostState,
                            message = ConstantsApp.MESSAGE_SIGN_OUT_ACCOUNT,
                            actionLabel = "Sair",
                            onAction = { viewModel.onSignOut() }
                        )
                    },
                    text = "Sair",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    ),
                )
            }

            when (val state = uiState) {
                is ProfileViewState.Dashboard -> { }

                is ProfileViewState.Loading -> { isLoading = true }

                is ProfileViewState.Success -> {
                    isLoading = false
                    RestartApp()
                }

                is ProfileViewState.SuccessClearInfoUser -> {
                    snackBarOnlyMessage(
                        snackBarHostState = snackBarHostState,
                        coroutineScope = scope,
                        message = state.message
                    )
                }

                is ProfileViewState.Error -> {
                    snackBarOnlyMessage(
                        snackBarHostState = snackBarHostState,
                        coroutineScope = scope,
                        message = state.message
                    )
                }
            }
        }
    }
}
