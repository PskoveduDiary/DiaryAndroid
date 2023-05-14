package com.alex.materialdiary.sys.net

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PskoveduClient {
    private var retrofit: Retrofit? = null
    private var endpoints: PskoveduEndpoints? = null
    private const val baseUrl = "https://api.pskovedu.ml"
    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
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