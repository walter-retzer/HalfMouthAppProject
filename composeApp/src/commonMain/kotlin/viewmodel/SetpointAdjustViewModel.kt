package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Feeds
import data.ThingSpeakResponse
import dev.tmapps.konnection.Konnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.NetworkRepository
import network.ResultNetwork
import util.ConstantsApp
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE

class SetpointAdjustViewModel(private val repository: NetworkRepository) : ViewModel() {

    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()
    private val range = -50.0..50.0

    private val _uiState = MutableStateFlow<SetpointAdjustViewModelState>(SetpointAdjustViewModelState.Loading)
    val uiState: StateFlow<SetpointAdjustViewModelState> = _uiState.asStateFlow()

    private val _setpointUiState = MutableStateFlow(SetpointsUiState())
    val setpointUiState: StateFlow<SetpointsUiState> = _setpointUiState.asStateFlow()

    init { updateValuesOnThingSpeak() }

    fun writeSetpoint(
        setpointField1: Double,
        setpointField2: Double,
        setpointField3: Double,
        setpointField4: Double,
        setpointField5: Double,
        setpointField6: Double,
        setpointField7: Double,
        setpointField8: Double,
    ) {
        viewModelScope.launch {
            val response = repository.updateFieldSetpointValue(
                setpointField1 = setpointField1,
                setpointField2 = setpointField2,
                setpointField3 = setpointField3,
                setpointField4 = setpointField4,
                setpointField5 = setpointField5,
                setpointField6 = setpointField6,
                setpointField7 = setpointField7,
                setpointField8 = setpointField8
            )
            _uiState.value = SetpointAdjustViewModelState.SuccessWriteSetpoint(response.toString())
        }
    }

    // 1) sanitiza (aceita dígitos, 1 separador . ou , e - só no começo)
    private fun sanitizeDecimal(raw: String): String {
        val cleaned = raw.filter { it.isDigit() || it == '.' || it == ',' || it == '-' }
        val sb = StringBuilder()
        var sepUsed = false
        var minusUsed = false

        cleaned.forEachIndexed { idx, c ->
            when (c) {
                '-' -> if (!minusUsed && idx == 0) { sb.append('-'); minusUsed = true }
                '.', ',' -> if (!sepUsed) { sb.append(c); sepUsed = true }
                else -> sb.append(c)
            }
        }
        return sb.toString()
    }

    // 2) parse seguro pt-BR (vírgula -> ponto)
    private fun parsePtBr(text: String): Double? {
        val normalized = text.replace(',', '.')
        if (normalized.isBlank() || normalized == "-" || normalized == "." || normalized == "-.") return null
        return normalized.toDoubleOrNull()
    }

    fun onTextChange(index: Int, raw: String) {
        val text = sanitizeDecimal(raw)
        val value = parsePtBr(text)

        _setpointUiState.update { s ->
            val newTexts = s.texts.toMutableList().apply { this[index] = text }
            val newValues = s.values.toMutableList().apply { this[index] = value }

            // erro só quando há número e está fora do range
            val newErrors = s.errors.toMutableList().apply {
                this[index] = (value != null && value !in range)
            }

            s.copy(texts = newTexts, values = newValues, errors = newErrors)
        }
    }

    fun validateAll(): Boolean {
        val values = _setpointUiState.value.values
        val newErrors = values.map { v -> v == null || v !in range }
        _setpointUiState.update { it.copy(errors = newErrors) }
        return newErrors.none { it }
    }

    fun errorMessage(): String = "Verifique o valor digitado! Valores aceitos: -50 a 50°C"

    private fun updateValuesOnThingSpeak() {
        if (!hasNetworkConnection){
            _uiState.value = SetpointAdjustViewModelState.ErrorNetworkConnection(ERROR_CONNECTION_MESSAGE)
            return
        }
        viewModelScope.launch {
            val responseApi = repository.getThingSpeakSetPointValues()
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
            _uiState.value = SetpointAdjustViewModelState.Error(ConstantsApp.ERROR_API_UPDATE_VALUE)
            return
        }

        listReceive.forEach { response ->
            response.feeds.first()?.field1?.let { onTextChange(0, it) }
            response.feeds.first()?.field2?.let { onTextChange(1, it) }
            response.feeds.first()?.field3?.let { onTextChange(2, it) }
            response.feeds.first()?.field4?.let { onTextChange(3, it) }
            response.feeds.first()?.field5?.let { onTextChange(4, it) }
            response.feeds.first()?.field6?.let { onTextChange(5, it)}
            response.feeds.first()?.field7?.let { onTextChange(6, it) }
            response.feeds.first()?.field8?.let { onTextChange(7, it) }

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
            _uiState.value = SetpointAdjustViewModelState.SuccessUpdateSetpoint(newFeedList)
        }
    }
}

data class SetpointsUiState(
    val texts: List<String> = List(8) { "" },
    val values: List<Double?> = List(8) { null },
    val errors: List<Boolean> = List(8) { false },
)


sealed interface SetpointAdjustViewModelState {
    data class Error(val message: String) : SetpointAdjustViewModelState

    data class ErrorNetworkConnection(val message: String) : SetpointAdjustViewModelState

    data object Loading : SetpointAdjustViewModelState

    data class SuccessUpdateSetpoint (val feeds: MutableList<Feeds> = mutableListOf()) : SetpointAdjustViewModelState

    data class SuccessWriteSetpoint (val message: String) : SetpointAdjustViewModelState
}