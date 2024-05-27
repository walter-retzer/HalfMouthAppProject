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
import util.MaskVisualTransformation
import viewmodel.SignInContactEvent
import viewmodel.SignInViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun SignInScreen(
    modifier: Modifier = Modifier,
) {
    val PHONE_MAX_NUMBER = 11
    val NAME_MAX_NUMBER = 25
    val PASSWORD_MAX_NUMBER = 6

    val scope = rememberCoroutineScope()
    val auth = remember { Firebase.auth }
    var firebaseUser: FirebaseUser? by remember{ mutableStateOf(null) }
    var userEmail by remember{ mutableStateOf("") }
    var userPassword by remember{ mutableStateOf("") }
    var progressButtonIsActivated by remember{ mutableStateOf(false) }

    val viewModel = getViewModel(
        key = "contact-list-screen",
        factory = viewModelFactory {
            SignInViewModel()
        }
    )
    //val state by viewModel.state.collectAsState()


    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        val width = this.maxWidth
        val finalModifier = if (width >= 780.dp) modifier.width(400.dp) else modifier.fillMaxWidth()
        Column(
            modifier = finalModifier.padding(start = 16.dp, end= 16.dp).fillMaxHeight()
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
                value = viewModel.newUser.name,
                placeholder = {Text("Nome")},
                onValueChange = { if (it.length <= NAME_MAX_NUMBER) viewModel.onEvent(
                    SignInContactEvent.OnFirstNameChanged(it)
                )}
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(20.dp),
                value = viewModel.newUser.phoneNumber,
                placeholder = { Text("Celular") },
                onValueChange = {
                    if (it.length <= PHONE_MAX_NUMBER) viewModel.onEvent(
                        SignInContactEvent.OnPhoneNumberChanged(it)
                    )
                },
                visualTransformation = MaskVisualTransformation(MaskVisualTransformation.PHONE)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(20.dp),
                value = viewModel.newUser.email,
                placeholder = { Text(text = "Email") },
                onValueChange = {
                    viewModel.onEvent(SignInContactEvent.OnEmailChanged(it))
                    userEmail = it
                },

                )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = true,
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(20.dp),
                value = viewModel.newUser.password,
                visualTransformation = PasswordVisualTransformation(),
                placeholder = { Text("Senha") },
                onValueChange = {
                    if (it.length <= PASSWORD_MAX_NUMBER) viewModel.onEvent(
                    SignInContactEvent.OnPasswordChanged(it))
                    userPassword = it
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProgressButton(
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                text = "Cadastrar",
                isLoading = progressButtonIsActivated,
                onClick = {
                    progressButtonIsActivated = !progressButtonIsActivated
                    scope.launch {
                        try{
                            auth.createUserWithEmailAndPassword(
                                email = userEmail,
                                password = userPassword
                            )
                            progressButtonIsActivated = !progressButtonIsActivated
                        } catch(e: Exception){
                            auth.signInWithEmailAndPassword(
                                email = userEmail,
                                password = userPassword
                            )
                            progressButtonIsActivated = !progressButtonIsActivated
                        }
                    }
                }
            )
        }
    }
}

