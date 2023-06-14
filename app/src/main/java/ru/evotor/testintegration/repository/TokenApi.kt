package ru.evotor.testintegration.repository

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TokenApi {

    @GET("authorize")
    suspend fun getToken(
        @Query("login") login: String,
        @Query("password") password: String
    ): Response<ResponseBody>

    companion object {
        private const val BASE_URL = "https://mobilecashier.ru/api/v2/"

        fun provideTokenApi(): TokenApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(TokenApi::class.java)
        }
    }
}