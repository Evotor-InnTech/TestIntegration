package ru.evotor.testintegration.repository

class TokenRepositoryImpl(private val tokenApi: TokenApi) : TokenRepository {

    override suspend fun getToken(login: String, password: String): String {
        val response = tokenApi.getToken(login, password)
        if (response.isSuccessful) {
            response.body()?.let {
                return it.string()
            }
        }
        error("Error receiving token")
    }
}