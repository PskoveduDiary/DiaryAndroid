package com.alex.materialdiary.sys.common;

import static xdroid.toaster.Toaster.toast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.alex.materialdiary.MainActivity;
import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils;
import com.alex.materialdiary.sys.common.models.ClassicBody;
import com.alex.materialdiary.sys.common.models.ShareUser;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods;
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay;
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay;
import com.alex.materialdiary.sys.common.models.get_user.UserData;
import com.alex.materialdiary.sys.common.models.get_user.UserInfo;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarks;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;
import com.alex.materialdiary.sys.common.models.periods.Periods;
import com.alex.materialdiary.ui.login.LoginActivity;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;

public class CommonAPI {
    public String uuid = "";
    String sid = "";
    Context context = null;
    NavController navController;
    public String message_id = "";
    String apikey = "";
    public static CommonAPI ca;
    public void ChangeUuid(String uuid){
        this.uuid = uuid;
        ((MainActivity) context).checkNav();
        //if(uuid.length() > 1) {
        //    apikey = Crypt.encryptSYS_GUID(uuid);
        //    //Toast.makeText(context, apikey, Toast.LENGTH_LONG).show();
        //}
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
        void periods(Periods periods);
    }
    public void get_api_cryptor(Context c){
        SharedPreferences p = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        String url = "https://raw.githubusercontent.com/Adlemex/MaterialDiaryNew/main/api_key.txt";
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback(){

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.body() != null){
                    if(response.code() == 200){
                        String res = response.body().string();
                        if(Objects.equals(res, p.getString("api_key", ""))) return;
                        p.edit().putString("api_key", res).apply();
                        //Crypt.generateKeyFromString(res);
                        //if(uuid.length() > 1) {
                        //    apikey = Crypt.encryptSYS_GUID(uuid);
                        //}
                    }
                }
            }
        });
    }
    public CommonAPI(Context c, NavController navController){
        SharedPreferences p = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        //if(p.contains("message_id")) {
        //    message_id = p.getString("message_id", "");
        //}
        //get_api_cryptor(c);
        if(p.contains("uuid")) {
            uuid = p.getString("uuid", "");
            //if(uuid.length() > 1) {
            //    apikey = Crypt.encryptSYS_GUID(uuid);
            //    //Toast.makeText(c, apikey, Toast.LENGTH_LONG).show();
            //}
            //Toast.makeText(c, apikey, Toast.LENGTH_LONG).show();
        }
        else {
            navController.navigate(R.id.to_new_ch_users);
        }
        this.navController = navController;
        context = c;
        ca = this;
    }
    public void getUserInfo(UserCallback callback){
        Gson gson = new Gson();
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String datas = utils.readJsonFileData("users.json");
        if (datas != null){
            UserInfo entity = gson.fromJson(String.valueOf(datas), UserInfo.class);
            callback.user(entity.getData());
            return;
        }
        String X1 = "";
        Intent i = new Intent(context, LoginActivity.class);
        String copkies = CookieManager.getInstance().getCookie("one.pskovedu.ru");
        if (copkies == null) {
            Log.d("redirect", "no-cookies");
            context.startActivity(i);
            return;
        } else {

            String[] splitted = copkies.split("; ");
            for (int ir = 0; ir < splitted.length; ir++) {
                Log.d("14324", splitted[ir]);
                if (splitted[ir].startsWith("X1_SSO=")) {
                    X1 = splitted[ir].replace("X1_SSO=", "");
                    sid = X1;
                    break;
                }
            }
            if (X1 == "") {
                CookieManager.getInstance().removeAllCookies(null);
                Log.d("redirect", "x1-empty");
                context.startActivity(i);
                return;
            }
        }
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
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
                toast("На серверах pskovedu произошла ошибка");

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try {
                    if (response.body() == null){
                        return;
                    }
                    String body = String.valueOf(response.body().string());
                    UserInfo entity = gson.fromJson(body, UserInfo.class);
                    callback.user(entity.getData());
                    utils.createJsonFileData("users.json", body);
                }
                catch (Exception e){
                    e.printStackTrace();
                    crashlytics.recordException(e);
                }
            }

        });
    }
    public static CommonAPI getInstance(Context c, NavController navController) {
        if (ca == null) return new CommonAPI(c, navController);
        return ca;
    }
    public static CommonAPI getInstance() {
        return ca;
    }
    public void myMessageUuid(String uuid){
        if (Objects.equals(message_id, uuid)) return;
        Log.d("messss", message_id + " " + uuid);
        message_id = uuid;
        SharedPreferences p = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        editor.putString("message_id", uuid);
        editor.apply();

    }
    public List<ShareUser> getShared(){
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String readed = utils.readJsonFileData("shared.json");
        if (readed == null) return new ArrayList<>();
        Type listType = new TypeToken<ArrayList<ShareUser>>(){}.getType();
        return new Gson().fromJson(readed, listType);
    }
    public void addShared(ShareUser shareUser){
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        List<ShareUser> current = getShared();
        current.add(shareUser);
        String json = new Gson().toJson(current);
        utils.createJsonFileData("shared.json", json);
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
                        if (response.code() == 500){
                            toast("На серверах pskovedu произошла ошибка");
                            return;
                        }
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
                        toast("На серверах pskovedu произошла ошибка");
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
                        if (response.code() == 500){
                            toast("На серверах pskovedu произошла ошибка");
                            return;
                        }
                        PeriodMarks body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.marks(body.getData());
                    }

                    @Override
                    public void onFailure(Call<PeriodMarks> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
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
                        if (response.code() == 500){
                            toast("На серверах pskovedu произошла ошибка");
                            return;
                        }
                        PeriodMarks body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.marks(body.getData());
                    }

                    @Override
                    public void onFailure(Call<PeriodMarks> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
                        t.printStackTrace();
                    }

                });
    }
    public void getAllPeriods(MarksCallback callb){
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
                        if (response.code() == 500){
                            toast("На серверах pskovedu произошла ошибка");
                            return;
                        }
                        AllPeriods body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.allperiods(body);
                    }

                    @Override
                    public void onFailure(Call<AllPeriods> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
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
                .getPeriods(body)
                .enqueue(new retrofit2.Callback<Periods>() {
                    @Override
                    public void onResponse(Call<Periods> call, Response<Periods> response) {
                        if (response.code() == 500){
                            toast("На серверах pskovedu произошла ошибка");
                            return;
                        }
                        Periods body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        callb.periods(body);
                    }

                    @Override
                    public void onFailure(Call<Periods> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
                        t.printStackTrace();
                    }

                });
    }

}
