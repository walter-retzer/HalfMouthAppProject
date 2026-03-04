package screens.setpoint

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import components.DrawerMenuNavigation
import components.MenuToolbar
import components.MyAppCircularProgressIndicator
import components.ProgressButton
import database.TicketDao
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import util.snackBarOnlyMessage
import viewmodel.SetpointAdjustViewModel
import viewmodel.SetpointAdjustViewModelState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetpopintScreen(
    modifier: Modifier = Modifier,
    ticketDao: TicketDao,
    onNavigateToProfile: () -> Unit,
    onNavigateFromDrawerMenu: (route: String) -> Unit,
    viewModel: SetpointAdjustViewModel = koinInject()
) {
    val listOfTickets by ticketDao.getAllTickets().collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isSnackBarOpen by remember { mutableStateOf(false) }
    var isSnackBarMessageErrorApiOpen by remember { mutableStateOf(false) }


    val nameError by viewModel.nameError.collectAsState()
    val phoneError by viewModel.phoneNumberError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()

    val newSetpoint by viewModel.newSetpointInState.collectAsState()
    val errorSetpointField1 by viewModel.newSetpointField1Error.collectAsState()

    var progressButtonIsActivated by remember { mutableStateOf(false) }
    var snackBarIsActivated by remember { mutableStateOf(false) }


    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenuNavigation(
                scope = scope,
                drawerState = drawerState,
                tickets = listOfTickets.size,
                onNavigateFromDrawerMenu = { route->
                    onNavigateFromDrawerMenu(route)
                }
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                MenuToolbar(
                    title = "Ajuste de Setpoint",
                    onNavigationToMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNavigationToProfile = { onNavigateToProfile() },
                    onNavigateToNotifications = { },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->
            when (val state = uiState) {

                is SetpointAdjustViewModelState.Error -> {
                   if(!isSnackBarMessageErrorApiOpen) {
                       snackBarOnlyMessage(
                           snackBarHostState = snackBarHostState,
                           coroutineScope = scope,
                           message = state.message
                       )
                       isSnackBarMessageErrorApiOpen = true
                   }
                }

                is SetpointAdjustViewModelState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyAppCircularProgressIndicator()
                    }
                }

                is SetpointAdjustViewModelState.ErrorNetworkConnection -> {
                    if(!isSnackBarOpen) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = state.message
                        )
                        isSnackBarOpen = true
                    }
                }

                is SetpointAdjustViewModelState.SuccessUpdateSetpoint -> {
                    if(!isSnackBarOpen) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = state.feeds.toString()
                        )
                        isSnackBarOpen = true
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .windowInsetsPadding(WindowInsets.safeDrawing)
                                .verticalScroll(rememberScrollState())
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                modifier = Modifier.align(alignment = Alignment.Start),
                                text = "Ajuste dos valores:",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.secondary,
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField1.toString(),
                                isError = errorSetpointField1,
                                supportingText = {
                                    if (errorSetpointField1) Text(text = viewModel.validateSetpointField1(newSetpoint.setpointField1!!))
                                },
                                placeholder = { Text("${state.feeds[0].fieldName} = ${state.feeds[0].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField1(it.toDouble())}
                            )

                            Spacer(modifier = Modifier.height(6.dp))

//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.None,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.Phone,
//                                    imeAction = ImeAction.Next
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.phoneNumber,
//                                isError = phoneError,
//                                supportingText = {
//                                    if (phoneError) Text(" phone")
//                                },
//                                placeholder = { Text("Celular") },
//                                onValueChange = { },
//                                visualTransformation = MaskVisualTransformation(
//                                    MaskVisualTransformation.PHONE
//                                )
//                            )
//
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.None,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.Email,
//                                    imeAction = ImeAction.Next
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.email,
//                                isError = emailError,
//                                supportingText = {
//                                    if (emailError) Text("erro"
//                                    )
//                                },
//                                placeholder = { Text(text = "Email") },
//                                onValueChange = {  },
//                            )
//
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.None,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.NumberPassword,
//                                    imeAction = ImeAction.Done
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.password,
//                                isError = passwordError,
//                                supportingText = {
//                                    if (passwordError)
//                                        Text(text = "Errro")
//                                },
//                                visualTransformation = PasswordVisualTransformation(),
//                                placeholder = { Text("Senha") },
//                                onValueChange = { }
//                            )
//
//                            Spacer(modifier = Modifier.height(10.dp))
//
//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.Words,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.Text,
//                                    imeAction = ImeAction.Next
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.name,
//                                isError = nameError,
//                                supportingText = {
//                                    if (nameError) Text(
//                                        text = "Texy"
//                                    )
//                                },
//                                placeholder = { Text("Nome") },
//                                onValueChange = { }
//                            )
//
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.None,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.Phone,
//                                    imeAction = ImeAction.Next
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.phoneNumber,
//                                isError = phoneError,
//                                supportingText = {
//                                    if (phoneError) Text(" phone")
//                                },
//                                placeholder = { Text("Celular") },
//                                onValueChange = { },
//                                visualTransformation = MaskVisualTransformation(
//                                    MaskVisualTransformation.PHONE
//                                )
//                            )
//
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.None,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.Email,
//                                    imeAction = ImeAction.Next
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.email,
//                                isError = emailError,
//                                supportingText = {
//                                    if (emailError) Text("erro"
//                                    )
//                                },
//                                placeholder = { Text(text = "Email") },
//                                onValueChange = {  },
//                            )
//
//                            Spacer(modifier = Modifier.height(6.dp))
//
//                            OutlinedTextField(
//                                modifier = Modifier.fillMaxWidth(),
//                                keyboardOptions = KeyboardOptions(
//                                    capitalization = KeyboardCapitalization.None,
//                                    autoCorrect = true,
//                                    keyboardType = KeyboardType.NumberPassword,
//                                    imeAction = ImeAction.Done
//                                ),
//                                shape = RoundedCornerShape(20.dp),
//                                value = newUserSignInState.password,
//                                isError = passwordError,
//                                supportingText = {
//                                    if (passwordError)
//                                        Text(text = "Errro")
//                                },
//                                visualTransformation = PasswordVisualTransformation(),
//                                placeholder = { Text("Senha") },
//                                onValueChange = { }
//                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            ProgressButton(
                                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                                text = "Enviar",
                                isLoading = progressButtonIsActivated,
                                onClick = { viewModel.writeSetpoint() }
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}
