package viewmodel

import database.Ticket
import database.TicketDao
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import util.ConstantsApp
import util.ConstantsApp.Companion.MESSAGE_ERROR_DISCOUNT
import util.ConstantsApp.Companion.TICKET_ERROR_INSERT


class DiscountsViewModel(private val ticketDao: TicketDao) : ViewModel() {

    private val firebase = Firebase.database
    private val _uiState = MutableStateFlow<DiscountsViewState>(DiscountsViewState.Dashboard)
    val uiState: StateFlow<DiscountsViewState> = _uiState.asStateFlow()
    private var urlDiscount: String = ""
    private var discountMessage: String = ""
    private var expirationDate: String = ""

    init {
        getUrlOnFirebaseDatabase()
    }

    private fun getUrlOnFirebaseDatabase() {
        viewModelScope.launch {
            try {
                val notification = firebase.reference("halfmouth").valueEvents.first()

                val message = notification.child("information").child("url").value
                    ?: ConstantsApp.MESSAGE_DEFAULT_NOTIFICATION

                val discount = notification.child("information").child("discount").value
                    ?: ConstantsApp.MESSAGE_DEFAULT_DISCOUNT

                val expiration = notification.child("information").child("expirationDate").value
                    ?: "##/##/####"

                urlDiscount = message as String
                discountMessage = discount as String
                expirationDate = expiration as String
            } catch (e: Exception) {
                println(" Errror $e")
            }
        }
    }

    fun getSuccess(qrCodeURL: String) {
        _uiState.value = DiscountsViewState.Loading
        val now: Instant = Clock.System.now()
        val today: LocalDate = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val expiration = "${today.dayOfMonth}/${today.monthNumber}/${today.year}"
        val expirationMessage = "Parabéns, o seu cupom de desconto é valido até o dia $expiration"
        val hour: LocalTime = now.toLocalDateTime(TimeZone.currentSystemDefault()).time
        val timeItWasCreated = "${hour.hour}:${hour.minute}:${hour.second}"

        val ticket = Ticket(
            date = expirationDate,
            discount = discountMessage,
            timeTicketGenerated = timeItWasCreated,
            dateTicketGenerated = expiration,
        )
        viewModelScope.launch {
            delay(2000L)
            if (urlDiscount == qrCodeURL){
                try{
                    ticketDao.upsert(ticket)
                    _uiState.value = DiscountsViewState.Success(discountMessage, expirationMessage )
                } catch(e: Exception){
                    _uiState.value = DiscountsViewState.Error(TICKET_ERROR_INSERT)
                    println(e)
                }
            }
            else _uiState.value = DiscountsViewState.Error(MESSAGE_ERROR_DISCOUNT)
        }
    }

    fun getError(error: String) {
        _uiState.value = DiscountsViewState.Loading
        viewModelScope.launch {
            delay(2000L)
            _uiState.value = DiscountsViewState.Error(MESSAGE_ERROR_DISCOUNT)
            println(error)
        }
    }

    fun getRefreshScan() {
        _uiState.value = DiscountsViewState.Dashboard
    }
}

sealed interface DiscountsViewState {
    data object Dashboard : DiscountsViewState
    data object Loading : DiscountsViewState
    data class Success(val message: String, val expirationMessage: String) : DiscountsViewState
    data class Error(val message: String) : DiscountsViewState
}
