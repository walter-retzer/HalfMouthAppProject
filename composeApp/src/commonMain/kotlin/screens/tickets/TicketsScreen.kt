package screens.tickets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import components.TicketsToolbar
import database.TicketDao
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.image_ticket_on_top
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.mainYellowColor
import theme.onBackgroundDark
import theme.surfaceBrightDark
import theme.surfaceVariantDark
import util.ConstantsApp
import util.snackBarOnlyMessage


@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    ticketDao: TicketDao,
    onNavigateBack: () -> Unit,
    onNavigateClose: () -> Unit,
){
    val listOfTickets by ticketDao.getAllTickets().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    val snackBarHostState = remember { SnackbarHostState() }
    var isSnackBarDisplaying by remember { mutableStateOf( false) }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            TicketsToolbar(
                title = "Meus Cupons",
                onNavigationIconBack = { onNavigateBack() },
                onNavigationIconClose = { onNavigateClose() }
            )
        }
    ) { innerPadding ->
        LaunchedEffect(key1 = true) {
            delay(500L)
            if(!isSnackBarDisplaying && listOfTickets.isNullOrEmpty()){
                snackBarOnlyMessage(
                    snackBarHostState = snackBarHostState,
                    coroutineScope = scope,
                    message = ConstantsApp.TICKET_EMPTY_LIST
                )
                isSnackBarDisplaying = true
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = innerPadding,
        ) {
            listOfTickets?.let{ list->
                items(list) { ticket ->
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                            .background(
                                color = surfaceVariantDark,
                                shape = RoundedCornerShape(25.dp)
                            )
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.image_ticket_on_top),
                            contentDescription = "qr-code",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .width(100.dp)
                                .align(Alignment.CenterEnd)
                                .padding(
                                    top = 12.dp,
                                    end = 24.dp,
                                    bottom = 12.dp
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            val shape = RoundedCornerShape(10.dp)
                            Text(
                                text = ticket.discountValue,
                                modifier = Modifier
                                    .padding(top = 12.dp, start = 20.dp)
                                    .border(1.dp, onBackgroundDark, shape)
                                    .background(surfaceBrightDark, shape)
                                    .padding(start = 4.dp, end = 4.dp)
                                    .clickable {
                                        scope.launch {
                                            ticketDao.delete(ticket)
                                        }
                                    },
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 20.sp,
                                color = mainYellowColor,
                            )

                            Text(
                                text = "Válido até ${ticket.expirationDate}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        scope.launch {
                                            ticketDao.delete(ticket)
                                        }
                                    }
                                    .padding(top = 10.dp, start = 24.dp),
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 14.sp,
                            )

                            Text(
                                text = "Cupom de desconto gerado em ${ticket.dateTicketCreated} às ${ticket.timeTicketCreated}hrs",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .clickable {
                                        scope.launch {
                                            ticketDao.delete(ticket)
                                        }
                                    }
                                    .padding(top = 5.dp, start = 24.dp, bottom = 12.dp, end = 124.dp),
                                style = MaterialTheme.typography.titleSmall,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}
