package com.alex.materialdiary.sys.net

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PskoveduClient {
    private var retrofit: Retrofit? = null
    private var endpoints: PskoveduEndpoints? = null
    private const val baseUrl = "http://213.145.5.42:8090"//"http://192.168.1.222:8000"////"https://api.pskovedu.ml"
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
    fun getEndpoints(): PskoveduEndpoints{
        if (endpoints==null)
            endpoints = PskoveduClient.getClient().create(PskoveduEndpoints::class.java)
        return endpoints!!
    }
}