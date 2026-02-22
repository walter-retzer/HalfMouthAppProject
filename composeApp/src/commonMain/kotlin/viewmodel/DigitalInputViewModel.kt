package viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Feeds
import data.ThingSpeakResponse
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import network.NetworkRepository
import network.ResultNetwork
import util.ConstantsApp
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE


class DigitalInputViewModel(private val repository: NetworkRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DigitalInputViewModelState>(DigitalInputViewModelState.Loading)
    val uiState: StateFlow<DigitalInputViewModelState> = _uiState.asStateFlow()
    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()

    init {
        fetchThingSpeakInformation()
    }

    private fun fetchThingSpeakInformation() {
        if (!hasNetworkConnection){
            _uiState.value = DigitalInputViewModelState.ErrorNetworkConnection(ERROR_CONNECTION_MESSAGE)
            return
        }
        viewModelScope.launch {
            val responseApi = repository.getThingSpeakDigitalInputValues()
            val thingSpeakResponse = handleResponseApi(responseApi)
            adjustValuesInListFeed(mutableListOf(thingSpeakResponse))
        }
    }


    private fun handleResponseApi(responseApi: ResultNetwork<ThingSpeakResponse>): ThingSpeakResponse {
        return when (responseApi) {
            is ResultNetwork.Failure -> ThingSpeakResponse(null, emptyList())
            is ResultNetwork.Success -> responseApi.data
        }
    }


    private fun adjustValuesInListFeed(listReceive: List<ThingSpeakResponse>){

        if (listReceive.first().feeds.isEmpty() || listReceive.first().channel == null) {
            _uiState.value = DigitalInputViewModelState.Error(ConstantsApp.ERROR_API)
            return
        }
        listReceive.forEach { response ->
            val newFeedList = mutableListOf(
                Feeds(
                    fieldName = response.channel?.field1,
                    fieldValue = response.feeds.first()?.field1,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field2,
                    fieldValue = response.feeds.first()?.field2,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field3,
                    fieldValue = response.feeds.first()?.field3,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field4,
                    fieldValue = response.feeds.first()?.field4,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field5,
                    fieldValue = response.feeds.first()?.field5,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field6,
                    fieldValue = response.feeds.first()?.field6,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field7,
                    fieldValue = response.feeds.first()?.field7,
                    fieldData = response.feeds.first()?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field8,
                    fieldValue = response.feeds.first()?.field8,
                    fieldData = response.feeds.first()?.created_at
                ),
            )
            _uiState.value = DigitalInputViewModelState.Dashboard(newFeedList)
        }
    }
}


sealed interface DigitalInputViewModelState {
    data class Dashboard(val sensorsValues: MutableList<Feeds> = mutableListOf()) : DigitalInputViewModelState

    data class Error(val message: String) : DigitalInputViewModelState

    data class ErrorNetworkConnection(val message: String) : DigitalInputViewModelState

    data object Loading : DigitalInputViewModelState
}
