package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Text
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import util.ConstantsApp
import util.MaskVisualTransformation
import viewmodel.SignInViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun SignInScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = getViewModel(
        key = "sign-in",
        factory = viewModelFactory {
            SignInViewModel()
        }
    )
    val uiState by viewModel.uiState.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val phoneError by viewModel.phoneNumberError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val scope = rememberCoroutineScope()
    val auth = remember { Firebase.auth }
    var firebaseUser: FirebaseUser? by remember { mutableStateOf(null) }
    var progressButtonIsActivated by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        val width = this.maxWidth
        val finalModifier = if (width >= 780.dp) modifier.width(400.dp) else modifier.fillMaxWidth()
        Column(
            modifier = finalModifier.padding(start = 16.dp, end = 16.dp).fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Criar uma conta",
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
                    .border(2.dp, androidx.compose.ui.graphics.Color.White, CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier.align(alignment = Alignment.Start),
                text = "Inscreva-se para começar:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(20.dp),
                value = uiState.name,
                isError = nameError,
                supportingText = {
                    if (nameError) Text(text = viewModel.validateName(uiState.name))
                },
                placeholder = { Text("Nome") },
                onValueChange = {
                    if (it.length <= ConstantsApp.NAME_MAX_NUMBER) viewModel.onNameChange(it)
                }
            )

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(20.dp),
                value = uiState.phoneNumber,
                isError = phoneError,
                supportingText = {
                    if (phoneError) Text(text = viewModel.validatePhoneNumber(uiState.phoneNumber))
                },
                placeholder = { Text("Celular") },
                onValueChange = {
                    if (it.length <= ConstantsApp.PHONE_MAX_NUMBER) viewModel.onPhoneNumberChange(it)
                },
                visualTransformation = MaskVisualTransformation(MaskVisualTransformation.PHONE)
            )

            Spacer(modifier = Modifier.height(6.dp))

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
                    imeAction = ImeAction.Done
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
                    if (it.length <= ConstantsApp.PASSWORD_MAX_NUMBER) viewModel.onPasswordChange(it)
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ProgressButton(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                text = "Cadastrar",
                isLoading = progressButtonIsActivated,
                onClick = {
                    viewModel.validateName(uiState.name)
                    viewModel.validatePhoneNumber(uiState.phoneNumber)
                    viewModel.validateEmail(uiState.email)
                    viewModel.validatePassword(uiState.password)
                    if (!nameError || !phoneError || !emailError || !passwordError) {
                        scope.launch {
                            try {
                                auth.createUserWithEmailAndPassword(
                                    email = uiState.email,
                                    password = uiState.password
                                )
                                progressButtonIsActivated = true
                            } catch (e: Exception) {
                                progressButtonIsActivated = false
                            }
                        }
                    }
                }
            )
        }
    }
}
