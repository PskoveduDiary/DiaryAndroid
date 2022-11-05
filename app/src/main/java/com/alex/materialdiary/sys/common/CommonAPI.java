package com.alex.materialdiary.sys.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.common.models.ClassicBody;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods;
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay;
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay;
import com.alex.materialdiary.sys.common.models.get_user.UserData;
import com.alex.materialdiary.sys.common.models.get_user.UserInfo;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarks;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;
import com.alex.materialdiary.ui.login.LoginActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;

public class CommonAPI {
    String uuid = "";
    String sid = "";
    Context context = null;
    NavController navController;
    String apikey = "agh8mwhvwmj9v8h09h90vhcwvn8wrnhv89whv9hwr9pv";
    public static CommonAPI ca;
    public void ChangeUuid(String uuid){
        this.uuid = uuid;
        SharedPreferences p = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        editor.putString("uuid", uuid);
        editor.apply();
        Log.d("uuid", uuid);
        navController.navigate(R.id.to_diary);
    }
    public interface CommonCallback{
        void day(List<DatumDay> lesson);
    }
    public interface UserCallback{
        void user(UserData user);
    }

    public interface MarksCallback{
        void allperiods(AllPeriods periods);
        void marks(List<PeriodMarksData> marks);
    }
    public CommonAPI(Context c, NavController navController){
        SharedPreferences p = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(p.contains("uuid")) {
            uuid = p.getString("uuid", "");
        }
        else {
            String X1 = "";
            Intent i = new Intent(c, LoginActivity.class);
            String copkies = CookieManager.getInstance().getCookie("one.pskovedu.ru");
            if (copkies == null) {
                c.startActivity(i);
            } else {

                String[] splitted = copkies.split(";");
                for (int ir = 0; ir < splitted.length; ir++) {
                    if (splitted[ir].startsWith("X1_SSO=")) {
                        X1 = splitted[ir].replace("X1_SSO=", "");
                        sid = X1;
                        navController.navigate(R.id.to_ch_users);
                    }
                }
                if (X1 == "") {
                    CookieManager.getInstance().removeAllCookies(null);
                    c.startActivity(i);
                }
            }
        }
        this.navController = navController;
        context = c;
        ca = this;
    }
    public void getUserInfo(UserCallback callback){
        String X1 = "";
        Intent i = new Intent(context, LoginActivity.class);
        String copkies = CookieManager.getInstance().getCookie("one.pskovedu.ru");
        if (copkies == null) {
            context.startActivity(i);
        } else {

            String[] splitted = copkies.split(";");
            for (int ir = 0; ir < splitted.length; ir++) {
                Log.d("14324", splitted[ir]);
                String ds = splitted[ir].replace(" ", "");
                if (ds.startsWith("X1_SSO=")) {
                    X1 = ds.replace("X1_SSO=", "");
                    sid = X1;
                }
            }
            if (X1 == "") {
                CookieManager.getInstance().removeAllCookies(null);
                context.startActivity(i);
            }
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Gson gson = new Gson();
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCustomKey("sid", sid /* string value */);
        RequestBody formBody = new FormBody.Builder()
                .add("api_key", "agh8mwhvwmj9v8h09h90vhcwvn8wrnhv89whv9hwr9pv")
                .add("sid", sid)
                .build();
        Request request = new Request.Builder()
                .url("https://one.pskovedu.ru/person-info-api/login")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback(){
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try {
                    UserInfo entity = gson.fromJson(String.valueOf(response.body().string()), UserInfo.class);
                    callback.user(entity.getData());
                    crashlytics.setCustomKey("user_data", String.valueOf(response.body()));
                }
                catch (Exception e){
                    crashlytics.recordException(e);
                }
            }

        });
    }
    public CommonAPI(Context c, String uuid) {
        SharedPreferences p = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(p.contains("uuid")) {
            uuid = p.getString("uuid", "");
        }
        else {
            this.context = c;
            this.uuid = uuid.toString();
            ca = this;
        }
    }
    public static CommonAPI getInstance() {
        return ca;
    }
    public void getDay(CommonCallback callb, @Nullable String date){
        ClassicBody body = new ClassicBody();
        body.setGuid(uuid);
        body.setApikey(apikey);
        body.setDate(date);
        CommonService
                .getInstance()
                .getJSONApi()
                .getDiaryDay(body)
                .enqueue(new retrofit2.Callback<DiaryDay>() {
                    @Override
                    public void onResponse(Call<DiaryDay> call, Response<DiaryDay> response) {
                        DiaryDay body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        List<DatumDay> day = body.getData();
                        callb.day(day);
                    }

                    @Override
                    public void onFailure(Call<DiaryDay> call, Throwable t) {
                        t.printStackTrace();
                    }

        });
    }
    public void allMarks(MarksCallback callb){
        ClassicBody body = new ClassicBody();
        body.setGuid(uuid);
        body.setApikey(apikey);
        CommonService
                .getInstance()
                .getJSONApi()
                .getAllMarks(body)
                .enqueue(new retrofit2.Callback<PeriodMarks>() {
                    @Override
                    public void onResponse(Call<PeriodMarks> call, Response<PeriodMarks> response) {
                        PeriodMarks body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.marks(body.getData());
                    }

                    @Override
                    public void onFailure(Call<PeriodMarks> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
    }

    public void periodMarks(MarksCallback callb, String from, String to){
        ClassicBody body = new ClassicBody();
        body.setGuid(uuid);
        body.setApikey(apikey);
        body.setFrom(from);
        body.setTo(to);
        CommonService
                .getInstance()
                .getJSONApi()
                .getPeriodMarks(body)
                .enqueue(new retrofit2.Callback<PeriodMarks>() {
                    @Override
                    public void onResponse(Call<PeriodMarks> call, Response<PeriodMarks> response) {
                        PeriodMarks body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.marks(body.getData());
                    }

                    @Override
                    public void onFailure(Call<PeriodMarks> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
    }
    public void getPeriods(MarksCallback callb){
        ClassicBody body = new ClassicBody();
        body.setGuid(uuid);
        body.setApikey(apikey);
        CommonService
                .getInstance()
                .getJSONApi()
                .getAllPeriods(body)
                .enqueue(new retrofit2.Callback<AllPeriods>() {
                    @Override
                    public void onResponse(Call<AllPeriods> call, Response<AllPeriods> response) {
                        AllPeriods body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.allperiods(body);
                    }

                    @Override
                    public void onFailure(Call<AllPeriods> call, Throwable t) {
                        t.printStackTrace();
                    }

                });
    }
}
