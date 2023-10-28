package com.sekalisubmit.storymu.data.remote.retrofit


import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun createClientWithToken(token: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()

                println("REQUEST: ${request.url}")
                println("REQUEST: ${request.headers}")
                println("REQUEST: $token")
                val tokenStructure = "Authorization Bearer $token"
                println("REQUEST: $tokenStructure")

                chain.proceed(request)
            }
            .build()
    }

    fun getApiService(token: String): ApiService {
        val client = createClientWithToken(token)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}
