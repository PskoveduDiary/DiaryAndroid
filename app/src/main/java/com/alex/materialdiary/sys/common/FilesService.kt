package com.alex.materialdiary.sys.common

import android.webkit.CookieManager
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import com.alex.materialdiary.sys.common.FilesService
import retrofit2.converter.gson.GsonConverterFactory
import com.alex.materialdiary.sys.common.FilesInterface

class FilesService private constructor() {
    private val mRetrofit: Retrofit

    init {
        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(
                Interceptor { chain ->
                    val builder = chain.request().newBuilder()
                    builder
                        .header("Cookie",
                            CookieManager.getInstance()
                                .getCookie("one.pskovedu.ru") + "; " + CookieManager.getInstance()
                                .getCookie("pskovedu.ru")
                        )
                    return@Interceptor chain.proceed(builder.build())
                }
                )
            .build()
        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val jSONApi: FilesInterface
        get() = mRetrofit.create(FilesInterface::class.java)

    companion object {
        private var mInstance: FilesService? = null
        private const val BASE_URL = "https://one.pskovedu.ru/" //"https://pskovedu.ml/api/";//
        val instance: FilesService
            get() {
                if (mInstance == null) {
                    mInstance = FilesService()
                    return mInstance as FilesService
                }
                return mInstance as FilesService
            }
    }
}