package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init { updateValuesOnThingSpeak() }

    private fun updateValuesOnThingSpeak() {
        if (!hasNetworkConnection){
            _uiState.value = SetpointAdjustViewModelState.ErrorNetworkConnection(ERROR_CONNECTION_MESSAGE)
            return
        }
        viewModelScope.launch {
            when (val responseApi = repository.updateFieldValue()) {
                is ResultNetwork.Failure -> {
                    _uiState.value = SetpointAdjustViewModelState.ErrorNetworkConnection(ERROR_UPDATE_MESSAGE)
                }
                is ResultNetwork.Success -> {
                    if (responseApi.data == 0) _uiState.value = SetpointAdjustViewModelState.Error(ConstantsApp.ERROR_API_UPDATE_VALUE)
                    else _uiState.value = SetpointAdjustViewModelState.SuccessUpdateValues(responseApi.data)
                }
            }
        }
    }
}


sealed interface SetpointAdjustViewModelState {
    data class Error(val message: String) : SetpointAdjustViewModelState

    data class ErrorNetworkConnection(val message: String) : SetpointAdjustViewModelState

    data object Loading : SetpointAdjustViewModelState

    data class SuccessUpdateValues (val value: Int) : SetpointAdjustViewModelState
}