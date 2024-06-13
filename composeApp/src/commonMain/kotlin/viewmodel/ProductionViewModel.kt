package viewmodel

import data.ThingSpeakResponse
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import network.ApiService
import network.ResultType


class ProductionViewModel : ViewModel() {

    private val service = ApiService.create()
    private val results = "2"

    var res: ThingSpeakResponse = ThingSpeakResponse(
        channel = null,
        feeds = emptyList()
    )

    private val _uiState = MutableStateFlow(
        ThingSpeakResponse(
            channel = null,
            feeds = emptyList()
        )
    )
    val uiState = _uiState.asStateFlow()

    fun loadFirstShimmer() {
        viewModelScope.launch {
            val resp =   service.getThingSpeakValues(results = results)
            println(resp)
            res = handleResult(resp)
            _uiState.value = res
        }
    }

    private fun handleResult(result: ResultType<ThingSpeakResponse>): ThingSpeakResponse {
        return when (result) {
            is ResultType.Failure -> throw result.exception
            is ResultType.Success -> result.data
        }
    }
}
