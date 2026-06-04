package com.azyabon.moviesapp.data.remote

import com.azyabon.moviesapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_API_KEY}")
                .addHeader("accept", "application/json")
                .build()

            chain.proceed(request)
        })
        .build()

    val api: MovieApiService by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}
