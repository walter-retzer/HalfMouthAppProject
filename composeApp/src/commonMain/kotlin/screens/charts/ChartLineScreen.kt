package screens.charts

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import components.DrawerMenuNavigation
import components.MyAppCircularProgressIndicator
import components.SimpleToolbar
import database.TicketDao
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.Roboto_Bold
import kotlinx.coroutines.launch
import network.chaintech.chartsLib.ui.linechart.model.IntersectionPoint
import network.chaintech.cmpcharts.axis.AxisData
import network.chaintech.cmpcharts.common.extensions.formatToSinglePrecision
import network.chaintech.cmpcharts.common.model.Point
import network.chaintech.cmpcharts.common.ui.GridLinesUtil
import network.chaintech.cmpcharts.common.ui.SelectionHighlightPoint
import network.chaintech.cmpcharts.common.ui.SelectionHighlightPopUp
import network.chaintech.cmpcharts.common.ui.ShadowUnderLine
import network.chaintech.cmpcharts.ui.linechart.LineChart
import network.chaintech.cmpcharts.ui.linechart.model.Line
import network.chaintech.cmpcharts.ui.linechart.model.LineChartData
import network.chaintech.cmpcharts.ui.linechart.model.LinePlotData
import network.chaintech.cmpcharts.ui.linechart.model.LineStyle
import org.jetbrains.compose.resources.Font
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
    fieldResult: String,
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
                    viewModel.fetchThingSpeakChannelField(fieldId, fieldResult)
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

                is ChartLineViewState.SuccessSimpleChart -> {
                    Box(modifier = Modifier.padding(innerPadding)) {
                        SimpleChart(fieldName, state.listOfValues, state.listOfDate)
                    }
                }

                is ChartLineViewState.ErrorNetworkConnection -> {
                    if (!isSnackBarMessageErrorApiOpen) {
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

    val xAxisData = AxisData.Builder()
        .fontFamily(
            fontFamily = FontFamily(
                Font(Res.font.Roboto_Bold, weight = FontWeight.Black)
            )
        )
        .axisStepSize(30.dp)
        .topPadding(105.dp)
        .axisLabelColor(Color.Gray)
        .axisLineColor(Color.Gray)
        .steps(pointsData.size - 1)
        .labelData { i -> pointsData[i / 2].description }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .fontFamily(
            fontFamily = FontFamily(
                Font(Res.font.Roboto_Bold, weight = FontWeight.Black)
            )
        )
        .startPadding(20.dp)
        .startDrawPadding(20.dp)
        .steps(steps)
        .axisLabelColor(Color.Gray)
        .axisLineColor(Color.Gray)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Add yMin to get the negative axis values to the scale
            val yMin = pointsData.minOf { it.y }
            val yMax = pointsData.maxOf { it.y }
            val yScale = (yMax - yMin) / steps
            ((i * yScale) + yMin).formatToSinglePrecision()
        }
        .build()

    val lineChartProperties = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(
                        lineType = network.chaintech.cmpcharts.ui.linechart.model.LineType.SmoothCurve(
                            true
                        ),
                        color = mainYellowColor
                    ),
                    intersectionPoint = IntersectionPoint(
                        color = mainYellowColor
                    ),
                    selectionHighlightPoint = SelectionHighlightPoint(
                        color = Color.White
                    ),
                    shadowUnderLine = ShadowUnderLine(
                        brush = Brush.verticalGradient(
                            listOf(
                                mainYellowColor,
                                Color.Transparent
                            )
                        ),
                        alpha = 0.25f
                    ),
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        textMeasurer = textMeasurer,
                        backgroundColor = Color.White,
                        labelColor = Color.Black,
                        labelTypeface = FontWeight.Bold
                    )
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLinesUtil(color = Color.Gray),
        backgroundColor = surfaceVariantDark,
        paddingTop = 40.dp,
        bottomPadding = 30.dp,
        paddingRight = 20.dp,
        containerPaddingEnd = 30.dp,
        isZoomAllowed = false
    )
    LineChart(
        modifier = Modifier.fillMaxSize(),
        lineChartData = lineChartProperties
    )
}

@Composable
fun SimpleChart(
    fieldName: String,
    data: List<Double>,
    listOfDate: List<String>
) {
    val lineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = fieldName,
            data = data,
            lineColor = mainYellowColor,
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        )
    )
    LineChart(
        modifier = Modifier
            .background(surfaceVariantDark)
            .padding(20.dp)
            .fillMaxSize(),
        linesParameters = lineParameters,
        isGrid = true,
        gridColor = Color.Gray,
        xAxisData = listOfDate,
        animateChart = true,
        showGridWithSpacer = true,
        descriptionStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.W400
        ),
        yAxisStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
        ),
        xAxisStyle = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.W400
        ),
        yAxisRange = 4,
        oneLineChart = false,
        showXAxis = true,
        showYAxis = true,
        gridOrientation = GridOrientation.GRID,
        legendPosition = LegendPosition.BOTTOM,
    )
}
