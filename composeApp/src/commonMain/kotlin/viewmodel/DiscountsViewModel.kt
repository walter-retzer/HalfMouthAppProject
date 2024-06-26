package viewmodel

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import util.ConstantsApp

class DiscountsViewModel : ViewModel() {

    private val firebase = Firebase.database
    private val _urlDiscount = MutableStateFlow(ConstantsApp.MESSAGE_DEFAULT_NOTIFICATION)
    val urlDiscount = _urlDiscount.asStateFlow()

    init {
        getInfoFirebaseRealTimeDatabase()
    }

    private fun getInfoFirebaseRealTimeDatabase() {
        viewModelScope.launch {
            try {
                val notification = firebase.reference("notifications").valueEvents.first()
                val message = notification.child("list").child("url").value
                    ?: ConstantsApp.MESSAGE_DEFAULT_NOTIFICATION
                _urlDiscount.value = message as String
            } catch (e: Exception) {
                println(" Errror $e")
            }
        }
    }
}
