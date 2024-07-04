package screens.tickets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.Ticket
import database.TicketDao
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.image_ticket_on_top
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.mainYellowColor
import theme.onBackgroundDark
import theme.surfaceBrightDark
import theme.surfaceVariantDark

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TicketScreen(ticketDao: TicketDao){
    val ticket by ticketDao.getAllTickets().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

//    LaunchedEffect(true) {
//        val ticketList = listOf(
//            Ticket(date = "11/05/1988", url = "wwwww"),
//            Ticket(date = "12/06/1993", url = "wfwgcgcgcgc"),
//            Ticket(date = "12/08/2093", url = "wwvhvhvhvhhvhvh"),
//        )
//        ticketList.forEach {
//            ticketDao.upsert(it)
//        }
//    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(ticket) { info ->
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

                Column(modifier = Modifier
                    .fillMaxSize()
                ) {
                    val shape = RoundedCornerShape(10.dp)
                    Text(
                        text = info.discount,
                        modifier = Modifier
                            .padding(top = 12.dp, start = 20.dp)
                            .border(1.dp, onBackgroundDark, shape)
                            .background(surfaceBrightDark, shape)
                            .padding(start = 4.dp, end = 4.dp)
                            .clickable {
                                scope.launch {
                                    ticketDao.delete(info)
                                }
                            },
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        color = mainYellowColor,
                    )

                    Text(
                        text = "Válido até ${info.date}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                scope.launch {
                                    ticketDao.delete(info)
                                }
                            }
                            .padding(top = 10.dp, start = 24.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 14.sp,
                    )

                    Text(
                        text = "Cupom de desconto gerado em ${info.dateTicketGenerated} às ${info.timeTicketGenerated}hrs",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .clickable {
                                scope.launch {
                                    ticketDao.delete(info)
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