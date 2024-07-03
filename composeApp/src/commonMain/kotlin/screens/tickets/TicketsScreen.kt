package screens.tickets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import database.Ticket
import database.TicketDao
import kotlinx.coroutines.launch

@Composable
fun TicketScreen(ticketDao: TicketDao){
    val ticket by ticketDao.getAllTickets().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        val ticketList = listOf(
            Ticket(date = "11/05/1988", url = "wwwww"),
            Ticket(date = "12/06/1993", url = "wfwgcgcgcgc"),
            Ticket(date = "12/08/2093", url = "wwvhvhvhvhhvhvh"),
        )
        ticketList.forEach {
            ticketDao.upsert(it)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(ticket) { info ->
            Text(
                text = info.date,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        scope.launch {
                            ticketDao.delete(info)
                        }
                    }
                    .padding(16.dp)
            )
        }
    }
}