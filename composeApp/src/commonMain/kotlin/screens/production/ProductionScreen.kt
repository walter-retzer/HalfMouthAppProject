package screens.production

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.AppToolbarLarge
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.icon_beer
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.onSurfaceVariantLight
import viewmodel.ProductionViewModel

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
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val listFeedReceive = viewModel.newFeedList

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            AppToolbarLarge(
                title = "Perfil",
                onNavigationLeftIconClick = { },
                onNavigationProfileIconClick = { },
                onNavigationSettingsIconClick = { },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(listFeedReceive) {
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

                    Row(
                        modifier = Modifier
                            .padding(
                                top = 24.dp,
                                end = 16.dp,
                            )
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.icon_beer),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(5.dp)
                                .clickable(onClick = { }),
                            alignment = Alignment.BottomEnd,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    end = 0.dp,
                                    bottom = 4.dp
                                )
                                .fillMaxSize(),
                        ) {
//                            val drawable =
//                                if (it.fieldName.toString() == "CAMARA FRIA") Res.drawable.icon_freezer
//                                else if (it.fieldName.toString() == "CHILLER") Res.drawable.icon_freezer
//                                else if (it.fieldName.toString() == "BOMBA RECIRCULAÇÃO") Res.drawable.icon_freezer
//                                else Res.drawable.icon_thermostat
                            Image(
                                painter = painterResource(Res.drawable.icon_beer),
                                contentDescription = null,
                                modifier = Modifier.padding(5.dp)
                            )
                            val name =
                                if (it.fieldName.toString() == "BOMBA RECIRCULAÇÃO") "MBO-001"
                                else it.fieldName.toString()
                            Text(
                                text = name,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                            val valueFormatted = it.fieldValue.toString()
                            val text =
                                when (it.fieldValue) {
                                    "0.00000" -> " = 0"
                                    "1.00000" -> " = 1"
                                    else -> " = ${
                                        valueFormatted.replace(
                                            ",",
                                            "."
                                        )
                                    }°C"
                                }
                            Text(
                                text = text,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                )
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
                                    text = "Data: $date",
                                    style = TextStyle(
                                        color = onSurfaceVariantLight,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                    )
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}