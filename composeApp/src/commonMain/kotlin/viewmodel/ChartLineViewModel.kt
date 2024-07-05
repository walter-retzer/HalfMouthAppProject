package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.FeedsThingSpeak
import data.ThingSpeakResponse
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import network.ApiServiceImpl
import network.ResultNetwork
import util.ConstantsApp.Companion.ERROR_API_CHART_LINE
import util.ConstantsApp.Companion.ERROR_CHART_LINE
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE
import util.formattedAsTimeToChart

class ChartLineViewModel(private var service: ApiServiceImpl) : ViewModel() {

    private val results = "5"
    private val _uiState = MutableStateFlow<ChartLineViewState>(ChartLineViewState.Dashboard)
    val uiState: StateFlow<ChartLineViewState> = _uiState.asStateFlow()
    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()


    fun fetchThingSpeakChannelField(fieldId: String) {
        _uiState.value = ChartLineViewState.Loading
        if (!hasNetworkConnection){
            _uiState.value = ChartLineViewState.ErrorNetworkConnection(ERROR_CONNECTION_MESSAGE)
            return
        }
        viewModelScope.launch {
            val responseApi = service.getThingSpeakChannelFeed(fieldId = fieldId, results = results)
            val thingSpeakResponse = handleResponseApi(responseApi)
            adjustValuesChannelField(mutableListOf(thingSpeakResponse), fieldId)
        }
    }


    private fun handleResponseApi(responseApi: ResultNetwork<ThingSpeakResponse>): ThingSpeakResponse {
        return when (responseApi) {
            is ResultNetwork.Failure -> ThingSpeakResponse(null, emptyList())
            is ResultNetwork.Success -> responseApi.data
        }
    }

    private fun adjustValuesChannelField(listReceive: List<ThingSpeakResponse>, fieldId: String) {

        if (listReceive.first().feeds.isEmpty() || listReceive.first().channel == null) {
            _uiState.value = ChartLineViewState.Error(ERROR_API_CHART_LINE)
            return
        }

        try{
            val channelFeed = mutableListOf<FeedsThingSpeak?>()
            listReceive.forEach {
                it.feeds.mapNotNull { feeds->
                    channelFeed.add(feeds)
                }
            }

            val listOfFields = channelFeed.mapNotNull {
                when (fieldId) {
                    "1" -> it?.field1?.toDouble()
                    "2" -> it?.field2?.toDouble()
                    "3" -> it?.field3?.toDouble()
                    "4" -> it?.field4?.toDouble()
                    "5" -> it?.field5?.toDouble()
                    "6" -> it?.field6?.toDouble()
                    "7" -> it?.field7?.toDouble()
                    "8" -> it?.field8?.toDouble()
                    else -> emptyList<Double>()
                }
            }

            val listOfValues = listOfFields.map {
                it.toString().toDouble()
            }

            val listOfDate = channelFeed.mapNotNull {
                it?.created_at.toString().formattedAsTimeToChart()
            }

            if (listOfValues.isEmpty() || listOfValues.size != listOfDate.size) _uiState.value =
                ChartLineViewState.Error(ERROR_CHART_LINE)
            else _uiState.value = ChartLineViewState.Success(listOfValues, listOfDate)
        } catch(e: Exception){
            _uiState.value = ChartLineViewState.Error(ERROR_API_CHART_LINE)
            println(e)
        }
    }
}

sealed interface ChartLineViewState {
    data object Dashboard : ChartLineViewState

    data class Success(val listOfValues:  List<Double>, val listOfDate: List<String>) : ChartLineViewState

    data class ErrorNetworkConnection(val message: String) : ChartLineViewState

    data class Error(val message: String) : ChartLineViewState

    data object Loading : ChartLineViewState
}
