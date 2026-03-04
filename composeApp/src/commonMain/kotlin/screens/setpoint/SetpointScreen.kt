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
    val newSetpoint by viewModel.newSetpointInState.collectAsState()
    val errorSetpointField1 by viewModel.newSetpointField1Error.collectAsState()
    val errorSetpointField2 by viewModel.newSetpointField2Error.collectAsState()
    val errorSetpointField3 by viewModel.newSetpointField3Error.collectAsState()
    val errorSetpointField4 by viewModel.newSetpointField4Error.collectAsState()
    val errorSetpointField5 by viewModel.newSetpointField5Error.collectAsState()
    val errorSetpointField6 by viewModel.newSetpointField6Error.collectAsState()
    val errorSetpointField7 by viewModel.newSetpointField7Error.collectAsState()
    val errorSetpointField8 by viewModel.newSetpointField8Error.collectAsState()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isSnackBarOpen by remember { mutableStateOf(false) }
    var isSnackBarMessageErrorApiOpen by remember { mutableStateOf(false) }
    var progressButtonIsActivated by remember { mutableStateOf(false) }


    ModalNavigationDrawer(
        drawerContent = {
            DrawerMenuNavigation(
                scope = scope,
                drawerState = drawerState,
                tickets = listOfTickets.size,
                onNavigateFromDrawerMenu = { route ->
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
                    if (!isSnackBarMessageErrorApiOpen) {
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
                    if (!isSnackBarOpen) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = state.message
                        )
                        isSnackBarOpen = true
                    }
                }

                is SetpointAdjustViewModelState.SuccessUpdateSetpoint -> {
                    if (!isSnackBarOpen) {
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
                                    if (errorSetpointField1) Text(
                                        text = viewModel.validateSetpointField1(
                                            newSetpoint.setpointField1!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[0].fieldName} = ${state.feeds[0].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField1(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField2.toString(),
                                isError = errorSetpointField2,
                                supportingText = {
                                    if (errorSetpointField2) Text(
                                        text = viewModel.validateSetpointField2(
                                            newSetpoint.setpointField2!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[1].fieldName} = ${state.feeds[1].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField2(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField3.toString(),
                                isError = errorSetpointField3,
                                supportingText = {
                                    if (errorSetpointField3) Text(
                                        text = viewModel.validateSetpointField3(
                                            newSetpoint.setpointField3!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[2].fieldName} = ${state.feeds[2].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField3(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField4.toString(),
                                isError = errorSetpointField4,
                                supportingText = {
                                    if (errorSetpointField4) Text(
                                        text = viewModel.validateSetpointField4(
                                            newSetpoint.setpointField4!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[3].fieldName} = ${state.feeds[3].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField4(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField5.toString(),
                                isError = errorSetpointField5,
                                supportingText = {
                                    if (errorSetpointField5) Text(
                                        text = viewModel.validateSetpointField5(
                                            newSetpoint.setpointField5!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[4].fieldName} = ${state.feeds[4].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField5(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField6.toString(),
                                isError = errorSetpointField6,
                                supportingText = {
                                    if (errorSetpointField6) Text(
                                        text = viewModel.validateSetpointField6(
                                            newSetpoint.setpointField6!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[5].fieldName} = ${state.feeds[5].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField6(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField7.toString(),
                                isError = errorSetpointField7,
                                supportingText = {
                                    if (errorSetpointField7) Text(
                                        text = viewModel.validateSetpointField7(
                                            newSetpoint.setpointField7!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[6].fieldName} = ${state.feeds[6].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField7(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Words,
                                    autoCorrect = true,
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Next
                                ),
                                shape = RoundedCornerShape(20.dp),
                                value = newSetpoint.setpointField8.toString(),
                                isError = errorSetpointField8,
                                supportingText = {
                                    if (errorSetpointField8) Text(
                                        text = viewModel.validateSetpointField8(
                                            newSetpoint.setpointField8!!
                                        )
                                    )
                                },
                                placeholder = { Text("${state.feeds[7].fieldName} = ${state.feeds[7].fieldValue} °C") },
                                onValueChange = { viewModel.onSetpointField8(it.toDouble()) }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            ProgressButton(
                                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                                text = "Enviar",
                                isLoading = progressButtonIsActivated,
                                onClick = {
                                    newSetpoint.setpointField1?.let {
                                        viewModel.validateSetpointField1(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField2?.let {
                                        viewModel.validateSetpointField2(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField3?.let {
                                        viewModel.validateSetpointField3(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField4?.let {
                                        viewModel.validateSetpointField4(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField5?.let {
                                        viewModel.validateSetpointField5(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField6?.let {
                                        viewModel.validateSetpointField6(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField7?.let {
                                        viewModel.validateSetpointField7(
                                            it
                                        )
                                    }
                                    newSetpoint.setpointField8?.let {
                                        viewModel.validateSetpointField8(
                                            it
                                        )
                                    }

                                    if (!errorSetpointField1 && !errorSetpointField2 &&
                                        !errorSetpointField3 && !errorSetpointField4 &&
                                        !errorSetpointField5 && !errorSetpointField6 &&
                                        !errorSetpointField7 && !errorSetpointField8
                                    ) viewModel.writeSetpoint(
                                        setpointField1 = newSetpoint.setpointField1!!,
                                        setpointField2 = newSetpoint.setpointField2!!,
                                        setpointField3 = newSetpoint.setpointField3!!,
                                        setpointField4 = newSetpoint.setpointField4!!,
                                        setpointField5 = newSetpoint.setpointField5!!,
                                        setpointField6 = newSetpoint.setpointField6!!,
                                        setpointField7 = newSetpoint.setpointField7!!,
                                        setpointField8 = newSetpoint.setpointField8!!,
                                    )
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}
