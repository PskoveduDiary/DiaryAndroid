package com.alex.materialdiary.sys.common;

import com.alex.materialdiary.sys.messages.MessageInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommonService {
    private static CommonService mInstance;
    private static final String BASE_URL = "http://213.145.5.42:8090/"; //"https://pskovedu.ml/api/";//
    private Retrofit mRetrofit;

    private CommonService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                /*.addNetworkInterceptor( chain ->
                chain.proceed(
                        chain.request()
                                .newBuilder()
                                .header("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 12;)")
                                .build()
                )
                )*/
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static CommonService getInstance() {
        if (mInstance == null) {
            mInstance = new CommonService();
        }
        return mInstance;
    }
    public CommonInterface getJSONApi() {
        return mRetrofit.create(CommonInterface.class);
    }
}
