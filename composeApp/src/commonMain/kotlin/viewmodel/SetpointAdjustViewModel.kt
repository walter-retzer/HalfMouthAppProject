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
import network.NetworkRepository
import network.ResultNetwork
import util.ConstantsApp
import util.ConstantsApp.Companion.ERROR_CONNECTION_MESSAGE
import util.ConstantsApp.Companion.ERROR_UPDATE_MESSAGE

class SetpointAdjustViewModel(private val repository: NetworkRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<SetpointAdjustViewModelState>(SetpointAdjustViewModelState.Loading)
    val uiState: StateFlow<SetpointAdjustViewModelState> = _uiState.asStateFlow()
    private val konnection = Konnection.instance
    private val hasNetworkConnection = konnection.isConnected()

    private val _newUserSignInState = MutableStateFlow(NewUserContact())
    val newUserSignInState = _newUserSignInState.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _nameError = MutableStateFlow(false)
    val nameError = _nameError.asStateFlow()

    private val _phoneNumberError = MutableStateFlow(false)
    val phoneNumberError = _phoneNumberError.asStateFlow()

    init { updateValuesOnThingSpeak() }

    fun writeSetpoint() {
        viewModelScope.launch {
            val response = repository.updateFieldSetpointValue()
            _uiState.value = SetpointAdjustViewModelState.ErrorNetworkConnection(response.toString())
        }
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


sealed interface SetpointAdjustViewModelState {
    data class Error(val message: String) : SetpointAdjustViewModelState

    data class ErrorNetworkConnection(val message: String) : SetpointAdjustViewModelState

    data object Loading : SetpointAdjustViewModelState

    data class SuccessUpdateSetpoint (val feeds: MutableList<Feeds> = mutableListOf()) : SetpointAdjustViewModelState
}