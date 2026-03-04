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

    private val _uiState = MutableStateFlow<SetpointAdjustViewModelState>(SetpointAdjustViewModelState.Loading)
    val uiState: StateFlow<SetpointAdjustViewModelState> = _uiState.asStateFlow()
    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()
    private val range = -50.0..50.0

    private val _newSetpointInState = MutableStateFlow(NewSetpoint())
    val newSetpointInState = _newSetpointInState.asStateFlow()

    private val _newSetpointField1Error = MutableStateFlow(false)
    val newSetpointField1Error = _newSetpointField1Error.asStateFlow()

    private val _newSetpointField2Error = MutableStateFlow(false)
    val newSetpointField2Error = _newSetpointField2Error.asStateFlow()

    private val _newSetpointField3Error = MutableStateFlow(false)
    val newSetpointField3Error = _newSetpointField3Error.asStateFlow()

    private val _newSetpointField4Error = MutableStateFlow(false)
    val newSetpointField4Error = _newSetpointField4Error.asStateFlow()

    private val _newSetpointField5Error = MutableStateFlow(false)
    val newSetpointField5Error = _newSetpointField5Error.asStateFlow()

    private val _newSetpointField6Error = MutableStateFlow(false)
    val newSetpointField6Error = _newSetpointField6Error.asStateFlow()

    private val _newSetpointField7Error = MutableStateFlow(false)
    val newSetpointField7Error = _newSetpointField7Error.asStateFlow()

    private val _newSetpointField8Error = MutableStateFlow(false)
    val newSetpointField8Error = _newSetpointField8Error.asStateFlow()

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
            _uiState.value = SetpointAdjustViewModelState.ErrorNetworkConnection(response.toString())
        }
    }

    fun onSetpointField1(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField1 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField1Error.value = false
    }

    fun onSetpointField2(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField2 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField2Error.value = false
    }

    fun onSetpointField3(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField3 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField3Error.value = false
    }

    fun onSetpointField4(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField4 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField4Error.value = false
    }

    fun onSetpointField5(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField5 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField5Error.value = false
    }

    fun onSetpointField6(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField6 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField6Error.value = false
    }

    fun onSetpointField7(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField7 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField7Error.value = false
    }

    fun onSetpointField8(newValue: Double) {
        _newSetpointInState.update { it.copy(setpointField8 = newValue) }
        //reset error when the user types another character
        if (newValue in range) _newSetpointField8Error.value = false
    }

    fun validateSetpointField1(setpointField1: Double): String {
        if(setpointField1 !in range) _newSetpointField1Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField2(setpointField2: Double): String {
        if(setpointField2 !in range) _newSetpointField2Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField3(setpointField3: Double): String {
        if(setpointField3 !in range) _newSetpointField3Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField4(setpointField4: Double): String {
        if(setpointField4 !in range) _newSetpointField4Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField5(setpointField5: Double): String {
        if(setpointField5 !in range) _newSetpointField5Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField6(setpointField6: Double): String {
        if(setpointField6 !in range) _newSetpointField6Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField7(setpointField7: Double): String {
        if(setpointField7 !in range) _newSetpointField7Error.value = true
        return "Verifique o valor digitado!"
    }

    fun validateSetpointField8(setpointField8: Double): String {
        if(setpointField8 !in range) _newSetpointField8Error.value = true
        return "Verifique o valor digitado!"
    }

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
            response.feeds.first()?.field1?.toDouble()?.let { onSetpointField1(it) }
            response.feeds.first()?.field2?.toDouble()?.let { onSetpointField2(it) }
            response.feeds.first()?.field3?.toDouble()?.let { onSetpointField3(it) }
            response.feeds.first()?.field4?.toDouble()?.let { onSetpointField4(it) }
            response.feeds.first()?.field5?.toDouble()?.let { onSetpointField5(it) }
            response.feeds.first()?.field6?.toDouble()?.let { onSetpointField6(it) }
            response.feeds.first()?.field7?.toDouble()?.let { onSetpointField7(it) }
            response.feeds.first()?.field8?.toDouble()?.let { onSetpointField8(it) }

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


data class NewSetpoint(
    val setpointField1: Double? = 0.0,
    val setpointField2: Double? = 0.0,
    val setpointField3: Double? = 0.0,
    val setpointField4: Double? = 0.0,
    val setpointField5: Double? = 0.0,
    val setpointField6: Double? = 0.0,
    val setpointField7: Double? = 0.0,
    val setpointField8: Double? = 0.0,
)

sealed interface SetpointAdjustViewModelState {
    data class Error(val message: String) : SetpointAdjustViewModelState

    data class ErrorNetworkConnection(val message: String) : SetpointAdjustViewModelState

    data object Loading : SetpointAdjustViewModelState

    data class SuccessUpdateSetpoint (val feeds: MutableList<Feeds> = mutableListOf()) : SetpointAdjustViewModelState
}