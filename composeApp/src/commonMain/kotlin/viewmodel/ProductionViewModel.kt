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
    var newFeedList = mutableListOf(Feeds())

    private val _uiState = MutableStateFlow(
        ThingSpeakResponse(
            channel = null,
            feeds = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiState1 = MutableStateFlow<ProductionViewState>(ProductionViewState.Loading)
    val uiState1: StateFlow<ProductionViewState> = _uiState1.asStateFlow()

    //val listFeedReceive1: MutableList<Feeds>

//    private val _uiStateFeed = MutableStateFlow(MutableList( Feeds()))
//    val uiStateFeed = _uiStateFeed.asStateFlow()

    init {
        loadFirstShimmer()
    }

    fun loadFirstShimmer() {
        viewModelScope.launch {
            val response = service.getThingSpeakValues(results = results)
            _uiState.value = handleResult(response)
            adjustValuesInListFeed(mutableListOf(uiState.value))
        }
    }

    private fun handleResult(result: ResultNetwork<ThingSpeakResponse>): ThingSpeakResponse {
        return when (result) {
            is ResultNetwork.Failure -> throw result.exception
            is ResultNetwork.Success -> result.data
        }
    }

    private fun adjustValuesInListFeed(listReceive: List<ThingSpeakResponse>): MutableList<Feeds> {
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
