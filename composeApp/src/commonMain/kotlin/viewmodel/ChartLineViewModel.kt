package viewmodel

import data.FeedsThingSpeak
import data.ThingSpeakResponse
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import network.ApiService
import network.ResultNetwork
import util.formattedAsTimeToChart

class ChartLineViewModel : ViewModel() {

    private val service = ApiService.create()
    private val results = "5"
    private val _uiState = MutableStateFlow<ChartLineViewState>(ChartLineViewState.Dashboard)
    val uiState: StateFlow<ChartLineViewState> = _uiState.asStateFlow()

    fun fetchThingSpeakChannelField() {
        _uiState.value = ChartLineViewState.Loading
        viewModelScope.launch {
            val responseApi = service.getThingSpeakChannelFeed(field = "5", results = results)
            val thingSpeakResponse = handleResponseApi(responseApi)
            adjustValuesChannelField(mutableListOf(thingSpeakResponse))
        }
    }


    private fun handleResponseApi(responseApi: ResultNetwork<ThingSpeakResponse>): ThingSpeakResponse {
        return when (responseApi) {
            is ResultNetwork.Failure -> ThingSpeakResponse(null, emptyList())
            is ResultNetwork.Success -> responseApi.data
        }
    }

    private fun adjustValuesChannelField(listReceive: List<ThingSpeakResponse>) {
        try{
            val channelFeed = mutableListOf<FeedsThingSpeak?>()
            listReceive.forEach {
                it.feeds.mapNotNull { feeds->
                    channelFeed.add(feeds)
                }
            }

            val listOfValues = channelFeed.mapNotNull {
                it?.field5.toString().toDouble()
            }

            val listOfDate = channelFeed.mapNotNull {
                it?.created_at.toString().formattedAsTimeToChart()
            }

            _uiState.value = ChartLineViewState.Success(listOfValues, listOfDate)
        } catch(e: Exception){
            _uiState.value = ChartLineViewState.Error("Falha")
            println(e)
        }
    }
}

sealed interface ChartLineViewState {
    data object Dashboard : ChartLineViewState

    data class Success(val listOfValues:  List<Double>, val listOfDate: List<String>) : ChartLineViewState

    data class Error(val message: String) : ChartLineViewState

    data object Loading : ChartLineViewState
}