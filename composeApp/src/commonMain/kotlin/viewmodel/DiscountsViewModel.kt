package viewmodel

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import util.ConstantsApp
import util.ConstantsApp.Companion.MESSAGE_ERROR_DISCOUNT

class DiscountsViewModel : ViewModel() {

    private val firebase = Firebase.database
    private var urlDiscount: String = ""
    private var discountMessage: String = ""
    private val _uiState = MutableStateFlow<DiscountsViewState>(DiscountsViewState.Dashboard)
    val uiState: StateFlow<DiscountsViewState> = _uiState.asStateFlow()

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

                urlDiscount = message as String
                discountMessage = discount as String
            } catch (e: Exception) {
                println(" Errror $e")
            }
        }
    }

    fun getSuccess(qrCodeURL: String) {
        if (urlDiscount == qrCodeURL) _uiState.value = DiscountsViewState.Success(discountMessage)
        else _uiState.value = DiscountsViewState.Error(MESSAGE_ERROR_DISCOUNT)
    }

    fun getError(error: String) {
        _uiState.value = DiscountsViewState.Error(MESSAGE_ERROR_DISCOUNT)
        println(error)
    }

    fun getRefreshScan() {
        _uiState.value = DiscountsViewState.Dashboard
    }
}

sealed interface DiscountsViewState {
    data object Dashboard : DiscountsViewState
    data class Success(val message: String) : DiscountsViewState
    data class Error(val message: String) : DiscountsViewState
}
