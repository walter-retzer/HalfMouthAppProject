package screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import components.ProgressButton
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.splashscreenlogo
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.mainYellowColor
import util.ConstantsApp
import util.snackBarOnlyMessage
import viewmodel.LoginUserViewModel
import viewmodel.LoginUserViewState


@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToSignIn: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val viewModel = getViewModel(
        key = "login-screen",
        factory = viewModelFactory {
            LoginUserViewModel()
        }
    )
    val userLogin by viewModel.userLoginState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var progressButtonIsActivated by remember { mutableStateOf(false) }
    var snackBarIsActivated by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
        ) {
            val width = this.maxWidth
            val finalModifier =
                if (width >= 780.dp) modifier.width(400.dp) else modifier.fillMaxWidth()
            Column(
                modifier = finalModifier.padding(start = 16.dp, end = 16.dp).fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Seja Bem Vindo!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Image(
                    painter = painterResource(Res.drawable.splashscreenlogo),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .background(Color.White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.align(alignment = Alignment.Start),
                    text = "Informe seus dados:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(20.dp),
                    value = userLogin.email,
                    isError = emailError,
                    supportingText = {
                        if (emailError) Text(text = viewModel.validateEmail(userLogin.email))
                    },
                    placeholder = { Text(text = "Email") },
                    onValueChange = { viewModel.onEmailChange(it) },
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = true,
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(20.dp),
                    value = userLogin.password,
                    isError = passwordError,
                    supportingText = {
                        if (passwordError)
                            Text(text = viewModel.validatePassword(userLogin.password))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text("Senha") },
                    onValueChange = {
                        if (it.length <= ConstantsApp.PASSWORD_MAX_NUMBER) viewModel.onPasswordChange(
                            it
                        )
                    }
                )

                Text(
                    modifier = Modifier.align(alignment = Alignment.End),
                    text = "Esqueceu a senha?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProgressButton(
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    text = "Entrar",
                    isLoading = progressButtonIsActivated,
                    onClick = {
                        viewModel.validateEmail(userLogin.email)
                        viewModel.validatePassword(userLogin.password)
                        if (!emailError && !passwordError && userLogin.password.length == ConstantsApp.PASSWORD_MAX_NUMBER) {
                            viewModel.onSignIn(userLogin.email, userLogin.password)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.clickable { showBottomSheet = true },
                    text = "Ainda não tem uma conta?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier.clickable { onNavigateToSignIn() },
                    text = "Cadastre-se",
                    style = MaterialTheme.typography.bodyLarge,
                    color = mainYellowColor,
                )

                if (showBottomSheet) {
                    BottomSheet(viewModel) {
                        showBottomSheet = false
                    }
                }

                when (val state = uiState) {
                    is LoginUserViewState.Dashboard -> { }

                    is LoginUserViewState.Loading -> { progressButtonIsActivated = true }

                    is LoginUserViewState.Error -> {
                        progressButtonIsActivated = false
                        snackBarIsActivated = true

                        LaunchedEffect(snackBarIsActivated) {
                            snackBarOnlyMessage(
                                snackBarHostState = snackBarHostState,
                                coroutineScope = scope,
                                message = state.message,
                                duration = SnackbarDuration.Long
                            )
                            snackBarIsActivated = false
                        }
                    }

                    is LoginUserViewState.Success -> {
                        progressButtonIsActivated = false
                        LaunchedEffect(key1 = true) {
                            snackBarOnlyMessage(
                                snackBarHostState = snackBarHostState,
                                coroutineScope = scope,
                                message = state.message
                            )
                            delay(2000L)
                            onNavigateToHome()
                        }
                    }

                    is LoginUserViewState.SuccessResetPassword -> {
                        println("Success RESET")
                        snackBarIsActivated = true

                        LaunchedEffect(snackBarIsActivated) {
                            snackBarOnlyMessage(
                                snackBarHostState = snackBarHostState,
                                coroutineScope = scope,
                                message = state.message,
                                duration = SnackbarDuration.Long
                            )
                            snackBarIsActivated = false
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(viewModel: LoginUserViewModel, onDismiss: () -> Unit) {

    val modalBottomSheetState = rememberModalBottomSheetState()
    var email by rememberSaveable { mutableStateOf("") }

    val userLogin by viewModel.userLoginState.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    ModalBottomSheet(
        //modifier = Modifier.displayCutoutPadding(),
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        //windowInsets = WindowInsets.displayCutout
    ) {

        Column(
            modifier = Modifier.padding(16.dp).height(250.dp)
        ) {

            Text(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                text = "Recuperar senha",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier.align(alignment = Alignment.Start),
                text = "Informe seu email:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(20.dp),
                value = email,
                placeholder = { Text(text = "Email") },
                onValueChange = { viewModel.onEmailForResetPasswordChange(it) }
            )

            ProgressButton(
                modifier = Modifier.padding(top = 16.dp, bottom = 50.dp).fillMaxWidth(),
                text = "Enviar",
                isLoading = false,
                onClick = {
                    viewModel.onResetPassword(email)
                    onDismiss()
                }
            )
        }
    }
}
