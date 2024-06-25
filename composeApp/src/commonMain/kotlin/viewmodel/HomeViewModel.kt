package viewmodel

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class HomeViewModel : ViewModel() {

    private val firebase = Firebase.database

    private val _isNotificationActivated = MutableStateFlow(false)
    val isNotificationActivated = _isNotificationActivated.asStateFlow()

    private val _notificationDate = MutableStateFlow("")
    val notificationDate = _notificationDate.asStateFlow()

    private val _notificationMessage = MutableStateFlow("")
    val notificationMessage = _notificationMessage.asStateFlow()

    init {
        getInfoFirebaseRealTimeDatabase()
    }

    private fun getInfoFirebaseRealTimeDatabase() {
        viewModelScope.launch {
            try {
                val notification = firebase.reference("notifications").valueEvents.first()
                val isActivated: Any = notification.child("list").child("isActivated").value ?: false
                val date = notification.child("list").child("data").value ?: ""
                val message = notification.child("list").child("message").value ?: ""

                _isNotificationActivated.value = isActivated as Boolean
                _notificationDate.value = date as String
                _notificationMessage.value = message as String
            } catch (e: Exception) {
                println(" Errror $e")
            }
        }
    }
}

@Serializable
data class RealTimeItemFirebase(
    var data: String? = "",
    var message: String? = "",
    var index: String? = ""
)
