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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import components.MenuToolbar
import components.MyAppCircularProgressIndicator
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.launch
import theme.mainYellowColor
import theme.primaryContainerDark
import theme.secondaryContainerDark
import theme.surfaceVariantDark
import theme.tertiaryDark
import viewmodel.ChartLineViewModel
import viewmodel.ChartLineViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartLineScreen() {

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
                MenuToolbar(
                    title = "Gráficos",
                    onNavigationToMenu = {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    onNavigationToProfile = { },
                    onNavigateToNotifications = { },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { innerPadding ->

            when (val state = uiState) {

                is ChartLineViewState.Dashboard -> {
                    viewModel.fetchThingSpeakChannelField()
                }

                is ChartLineViewState.Error -> {

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
                    val list = state.channelFeed.mapNotNull {
                        it?.field5.toString().toDouble()
                    }
                    val date = state.channelFeed.mapNotNull {
                        it?.created_at.toString()
                    }
                    val testLineParameters: List<LineParameters> = listOf(
                        LineParameters(
                            label = "Temperatura TI-005",
                            data = list,
                            lineColor = mainYellowColor,
                            lineType = LineType.DEFAULT_LINE,
                            lineShadow = true,
                        )
                    )

                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        LineChart(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(surfaceVariantDark)
                                .padding(20.dp),
                            linesParameters = testLineParameters,
                            isGrid = true,
                            gridColor = Color.Gray,
                            xAxisData = listOf("2001", "2002", "2003", "2004", "2019"),
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
                            yAxisRange = 6,
                            oneLineChart = false,
                            showXAxis = true,
                            showYAxis = true,
                            gridOrientation = GridOrientation.GRID,
                            legendPosition = LegendPosition.BOTTOM
                        )
                    }
                }
            }
        }
    }
}
