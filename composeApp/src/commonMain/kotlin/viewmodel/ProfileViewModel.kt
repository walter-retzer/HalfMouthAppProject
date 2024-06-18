package viewmodel

import com.russhwolf.settings.Settings
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.ConstantsApp


class ProfileViewModel : ViewModel() {
    private val authService = Firebase.auth
    private val settingsPref = Settings()
    private val _uiState = MutableStateFlow<ProfileViewState>(ProfileViewState.Dashboard)
    val uiState: StateFlow<ProfileViewState> = _uiState.asStateFlow()

    fun signOut() {
        _uiState.value = ProfileViewState.Loading
        viewModelScope.launch {
            try {
                authService.signOut()
                settingsPref.clear()
                delay(3000L)
                _uiState.value = ProfileViewState.Success
            } catch (e: Exception) {
                println(e)
                _uiState.value = ProfileViewState.Error(ConstantsApp.ERROR_SIGN_OUT)
            }
        }
    }

    fun deleteUser() {
        _uiState.value = ProfileViewState.Loading
        viewModelScope.launch {
            try {
                authService.currentUser?.delete()
                authService.signOut()
                settingsPref.clear()
                _uiState.value = ProfileViewState.SuccessClearInfoUser(ConstantsApp.SUCCESS_DELETE_ACCOUNT)
                delay(3000L)
                _uiState.value = ProfileViewState.Success
            } catch (e: Exception) {
                println(e)
                _uiState.value = ProfileViewState.Error(ConstantsApp.ERROR_DELETE_ACCOUNT)
            }
        }
    }
}

sealed interface ProfileViewState {
    data object Loading : ProfileViewState
    data object Dashboard : ProfileViewState
    data object Success : ProfileViewState
    data class SuccessClearInfoUser(val message: String) : ProfileViewState
    data class Error(val message: String) : ProfileViewState

}
