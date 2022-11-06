package com.alex.materialdiary.sys.messages;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessageService {
    private static MessageService mInstance;
    private static final String BASE_URL = "https://one.pskovedu.ru/extjs/";
    private Retrofit mRetrofit;
    private String cookies = "";

    private MessageService(String cookies) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        if (cookies == null) cookies = "";
        Log.d("cookiesss", cookies);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        String finalCookies = cookies;
        OkHttpClient client = new OkHttpClient.Builder()
                .followRedirects(false)
                .followSslRedirects(false)
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    final Request original = chain.request();
                    final Request authorized = original.newBuilder()
                            .header("Cookie", finalCookies)
                            .build();
                    return chain.proceed(authorized);
                })
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static MessageService getInstance(String cookies) {
        if (mInstance == null) {
            mInstance = new MessageService(cookies);
        }
        return mInstance;
    }
    public static void restartInstance() {
        mInstance = null;
    }
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public MessageInterface getJSONApi() {
        return mRetrofit.create(MessageInterface.class);
    }
}
