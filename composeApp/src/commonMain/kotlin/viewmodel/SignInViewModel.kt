package viewmodel

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import util.ConstantsApp


class SignInViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NewUserContact())
    val uiState = _uiState.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _nameError = MutableStateFlow(false)
    val nameError = _nameError.asStateFlow()

    private val _phoneNumberError = MutableStateFlow(false)
    val phoneNumberError = _phoneNumberError.asStateFlow()


    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
        //reset error when the user types another character
        if (newValue.isNotBlank()) _emailError.value = false
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
        //reset error when the user types another character
        if (newValue.isNotBlank()) _passwordError.value = false
    }

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue) }
        //reset error when the user types another character
        if (newValue.isNotBlank()) _nameError.value = false
    }

    fun onPhoneNumberChange(newValue: String) {
        _uiState.update { it.copy(phoneNumber = newValue) }
        //reset error when the user types another character
        if (newValue.isNotBlank()) _phoneNumberError.value = false
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

    fun validatePhoneNumber(phoneNumber: String): String {
        if(phoneNumber.length != ConstantsApp.PHONE_MAX_NUMBER) {
            _phoneNumberError.value = true
        }
        return "Verifique o número digitado"
    }

    fun validateName(name: String): String {
        if(name.isEmpty()) {
            _nameError.value = true
        }
        return "Verifique o nome digitado"
    }
}

data class NewUserContact(
    val name: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val password: String = "",
)
