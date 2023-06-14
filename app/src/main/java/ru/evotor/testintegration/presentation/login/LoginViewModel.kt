package ru.evotor.testintegration.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.evotor.testintegration.presentation.entities.LoginUi
import ru.evotor.testintegration.repository.RepositoryModule
import ru.evotor.testintegration.repository.TokenRepository
import ru.evotor.testintegration.utils.SingleLiveEvent

class LoginViewModel(
    private val tokenRepository: TokenRepository = RepositoryModule.tokenRepository
) : ViewModel() {

    var currentLogin = LoginUi()
        private set(value) {
            field = value
            _loginIsValidState.value = value.isValid()
        }

    private val _loginIsValidState = MutableLiveData<Boolean>()
    val loginIsValidState: LiveData<Boolean> = _loginIsValidState

    private val _tokenState: MutableLiveData<String> = SingleLiveEvent()
    val tokenState: LiveData<String> = _tokenState

    private val _tokenErrorState: MutableLiveData<Unit> = SingleLiveEvent()
    val tokenErrorState: LiveData<Unit> = _tokenErrorState

    fun onLoginChanged(login: String) {
        currentLogin = currentLogin.copy(login = login)
    }

    fun onPasswordChanged(password: String) {
        currentLogin = currentLogin.copy(password = password)
    }

    fun onUserIdChanged(userId: String) {
        currentLogin = currentLogin.copy(userId = userId)
    }

    fun onInnChanged(inn: String?) {
        currentLogin = currentLogin.copy(inn = inn)
    }

    fun onResetAuthorizationChecked(isResetAuthorization: Boolean) {
        currentLogin = currentLogin.copy(isResetAuthorization = isResetAuthorization)
    }

    fun getToken() {
        viewModelScope.launch {
            try {
                with(currentLogin) {
                    val token = tokenRepository.getToken(login, password)
                    _tokenState.value = token
                }
            } catch (exception: Exception) {
                _tokenErrorState.value = Unit
            }
        }
    }
}