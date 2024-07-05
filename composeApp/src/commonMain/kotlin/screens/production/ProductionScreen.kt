package screens.production

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import components.DrawerMenuNavigation
import components.MenuToolbar
import components.MyAppCircularProgressIndicator
import database.TicketDao
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_chart_line2
import halfmouthappproject.composeapp.generated.resources.icon_freezer
import halfmouthappproject.composeapp.generated.resources.icon_freezer_small
import halfmouthappproject.composeapp.generated.resources.icon_motor
import halfmouthappproject.composeapp.generated.resources.icon_temperature
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import theme.mainYellowColor
import theme.onBackgroundDark
import theme.onSurfaceVariantDark
import util.adjustString
import util.formattedAsDate
import util.formattedAsTime
import util.snackBarOnlyMessage
import viewmodel.ProductionViewModel
import viewmodel.ProductionViewState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ProductionScreen(
    ticketDao: TicketDao,
    onNavigateToProfile: () -> Unit,
    onNavigateToChartLine: (fieldId: String, fieldName: String) -> Unit,
    onNavigateFromDrawerMenu: (route: String) -> Unit,
    viewModel: ProductionViewModel = koinInject()
) {
    val listOfTickets by ticketDao.getAllTickets().collectAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isSnackBarOpen by remember { mutableStateOf(false) }
    var isSnackBarMessageErrorApiOpen by remember { mutableStateOf(false) }


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
                    title = "Produção",
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
                is ProductionViewState.Dashboard -> {
                    LazyColumn(modifier = Modifier.fillMaxWidth(), contentPadding = innerPadding) {
                        items(state.sensorsValues) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 7.dp,
                                        bottom = 7.dp,
                                        start = 12.dp,
                                        end = 12.dp
                                    ),
                                shape = RoundedCornerShape(16.dp),
                            ) {

                                ConstraintLayout {
                                    val (icon, textTitle, textValue, dateText, sendDateText, iconChartLine) = createRefs()

                                    val topIconGuideLine = createGuidelineFromTop(0.08f)
                                    val startIconGuideLine = createGuidelineFromStart(0.85f)
                                    val endIconGuideLine = createGuidelineFromEnd(0.12f)
                                    val bottomIconGuideLine = createGuidelineFromBottom(0.08f)
                                    val shape = RoundedCornerShape(10.dp)

                                    Icon(
                                        vectorResource(Res.drawable.icon_chart_line2),
                                        contentDescription = " ",
                                        tint = mainYellowColor,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .border(1.dp, onBackgroundDark, shape)
                                            .clip(shape)
                                            .padding(3.dp)
                                            .constrainAs(iconChartLine) {
                                                top.linkTo(topIconGuideLine)
                                                end.linkTo(endIconGuideLine)
                                                start.linkTo(startIconGuideLine)
                                                bottom.linkTo(bottomIconGuideLine)
                                            }.clickable {
                                                onNavigateToChartLine(
                                                    it.fieldId.toString(),
                                                    it.fieldType.toString() + it.fieldName.toString()
                                                )
                                            }
                                    )

                                    val drawable =
                                        if (it.fieldName.toString() == "CAMARA FRIA") Res.drawable.icon_freezer_small
                                        else if (it.fieldName.toString() == "CHILLER") Res.drawable.icon_freezer
                                        else if (it.fieldName.toString() == "BOMBA RECIRCULAÇÃO") Res.drawable.icon_motor
                                        else Res.drawable.icon_temperature

                                    Icon(
                                        vectorResource(drawable),
                                        contentDescription = " ",
                                        tint = onSurfaceVariantDark,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .constrainAs(icon) {
                                                top.linkTo(parent.top)
                                                start.linkTo(parent.start, margin = 16.dp)
                                                bottom.linkTo(parent.bottom)
                                            }
                                    )

                                    val name =
                                        if (it.fieldName.toString() == "BOMBA RECIRCULAÇÃO") "MOTOR"
                                        else it.fieldName.toString()

                                    Text(
                                        modifier = Modifier.padding(start = 6.dp)
                                            .constrainAs(textTitle) {
                                                top.linkTo(parent.top, margin = 10.dp)
                                                start.linkTo(icon.end, margin = 10.dp)
                                            },
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )

                                    it.fieldValue?.adjustString()?.let { value ->
                                        Text(
                                            text = value,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.constrainAs(textValue) {
                                                top.linkTo(textTitle.top)
                                                start.linkTo(textTitle.end)
                                                bottom.linkTo(textTitle.bottom)
                                            }
                                        )
                                    }

                                    if (it.fieldData != null) {
                                        val date = it.fieldData.toString()

                                        Text(
                                            modifier = Modifier
                                                .padding(start = 6.dp)
                                                .constrainAs(dateText) {
                                                    top.linkTo(textTitle.bottom, margin = 10.dp)
                                                    start.linkTo(icon.end, margin = 10.dp)
                                                },
                                            text = "Data do Envio: ${date.formattedAsDate()}",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }

                                    if (it.fieldData != null) {
                                        val time = it.fieldData.toString()

                                        Text(
                                            text = "Hora do Envio: ${time.formattedAsTime()}",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier
                                                .padding(start = 6.dp, bottom = 16.dp)
                                                .constrainAs(sendDateText) {
                                                    top.linkTo(dateText.bottom, margin = 10.dp)
                                                    start.linkTo(icon.end, margin = 10.dp)
                                                },
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                is ProductionViewState.Error -> {
                   if(!isSnackBarMessageErrorApiOpen) {
                       snackBarOnlyMessage(
                           snackBarHostState = snackBarHostState,
                           coroutineScope = scope,
                           message = state.message
                       )
                       isSnackBarMessageErrorApiOpen = true
                   }
                }

                is ProductionViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyAppCircularProgressIndicator()
                    }
                }

                is ProductionViewState.ErrorNetworkConnection -> {
                    if(!isSnackBarOpen) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = state.message
                        )
                        isSnackBarOpen = true
                    }
                }
            }
        }
    }
}
