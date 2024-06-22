package viewmodel

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.DataSnapshot
import dev.gitlive.firebase.database.database
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class NotificationViewModel : ViewModel() {

    private var _listNotifications = MutableStateFlow(emptyList<RealTimeItemFirebase>())
    val listNotifications: StateFlow<List<RealTimeItemFirebase>> = _listNotifications

    var response: Flow<DataSnapshot>? = null

    init {
        getInfoFirebaseRealTimeDatabase()
    }

    private fun getInfoFirebaseRealTimeDatabase() {
        viewModelScope.launch {
            val firebase = Firebase.database
            var dataSnapshot: MutableList<DataSnapshot?> = mutableListOf()
            try {
                val response = firebase.reference("notifications").valueEvents.first().children
                val key = response.mapNotNull { it.value }


                println(response)
                println(key)



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