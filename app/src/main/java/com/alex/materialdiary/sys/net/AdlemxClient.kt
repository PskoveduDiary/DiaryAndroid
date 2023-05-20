package com.alex.materialdiary.sys.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AdlemxClient {
    private var retrofit: Retrofit? = null
    private var endpoints: AdlemxEndpoints? = null
    private const val baseUrl = "https://pskovedu.ml/api/"//"http://192.168.0.107:8090/"//"https://pskovedu.ml/api/"
    fun getClient(): Retrofit {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
    fun getEndpoints(): AdlemxEndpoints{
        if (endpoints==null)
            endpoints = AdlemxClient.getClient().create(AdlemxEndpoints::class.java)
        return endpoints!!
    }
}