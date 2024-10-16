package screens.charts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import components.DrawerMenuNavigation
import components.MyAppCircularProgressIndicator
import components.SimpleToolbar
import database.TicketDao
import kotlinx.coroutines.launch
import network.chaintech.chartsLib.ui.linechart.model.IntersectionPoint
import network.chaintech.cmpcharts.axis.AxisProperties
import network.chaintech.cmpcharts.common.extensions.formatToSinglePrecision
import network.chaintech.cmpcharts.common.model.Point
import network.chaintech.cmpcharts.common.ui.GridLinesUtil
import network.chaintech.cmpcharts.common.ui.SelectionHighlightPoint
import network.chaintech.cmpcharts.common.ui.SelectionHighlightPopUp
import network.chaintech.cmpcharts.ui.linechart.LineChart
import network.chaintech.cmpcharts.ui.linechart.model.Line
import network.chaintech.cmpcharts.ui.linechart.model.LineChartProperties
import network.chaintech.cmpcharts.ui.linechart.model.LinePlotData
import network.chaintech.cmpcharts.ui.linechart.model.LineStyle
import org.koin.compose.koinInject
import theme.mainYellowColor
import theme.surfaceVariantDark
import util.snackBarOnlyMessage
import viewmodel.ChartLineViewModel
import viewmodel.ChartLineViewState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartLineScreen(
    ticketDao: TicketDao,
    fieldId: String,
    fieldName: String,
    onNavigateToProduction: () -> Unit,
    onNavigateFromDrawerMenu: (route: String) -> Unit,
    viewModel: ChartLineViewModel = koinInject()
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
                SimpleToolbar(
                    title = fieldName,
                    onNavigationToMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNavigationClose = { onNavigateToProduction() },
                )
            }
        ) { innerPadding ->

            when (val state = uiState) {
                is ChartLineViewState.Dashboard -> {
                    viewModel.fetchThingSpeakChannelField(fieldId)
                }

                is ChartLineViewState.Error -> {
                    if (!isSnackBarOpen) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = state.message
                        )
                        isSnackBarOpen = true
                    }
                }

                is ChartLineViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        MyAppCircularProgressIndicator()
                    }
                }

                is ChartLineViewState.Success -> {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SingleLineChartWithGridLines(state.listOfPointsToPlot)
                    }
                }

                is ChartLineViewState.ErrorNetworkConnection -> {
                    if(!isSnackBarMessageErrorApiOpen) {
                        snackBarOnlyMessage(
                            snackBarHostState = snackBarHostState,
                            coroutineScope = scope,
                            message = state.message
                        )
                        isSnackBarMessageErrorApiOpen = true
                    }
                }
            }
        }
    }
}

@Composable
fun SingleLineChartWithGridLines(
    pointsData: List<Point>
) {
    val textMeasurer = rememberTextMeasurer()
    val steps = 5

    val xAxisProperties = AxisProperties(
        font = null,
        labelPadding = 10.dp,
        labelColor = Color.Gray,
        lineColor = Color.Gray,
        stepCount = pointsData.size -1,
        labelFormatter = { i -> pointsData[i/2].description },
        shouldExtendLineToEnd = true
    )

    val yAxisProperties = AxisProperties(
        font = null,
        stepCount = steps,
        labelColor = Color.Gray,
        lineColor = Color.Gray,
        labelFormatter = { i ->
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
    )

    val lineChartProperties = LineChartProperties(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = mainYellowColor
                    ),
                    IntersectionPoint(
                        color = mainYellowColor
                    ),
                    SelectionHighlightPoint(
                        color = Color.White
                    ),
                   null,
                    SelectionHighlightPopUp(
                        textMeasurer = textMeasurer,
                        backgroundColor = Color.White,
                        labelColor = Color.Black,
                        labelTypeface = FontWeight.Bold
                    )
                )
            )
        ),
        xAxisProperties = xAxisProperties,
        yAxisProperties = yAxisProperties,
        gridLines = GridLinesUtil(color = Color.Gray),
        backgroundColor = surfaceVariantDark,
        paddingTop = 40.dp,
        bottomPadding =  30.dp,
        paddingRight = 20.dp,
        containerPaddingEnd= 30.dp,
    )
    LineChart(
        modifier = Modifier.fillMaxSize(),
        lineChartProperties = lineChartProperties
    )
}
