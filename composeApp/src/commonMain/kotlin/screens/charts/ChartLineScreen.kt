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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch
import theme.mainYellowColor
import theme.surfaceVariantDark
import util.snackBarOnlyMessage
import viewmodel.ChartLineViewModel
import viewmodel.ChartLineViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartLineScreen(
    onNavigateToProduction: () -> Unit,
    fieldId: String,
    fieldName: String
) {

    val viewModel = getViewModel(
        key = "chart-line-screen",
        factory = viewModelFactory {
            ChartLineViewModel()
        }
    )
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
                onNavigateToDrawerMenu = { },
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
            topBar = {
                SimpleToolbar(
                    title = "Gráfico",
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
                    val lineParameters: List<LineParameters> = listOf(
                        LineParameters(
                            label = fieldName,
                            data = state.listOfValues,
                            lineColor = mainYellowColor,
                            lineType = LineType.CURVED_LINE,
                            lineShadow = true,
                        )
                    )

                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        LineChart(
                            modifier = Modifier
                                .background(surfaceVariantDark)
                                .padding(20.dp)
                                .fillMaxSize(),
                            linesParameters = lineParameters,
                            isGrid = true,
                            gridColor = Color.Gray,
                            xAxisData = state.listOfDate,
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
