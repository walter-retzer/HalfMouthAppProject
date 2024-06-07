package screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import components.ProgressButton
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.splashscreenlogo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import theme.mainYellowColor
import util.ConstantsApp
import util.snackBarOnlyMessage
import viewmodel.LoginUserViewModel


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
    val uiState by viewModel.uiState.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val auth = remember { Firebase.auth }
    var firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
    var progressButtonIsActivated by remember { mutableStateOf(false) }


    Scaffold(
       snackbarHost =  { SnackbarHost(hostState = snackBarHostState) }
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
                    value = uiState.email,
                    isError = emailError,
                    supportingText = {
                        if (emailError) Text(text = viewModel.validateEmail(uiState.email))
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
                    value = uiState.password,
                    isError = passwordError,
                    supportingText = {
                        if (passwordError)
                            Text(text = viewModel.validatePassword(uiState.password))
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    placeholder = { Text("Senha") },
                    onValueChange = {
                        if (it.length <= ConstantsApp.PASSWORD_MAX_NUMBER) viewModel.onPasswordChange(
                            it
                        )
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                ProgressButton(
                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                    text = "Entrar",
                    isLoading = progressButtonIsActivated,
                    onClick = {
                        viewModel.validateEmail(uiState.email)
                        viewModel.validatePassword(uiState.password)
                        if (!emailError || !passwordError) {
                            scope.launch {
                                try {
                                    progressButtonIsActivated = true
                                    auth.signInWithEmailAndPassword(
                                        email = uiState.email,
                                        password = uiState.password
                                    )
                                    firebaseUser = auth.currentUser
                                } catch (e: Exception) {
                                    progressButtonIsActivated = false
                                    snackBarOnlyMessage(
                                        snackBarHostState = snackBarHostState,
                                        coroutineScope = scope,
                                        message = "Não foi possível criar a sua conta, por favor, tente mais tarde.",
                                        duration = SnackbarDuration.Long
                                    )
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier,
                    text = "Ainda não tem uma conta?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    modifier = Modifier.clickable { onNavigateToSignIn() },
                    text = "Cadastre-se",
                    style = MaterialTheme.typography.bodyLarge,
                    color = mainYellowColor,
                )

                if (firebaseUser != null) {
                    LaunchedEffect(key1 = true) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = "Conta criada com Sucesso!"
                        )
                        delay(2000L)
                        progressButtonIsActivated = false
                        onNavigateToHome()
                    }
                }
            }
        }
    }
}
