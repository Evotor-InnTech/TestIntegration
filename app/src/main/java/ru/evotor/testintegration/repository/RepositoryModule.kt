package ru.evotor.testintegration.repository

import android.content.Context
import ru.evotor.integration.Integration
import ru.evotor.integration.IntegrationImpl

object RepositoryModule {

    lateinit var applicationContext: Context
        private set

    val tokenRepository: TokenRepository by lazy {
        TokenRepositoryImpl(tokenApi)
    }

    private val tokenApi: TokenApi by lazy {
        TokenApi.provideTokenApi()
    }

    val integration: Integration by lazy {
        IntegrationImpl()
    }

    fun init(applicationContext: Context) {
        this.applicationContext = applicationContext
    }
}