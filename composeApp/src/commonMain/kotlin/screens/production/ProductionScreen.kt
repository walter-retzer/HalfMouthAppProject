package screens.production

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.unit.dp
import components.AppToolbarLarge
import components.MyAppCircularProgressIndicator
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_freezer
import halfmouthappproject.composeapp.generated.resources.icon_thermostat
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource
import util.formattedAsDate
import util.formattedAsTime
import util.snackBarOnlyMessage
import viewmodel.ProductionViewModel
import viewmodel.ProductionViewState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ProductionScreen() {

    val viewModel = getViewModel(
        key = "production-screen",
        factory = viewModelFactory {
            ProductionViewModel()
        }
    )
    val uiState by viewModel.uiState.collectAsState()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppToolbarLarge(
                title = "Produção",
                onNavigationLeftIconClick = { },
                onNavigationProfileIconClick = { },
                onNavigationSettingsIconClick = { },
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
                                .padding(
                                    top = 7.dp,
                                    bottom = 7.dp,
                                    start = 12.dp,
                                    end = 12.dp
                                ),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(
                                            start = 16.dp,
                                            top = 6.dp,
                                            end = 0.dp,
                                            bottom = 6.dp
                                        )
                                        .fillMaxSize(),
                                ) {
                                    val drawable =
                                        if (it.fieldName.toString() == "CAMARA FRIA") Res.drawable.icon_freezer
                                        else if (it.fieldName.toString() == "CHILLER") Res.drawable.icon_freezer
                                        else if (it.fieldName.toString() == "BOMBA RECIRCULAÇÃO") Res.drawable.icon_freezer
                                        else Res.drawable.icon_thermostat
                                    Icon(
                                        vectorResource(drawable),
                                        contentDescription = " ",
                                        tint = Color.White
                                    )

                                    val name =
                                        if (it.fieldName.toString() == "BOMBA RECIRCULAÇÃO") "MBO-001"
                                        else it.fieldName.toString()
                                    Text(
                                        modifier = Modifier.padding(start = 6.dp),
                                        text = name,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                    val valueFormatted = it.fieldValue.toString()
                                    val text =
                                        when (it.fieldValue) {
                                            "0.00000" -> " = Desligado"
                                            "1.00000" -> " = Ligado"
                                            else -> " = ${
                                                valueFormatted.replace(
                                                    ",",
                                                    "."
                                                )
                                            }°C"
                                        }
                                    Text(
                                        text = text,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 24.dp,
                                            top = 0.dp,
                                            end = 16.dp,
                                            bottom = 8.dp
                                        )
                                        .fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    if (it.fieldData != null) {
                                        val date = it.fieldData.toString()

                                        Text(
                                            text = "Data: ${date.formattedAsDate()}",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(
                                            start = 24.dp,
                                            top = 0.dp,
                                            end = 16.dp,
                                            bottom = 8.dp
                                        )
                                        .fillMaxSize(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    if (it.fieldData != null) {
                                        val time = it.fieldData.toString()

                                        Text(
                                            text = "Enviado: ${time.formattedAsTime()}",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is ProductionViewState.Error -> {
                snackBarOnlyMessage(
                    snackBarHostState = snackBarHostState,
                    coroutineScope = scope,
                    message = state.message
                )
            }
            is ProductionViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MyAppCircularProgressIndicator()
                }
            }
        }
    }
}
