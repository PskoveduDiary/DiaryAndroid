package com.alex.materialdiary.sys.net

import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.alex.materialdiary.MyApplication
import com.alex.materialdiary.sys.net.database.CacheDao
import com.alex.materialdiary.sys.net.database.CacheDatabase
import com.alex.materialdiary.sys.net.database.CacheRepository
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
            val db = MyApplication.instance?.getDb()
            var cacheRepository: CacheRepository? = null
            if (db != null){
                cacheRepository = CacheRepository(MyApplication.instance!!.getDb()!!.cacheDao())
            }

            val interceptor = HttpLoggingInterceptor()
            val cacheInterceptor = CacheInterceptor(cacheRepository)
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addInterceptor(cacheInterceptor)
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