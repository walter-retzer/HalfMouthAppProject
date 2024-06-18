package viewmodel

import com.russhwolf.settings.Settings
import data.UserPreferences
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.ConstantsApp

class LoginUserViewModel : ViewModel() {

    private val settingsPref = Settings()
    private val authService = Firebase.auth
    private var firebaseUser: FirebaseUser? = null

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _userLoginState = MutableStateFlow(LoginUserData())
    val userLoginState = _userLoginState.asStateFlow()

    private val _uiState = MutableStateFlow<LoginUserViewState>(LoginUserViewState.Dashboard)
    val uiState: StateFlow<LoginUserViewState> = _uiState.asStateFlow()

    fun onSignIn(email: String, password: String) {
        _uiState.value = LoginUserViewState.Loading
        viewModelScope.launch {
            try {
                authService.signInWithEmailAndPassword(
                    email = email,
                    password = password
                )
                delay(3000L)
                firebaseUser = authService.currentUser
                settingsPref.putString(UserPreferences.UID, firebaseUser?.uid.toString())
                if (firebaseUser != null) _uiState.value =
                    LoginUserViewState.Success(ConstantsApp.SUCCESS_SIGN_IN)
                else _uiState.value = LoginUserViewState.Error(ConstantsApp.ERROR_SIGN_IN)
            } catch (e: Exception) {
                println(e)
                _uiState.value = LoginUserViewState.Error(ConstantsApp.ERROR_SIGN_IN)
            }
        }
    }


    fun onEmailChange(newValue: String) {
        _userLoginState.update { it.copy(email = newValue) }
        //reset error when the user types another character
        if (newValue.isNotBlank()) _emailError.value = false
    }

    fun onPasswordChange(newValue: String) {
        _userLoginState.update { it.copy(password = newValue) }
        //reset error when the user types another character
        if (newValue.isNotBlank()) _passwordError.value = false
    }

    fun validateEmail(email: String): String {
        val emailRegex = Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        if(!emailRegex.matches(email)) {
            _emailError.value = true
        }
        return "Verifique o email digitado"
    }

    fun validatePassword(password: String): String {
        if(password.length != ConstantsApp.PASSWORD_MAX_NUMBER) {
            _passwordError.value = true
        }
        return "A senha deve conter 6 dígitos"
    }
}

data class LoginUserData(
    val email: String = "",
    val password: String = "",
)

sealed interface LoginUserViewState {
    data object Loading : LoginUserViewState
    data object Dashboard : LoginUserViewState
    data class Success(val message: String) : LoginUserViewState
    data class Error(val message: String) : LoginUserViewState

}
