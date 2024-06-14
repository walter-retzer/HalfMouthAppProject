package viewmodel

import data.Feeds
import data.ThingSpeakResponse
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import network.ApiService
import network.ResultNetwork


class ProductionViewModel : ViewModel() {

    private val service = ApiService.create()
    private val results = "2"
    private val _uiState1 = MutableStateFlow<ProductionViewState>(ProductionViewState.Loading)
    val uiState1: StateFlow<ProductionViewState> = _uiState1.asStateFlow()

    init {
        fetchThingSpeakInformation()
    }

    private fun fetchThingSpeakInformation() {
        viewModelScope.launch {
            val responseApi = service.getThingSpeakValues(results = results)
            val thingSpeakResponse = handleResponseApi(responseApi)
            adjustValuesInListFeed(mutableListOf(thingSpeakResponse))
        }
    }


    private fun handleResponseApi(responseApi: ResultNetwork<ThingSpeakResponse>): ThingSpeakResponse {
        return when (responseApi) {
            is ResultNetwork.Failure -> throw responseApi.exception
            is ResultNetwork.Success -> responseApi.data
        }
    }


    private fun adjustValuesInListFeed(listReceive: List<ThingSpeakResponse>): MutableList<Feeds> {
        var newFeedList = mutableListOf(Feeds())
        listReceive.forEach { response ->
            val i1 = if (response.feeds.first()?.field1 == null) 1 else 0
            val i2 = if (response.feeds.first()?.field5 == null) 1 else 0
            newFeedList = mutableListOf(
                Feeds(
                    fieldName = response.channel?.field1,
                    fieldValue = response.feeds[i1]?.field1,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field2,
                    fieldValue = response.feeds[i1]?.field2,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field3,
                    fieldValue = response.feeds[i1]?.field3,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field4,
                    fieldValue = response.feeds[i1]?.field4,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field5,
                    fieldValue = response.feeds[i2]?.field5,
                    fieldData = response.feeds[i2]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field6,
                    fieldValue = response.feeds[i1]?.field6,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field7,
                    fieldValue = response.feeds[i1]?.field7,
                    fieldData = response.feeds[i1]?.created_at
                ),
                Feeds(
                    fieldName = response.channel?.field8,
                    fieldValue = response.feeds[i2]?.field8,
                    fieldData = response.feeds[i2]?.created_at
                ),
            )
            _uiState1.value = ProductionViewState.Dashboard(newFeedList)
        }
        return newFeedList
    }
}


sealed interface ProductionViewState {
    data class Dashboard(
        val sensorsValues: MutableList<Feeds> = mutableListOf(),
    ) : ProductionViewState

    data class Error(val message: String) : ProductionViewState

    data object Loading : ProductionViewState
}
