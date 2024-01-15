package ru.evotor.testintegration.presentation.entities

data class LoginUi(
    val login: String = "",
    val password: String = "",
    val userId: String = "",
    val inn: String? = null,
    val qrId: Long? = null,
    val deviceId: String? = null,
    val employeeId: String? = null,
    val isResetAuthorization: Boolean = false
) {

    fun isValid(): Boolean =
        login.isNotEmpty() && password.isNotEmpty()
}