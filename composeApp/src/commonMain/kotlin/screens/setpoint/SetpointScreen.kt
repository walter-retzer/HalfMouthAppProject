package screens.setpoint

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import components.DrawerMenuNavigation
import components.MenuToolbar
import components.MyAppCircularProgressIndicator
import components.ProgressButton
import database.TicketDao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import util.snackBarOnlyMessage
import viewmodel.SetpointAdjustViewModel
import viewmodel.SetpointAdjustViewModelState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetpopintScreen(
    ticketDao: TicketDao,
    onNavigateToHome: () -> Unit,
    onNavigateFromDrawerMenu: (route: String) -> Unit,
    viewModel: SetpointAdjustViewModel = koinInject()
) {
    val listOfTickets by ticketDao.getAllTickets().collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val setpointUiState by viewModel.setpointUiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isSnackBarOpen by remember { mutableStateOf(false) }
    var isSnackBarMessageErrorApiOpen by remember { mutableStateOf(false) }
    var progressButtonIsActivated by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(false) }


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
                    onNavigationToProfile = { onNavigateToHome() },
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

                            repeat(8) { i ->
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Words,
                                        autoCorrect = true,
                                        keyboardType = KeyboardType.Decimal,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    value = setpointUiState.texts[i],
                                    isError = setpointUiState.errors[i],
                                    supportingText = {
                                        if (setpointUiState.errors[i]) Text(viewModel.errorMessage())
                                    },
                                    placeholder = { Text("${state.feeds[i].fieldName} = ${state.feeds[i].fieldValue} °C") },
                                    onValueChange = { viewModel.onTextChange(i, it) }
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            isVisible = viewModel.validateAll()
                            AnimatedVisibility(visible = isVisible) {
                                ProgressButton(
                                    modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                                    text = "Enviar",
                                    isLoading = progressButtonIsActivated,
                                    onClick = {
                                        progressButtonIsActivated = true
                                        focusManager.clearFocus()
                                        val ok = viewModel.validateAll()
                                        if (ok) {
                                            val values = setpointUiState.values
                                            println("ok: ${values}")
                                            viewModel.writeSetpoint(
                                                setpointField1 = setpointUiState.values[0]!!,
                                                setpointField2 = setpointUiState.values[1]!!,
                                                setpointField3 = setpointUiState.values[2]!!,
                                                setpointField4 = setpointUiState.values[3]!!,
                                                setpointField5 = setpointUiState.values[4]!!,
                                                setpointField6 = setpointUiState.values[5]!!,
                                                setpointField7 = setpointUiState.values[6]!!,
                                                setpointField8 = setpointUiState.values[7]!!,
                                            )
                                        } else {
                                            println("Error1: ${setpointUiState.errors[0]}")
                                        }
                                    }
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }

                is SetpointAdjustViewModelState.SuccessWriteSetpoint -> {
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
            }
        }
    }
}
