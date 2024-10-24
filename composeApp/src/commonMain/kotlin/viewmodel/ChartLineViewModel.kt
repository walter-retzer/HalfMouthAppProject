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
import network.NetworkRepository
import network.ResultNetwork
import network.chaintech.cmpcharts.common.model.Point
import secrets.BuildConfig
import util.ConstantsApp.Companion.ERROR_API_CHART_LINE
import util.ConstantsApp.Companion.ERROR_CHART_LINE
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE
import util.formattedAsTimeToChart

class ChartLineViewModel(private val repository: NetworkRepository) : ViewModel() {

    private val results = BuildConfig.RESULTS
    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()
    private val _uiState = MutableStateFlow<ChartLineViewState>(ChartLineViewState.Dashboard)
    val uiState: StateFlow<ChartLineViewState> = _uiState.asStateFlow()


    fun fetchThingSpeakChannelField(fieldId: String) {
        _uiState.value = ChartLineViewState.Loading
        if (!hasNetworkConnection) {
            _uiState.value = ChartLineViewState.ErrorNetworkConnection(ERROR_CONNECTION_MESSAGE)
            return
        }
        viewModelScope.launch {
            val responseApi = repository.getThingSpeakChannelFeed(fieldId = fieldId, results = results)
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

        try {
            val channelFeed = mutableListOf<FeedsThingSpeak?>()
            listReceive.forEach {
                it.feeds.mapNotNull { feeds ->
                    channelFeed.add(feeds)
                }
            }

            val listOfDate = mutableListOf<String>()
            val listOfValues = mutableListOf<Double>()
            val listOfPointsToPlot = mutableListOf<Point>()

            channelFeed.mapNotNull {
                when (fieldId) {
                    "1" -> {
                        if (it?.field1 != null) {
                            listOfValues.add(it.field1.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field1.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "2" -> {
                        if (it?.field2 != null) {
                            listOfValues.add(it.field2.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field2.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "3" -> {
                        if (it?.field3 != null) {
                            listOfValues.add(it.field3.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field3.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "4" -> {
                        if (it?.field4 != null) {
                            listOfValues.add(it.field4.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field4.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "5" -> {
                        if (it?.field5 != null) {
                            listOfValues.add(it.field5.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field5.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "6" -> {
                        if (it?.field6 != null) {
                            listOfValues.add(it.field6.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field6.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "7" -> {
                        if (it?.field7 != null) {
                            listOfValues.add(it.field7.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field7.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    "8" -> {
                        if (it?.field8 != null) {
                            listOfValues.add(it.field8.toDouble())
                            listOfDate.add(it.created_at.formattedAsTimeToChart())
                            listOfPointsToPlot.add(
                                Point(
                                    it.entry_id.toFloat(),
                                    it.field8.toFloat(),
                                    it.created_at.formattedAsTimeToChart()
                                )
                            )
                        } else return@mapNotNull
                    }

                    else -> emptyList<Double>()
                }
            }

            if (listOfValues.isEmpty() || listOfValues.size != listOfDate.size) _uiState.value =
                ChartLineViewState.Error(ERROR_CHART_LINE)
            else _uiState.value = ChartLineViewState.Success(listOfPointsToPlot)
        } catch (e: Exception) {
            _uiState.value = ChartLineViewState.Error(ERROR_API_CHART_LINE)
            println(e)
        }
    }
}

sealed interface ChartLineViewState {
    data object Dashboard : ChartLineViewState
    data class Success(val listOfPointsToPlot: List<Point>) : ChartLineViewState
    data class ErrorNetworkConnection(val message: String) : ChartLineViewState
    data class Error(val message: String) : ChartLineViewState
    data object Loading : ChartLineViewState
}
