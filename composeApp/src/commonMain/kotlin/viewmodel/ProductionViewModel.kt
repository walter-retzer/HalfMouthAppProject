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
import network.ApiService
import network.ResultNetwork
import util.ConstantsApp
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE


class ProductionViewModel : ViewModel() {

    private val service = ApiService.create()
    private val results = "2"
    private val _uiState = MutableStateFlow<ProductionViewState>(ProductionViewState.Loading)
    val uiState: StateFlow<ProductionViewState> = _uiState.asStateFlow()
    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()

    init {
        fetchThingSpeakInformation()
    }

    private fun fetchThingSpeakInformation() {
        if (!hasNetworkConnection){
            _uiState.value = ProductionViewState.ErrorNetworkConnection(ERROR_CONNECTION_MESSAGE)
            return
        }
        viewModelScope.launch {
            val responseApi = service.getThingSpeakValues(results = results)
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
            _uiState.value = ProductionViewState.Error(ConstantsApp.ERROR_API)
            return
        }

        listReceive.forEach { response ->
            val i1 = if (response.feeds.first()?.field1 == null) 1 else 0
            val i2 = if (response.feeds.first()?.field5 == null) 1 else 0
            val newFeedList = mutableListOf(
                Feeds(
                    fieldId = "1",
                    fieldType = "TEMPERATURA ",
                    fieldName = response.channel?.field1,
                    fieldValue = response.feeds[i1]?.field1,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldId = "2",
                    fieldType = "TEMPERATURA ",
                    fieldName = response.channel?.field2,
                    fieldValue = response.feeds[i1]?.field2,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldId = "3",
                    fieldType = "TEMPERATURA ",
                    fieldName = response.channel?.field3,
                    fieldValue = response.feeds[i1]?.field3,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldId = "4",
                    fieldType = "TEMPERATURA ",
                    fieldName = response.channel?.field4,
                    fieldValue = response.feeds[i1]?.field4,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldId = "5",
                    fieldType = "TEMPERATURA ",
                    fieldName = response.channel?.field5,
                    fieldValue = response.feeds[i2]?.field5,
                    fieldData = response.feeds[i2]?.created_at
                ),
                Feeds(
                    fieldId = "6",
                    fieldType = "RESFRIAMENTO  ",
                    fieldName = response.channel?.field6,
                    fieldValue = response.feeds[i1]?.field6,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldId = "7",
                    fieldType = "MBO-001 ",
                    fieldName = response.channel?.field7,
                    fieldValue = response.feeds[i1]?.field7,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldId = "8",
                    fieldType = "RESFRIAMENTO  ",
                    fieldName = response.channel?.field8,
                    fieldValue = response.feeds[i2]?.field8,
                    fieldData = response.feeds[i2]?.created_at
                ),
            )
            _uiState.value = ProductionViewState.Dashboard(newFeedList)
        }
    }
}


sealed interface ProductionViewState {
    data class Dashboard(val sensorsValues: MutableList<Feeds> = mutableListOf()) : ProductionViewState

    data class Error(val message: String) : ProductionViewState

    data class ErrorNetworkConnection(val message: String) : ProductionViewState

    data object Loading : ProductionViewState
}
