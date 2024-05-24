package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.icerock.moko.mvvm.viewmodel.ViewModel


class SignInViewModel : ViewModel() {

    var newContact: NewUserContact by mutableStateOf(
        NewUserContact(
            name = "",
            phoneNumber = "",
            email = "",
            password = "",
            confirmPassword = ""
        )
    )
        private set



    fun onEvent(event: SignInContactEvent) {
        when (event) {
            is SignInContactEvent.OnFirstNameChanged -> {
                newContact = newContact.copy(
                    name = event.value
                )
            }

            is SignInContactEvent.OnPhoneNumberChanged -> {
                newContact = newContact.copy(
                    phoneNumber = event.value
                )
            }

            is SignInContactEvent.OnEmailChanged -> {
                newContact = newContact.copy(
                    email = event.value
                )
            }

            is SignInContactEvent.OnPasswordChanged -> {
                newContact = newContact.copy(
                    password = event.value
                )
            }

            is SignInContactEvent.OnConfirmPasswordChanged -> {
                newContact = newContact.copy(
                    confirmPassword = event.value
                )
            }

            SignInContactEvent.SaveContact -> TODO()
        }
    }
}

        sealed interface SignInContactEvent {
            data class OnFirstNameChanged(val value: String): SignInContactEvent
            data class OnPasswordChanged(val value: String): SignInContactEvent
            data class OnConfirmPasswordChanged(val value: String): SignInContactEvent
            data class OnEmailChanged(val value: String): SignInContactEvent
            data class OnPhoneNumberChanged(val value: String): SignInContactEvent
            object SaveContact: SignInContactEvent
        }

data class NewUserContact(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
)

data class SignInContactErrorState(
    val nameError: String? = null,
    val phoneNumberError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
)