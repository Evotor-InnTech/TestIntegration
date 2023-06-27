package ru.evotor.testintegration.repository

interface TokenRepository {
    suspend fun getToken(login: String, password: String): String
}