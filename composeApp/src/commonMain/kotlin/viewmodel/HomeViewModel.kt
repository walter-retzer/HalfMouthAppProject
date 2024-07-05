package viewmodel

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import util.ConstantsApp

class HomeViewModel : ViewModel() {

    private val firebase = Firebase.database
    private val _notificationMessage = MutableStateFlow(ConstantsApp.DEFAULT_NOTIFICATION)
    val notificationMessage = _notificationMessage.asStateFlow()

    init {
        getInfoFirebaseRealTimeDatabase()
    }

    private fun getInfoFirebaseRealTimeDatabase() {
        viewModelScope.launch {
            try {
                val notification = firebase.reference("halfmouth").valueEvents.first()
                val message = notification.child("information").child("message").value
                    ?: ConstantsApp.DEFAULT_NOTIFICATION
                _notificationMessage.value = message as String
            } catch (e: Exception) {
                println(" Errror $e")
            }
        }
    }
}
