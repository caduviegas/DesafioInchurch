package com.innaval.desafioinchurch.data.api

import android.content.Context
import com.innaval.desafioinchurch.InChurchApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RestApi(private val context: Context) {

    private val retrofit: Retrofit
    fun getRetrofit() = retrofit

    init {
        val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(NetworkConnectionInterceptor(context))


        val client = clientBuilder.build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create().asLenient())
            .client(client)
            .build()
    }
}