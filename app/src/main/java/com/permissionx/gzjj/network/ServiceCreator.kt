package com.permissionx.gzjj.network

import com.permissionx.gzjj.pojos.network.AddCookiesInterceptor
import com.permissionx.gzjj.pojos.network.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val BASE_URL=URL.BASE_URL

    private val okBuilder = OkHttpClient.Builder()
        .addInterceptor(AddCookiesInterceptor())
        .addInterceptor(ReceivedCookiesInterceptor())
        .build()

    private val retrofit= Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okBuilder)
        .build()

    fun<T> create (serviceClass: Class<T>):T= retrofit.create(serviceClass)

    inline fun <reified T> create():T=create(T::class.java)
}