package viewmodel

import data.FeedsThingSpeak
import data.ThingSpeakResponse
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import network.ApiService
import network.ResultNetwork
import util.ConstantsApp.Companion.ERROR_API_CHART_LINE
import util.ConstantsApp.Companion.ERROR_CHART_LINE
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE
import util.formattedAsTimeToChart

class ChartLineViewModel : ViewModel() {

    private val service = ApiService.create()
    private val results = "10"
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

            val listOfDate = mutableListOf<String>()
            val listOfValues = mutableListOf<Double>()

            channelFeed.mapNotNull {
                when (fieldId) {
                    "1" -> {
                        if (it?.field1 != null) {
                            listOfValues.add(it.field1.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "2" -> {
                        if (it?.field2 != null) {
                            listOfValues.add(it.field2.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "3" -> {
                        if (it?.field3 != null) {
                            listOfValues.add(it.field3.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "4" -> {
                        if (it?.field4 != null) {
                            listOfValues.add(it.field4.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "5" -> {
                        if (it?.field5 != null) {
                            listOfValues.add(it.field5.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "6" -> {
                        if (it?.field6 != null) {
                            listOfValues.add(it.field6.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "7" -> {
                        if (it?.field7 != null) {
                            listOfValues.add(it.field7.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    "8" -> {
                        if (it?.field8 != null) {
                            listOfValues.add(it.field8.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                        } else return@mapNotNull
                    }

                    else -> emptyList<Double>()
                }
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
