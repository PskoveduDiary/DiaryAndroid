package com.alex.materialdiary.sys.common;

import static xdroid.toaster.Toaster.toast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.alex.materialdiary.MainActivity;
import com.alex.materialdiary.R;
import com.alex.materialdiary.sys.ReadWriteJsonFileUtils;
import com.alex.materialdiary.sys.common.models.ClassicBody;
import com.alex.materialdiary.sys.common.models.PDAnswer;
import com.alex.materialdiary.sys.common.models.PDBody;
import com.alex.materialdiary.sys.common.models.ShareUser;
import com.alex.materialdiary.sys.common.models.all_periods.AllPeriods;
import com.alex.materialdiary.sys.common.models.diary_day.DatumDay;
import com.alex.materialdiary.sys.common.models.diary_day.DiaryDay;
import com.alex.materialdiary.sys.common.models.get_user.UserData;
import com.alex.materialdiary.sys.common.models.get_user.UserInfo;
import com.alex.materialdiary.sys.common.models.marks.Item;
import com.alex.materialdiary.sys.common.models.marks.Mark;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarks;
import com.alex.materialdiary.sys.common.models.period_marks.PeriodMarksData;
import com.alex.materialdiary.sys.common.models.periods.Periods;
import com.alex.materialdiary.ui.login.LoginActivity;
import com.alex.materialdiary.utils.MarksTranslator;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;

public class CommonAPI {
    public String uuid = "";
    public String sid = "";
    Context context = null;
    NavController navController;
    public String pdaKey = "";
    public String message_id = "";
    String apikey = "";
    public static CommonAPI ca;
    public void ChangeUuid(String uuid, String name){
        this.uuid = uuid;
        if (context != null) ((MainActivity) context).checkNav();
        if(uuid.length() > 1) {

            apikey = new Crypt().encryptSYS_GUID(uuid);;
            //apikey = x2.X.m0do(uuid);
            //Toast.makeText(context, apikey, Toast.LENGTH_LONG).show();
        }
        SharedPreferences p = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        editor.putString("uuid", uuid);
        editor.apply();
        checkPdaKey(name, uuid);
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
        void marks(List<PeriodMarksData> marks, Boolean needShowsDifs);
        void periods(Periods periods);
    }
    /*public void get_api_cryptor(Context c){
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
    }*/
    public CommonAPI(Context c, NavController navController){
        SharedPreferences p = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences pda = c.getSharedPreferences("pda", Context.MODE_PRIVATE);
        //if(p.contains("message_id")) {
        //    message_id = p.getString("message_id", "");
        //}
        //get_api_cryptor(c);
        if(p.contains("uuid")) {
            uuid = p.getString("uuid", "");
            if(uuid.length() > 1) {
                apikey = new Crypt().encryptSYS_GUID(uuid);
            }
            if (pda.contains("key")){
                //toast(pda.getString("guid", ""));
                if (pda.getString("guid", "").equals(uuid)){
                    pdaKey = pda.getString("key", "");
                }
                else {
                    Gson gson = new Gson();
                    ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(c);
                    String datas = utils.readJsonFileData("users.json");
                    if (datas != null && datas.length() > 100){
                        UserInfo entity = gson.fromJson(String.valueOf(datas), UserInfo.class);

                        String name = entity.getData().getSurname() + " " + entity.getData().getName() + " " + entity.getData().getSecondname();
                        checkPdaKey(name, uuid);
                    }
                }
            }
            else {
                Gson gson = new Gson();
                ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(c);
                String datas = utils.readJsonFileData("users.json");
                if (datas != null && datas.length() > 100){
                    UserInfo entity = gson.fromJson(String.valueOf(datas), UserInfo.class);

                    String name = entity.getData().getSurname() + " " + entity.getData().getName() + " " + entity.getData().getSecondname();
                    checkPdaKey(name, uuid);
                }
                else{
                    checkPdaKey(random_symbols(10), uuid);
                }
            }
            //Toast.makeText(c, apikey, Toast.LENGTH_LONG).show();
        }
        else {
            navController.navigate(R.id.to_new_ch_users);
        }
        this.navController = navController;
        context = c;
        ca = this;
    }
    public CommonAPI(Context c) {
        SharedPreferences p = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences pda = c.getSharedPreferences("pda", Context.MODE_PRIVATE);
        //if(p.contains("message_id")) {
        //    message_id = p.getString("message_id", "");
        //}
        //get_api_cryptor(c);
        if (p.contains("uuid")) {
            uuid = p.getString("uuid", "");
            if (uuid.length() > 1) {
                apikey = new Crypt().encryptSYS_GUID(uuid);
                //Toast.makeText(c, apikey, Toast.LENGTH_LONG).show();
            }
            if (pda.contains("key")) {
                //toast(pda.getString("guid", ""));
                if (pda.getString("guid", "").equals(uuid)) {
                    pdaKey = pda.getString("key", "");
                } else {
                    Gson gson = new Gson();
                    ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(c);
                    String datas = utils.readJsonFileData("users.json");
                    if (datas != null && datas.length() > 100) {
                        UserInfo entity = gson.fromJson(String.valueOf(datas), UserInfo.class);

                        String name = entity.getData().getSurname() + " " + entity.getData().getName() + " " + entity.getData().getSecondname();
                        checkPdaKey(name, uuid);
                    }
                }
            } else {
                Gson gson = new Gson();
                ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(c);
                String datas = utils.readJsonFileData("users.json");
                if (datas != null && datas.length() > 100) {
                    UserInfo entity = gson.fromJson(String.valueOf(datas), UserInfo.class);

                    String name = entity.getData().getSurname() + " " + entity.getData().getName() + " " + entity.getData().getSecondname();
                    checkPdaKey(name, uuid);
                } else {
                    checkPdaKey(random_symbols(10), uuid);
                }

                context = c;
            }
        }
    }
    public String getUser_type(){
        Gson gson = new Gson();
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String datas = utils.readJsonFileData("users.json");
        if (datas != null){
            UserInfo entity = gson.fromJson(String.valueOf(datas), UserInfo.class);
            if (entity.getData().getSchools().size() > 0) {
                List<String> roles = entity.getData().getSchools().get(0).getRoles();
                if (roles.contains("participant")) return "participant";
                else return "parents";
            }
            return "";
        }
        return "";
    }
    public void getUserInfo(UserCallback callback){
        Gson gson = new Gson();
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String datas = utils.readJsonFileData("users.json");
        if (datas != null && datas.length() > 100){
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
        String req = "{\"api_key\":\"" + new Crypt().encryptSYS_GUID(sid) + "\"," +
                "\"sid\":\"" + sid + "\"}";
        RequestBody formBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), req);
        Request request = new Request.Builder()
                .url("http://213.145.5.42:8090/journals/login")
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
                        Log.d("redirect", "body null");
                        context.startActivity(i);
                        return;
                    }
                    String body = String.valueOf(response.body().string());
                    UserInfo entity = gson.fromJson(body, UserInfo.class);
                    if (entity.getSuccess() != true) {
                        toast(entity.getMessage());
                        Log.d("redirect", entity.getMessage());
                        context.startActivity(i);
                    }
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
    public static CommonAPI getInstance(Context c) {
        if (ca == null) return new CommonAPI(c);
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

    public int getMarksDifferencesCount(List<Item> neww){
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String readed = utils.readJsonFileData("marks.json");
        if (readed == null) return 0;
        Type listType = new TypeToken<ArrayList<Item>>(){}.getType();
        List<Item> old = new Gson().fromJson(readed, listType);
        if (old.size() != neww.size()) return Math.abs(neww.size() - old.size());
        int diffs = 0;
        for (int i = 0; i < old.size(); i++){

            List<Mark> diff = new ArrayList<Mark>(neww.get(i).getMarks());
            diff.removeAll(old.get(i).getMarks());
            diffs += diff.size();
        }
        return diffs;
    }
    public void addMarksCache(List<Item> items){
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String json = new Gson().toJson(items);
        utils.createJsonFileData("marks.json", json);
    }
    public void updateMarksCache(){
        ClassicBody body = new ClassicBody();
        body.setGuid(uuid);
        body.setApikey(apikey);
        body.setPdakey(pdaKey);
        AllPeriods cached = getCached();
        if (cached == null) return;
        List<LocalDate> period = MarksTranslator.Companion.get_cur_period(cached.getData());
        if (period.size() < 2) return;
        body.setFrom(period.get(0).toString());
        body.setTo(period.get(1).toString());
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
                        addMarksCache(new MarksTranslator(body.getData()).getItems());
                    }

                    @Override
                    public void onFailure(Call<PeriodMarks> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
                        t.printStackTrace();
                    }

                });
    }
    public void getDay(CommonCallback callb, @Nullable String date){
        ClassicBody body = new ClassicBody();
        if (pdaKey == "" || pdaKey == null) return;
        body.setGuid(uuid);
        body.setApikey(apikey);
        body.setPdakey(pdaKey);
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
        body.setPdakey(pdaKey);
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
                        callb.marks(body.getData(), false);
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
        body.setPdakey(pdaKey);
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
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");
                        ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
                        dates.add(LocalDate.parse(from, formatter));
                        dates.add(LocalDate.parse(to, formatter));
                        callb.marks(body.getData(), MarksTranslator.Companion.get_cur_period(getCached().getData()).equals(dates));
                    }

                    @Override
                    public void onFailure(Call<PeriodMarks> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
                        t.printStackTrace();
                    }

                });
    }
    public AllPeriods getCached(){
        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
        String readed = utils.readJsonFileData("periods.json");
        if (readed == null || readed.length() < 75) {
            return null;
        }
        Type listType = new TypeToken<AllPeriods>(){}.getType();
        return (new Gson().fromJson(readed, listType));
    }
    public void getAllPeriods(MarksCallback callb){
        if (getCached() != null) {
            callb.allperiods(getCached());
        }
        ClassicBody body = new ClassicBody();
        body.setGuid(uuid);
        body.setApikey(apikey);
        body.setPdakey(pdaKey);
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
                        ReadWriteJsonFileUtils utils = new ReadWriteJsonFileUtils(context);
                        if (utils.readJsonFileData("periods.json") == null) {
                            String json = new Gson().toJson(body);
                            utils.createJsonFileData("periods.json", json);
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
        body.setPdakey(pdaKey);
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
    public String gen_pda_key() {
        String l = Long.valueOf(System.currentTimeMillis() / 1000).toString();
        return l + "-" + random_symbols(8);
    }
    public static String random_symbols(int i) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("0123456789qwertyuiopasdfghjklzxcvbnm".charAt(random.nextInt(36)));
        }
        return sb.toString();
    }

    public void checkPdaKey(String name, String guid){
        PDBody pdBody = new PDBody();
        //pdBody.setName(name);
        pdBody.setSysguid(guid);
        //pdBody.setPdakey(gen_pda_key());
        CommonService
                .getInstance()
                .getJSONApi()
                .getPda(pdBody)
                .enqueue(new retrofit2.Callback<PDAnswer>() {
                    @Override
                    public void onResponse(Call<PDAnswer> call, Response<PDAnswer> response) {
                        if (response.code() == 500){
                            toast("Произошла ошибка при получении PDA");
                            return;
                        }
                        PDAnswer body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        if (Objects.equals(body.getStatus(), "not found")){
                            setPdaKey(name, guid);
                        }
                        else{
                            if (body.getPdakey() == "") toast("Ошибка потверждения PDA");
                            else {
                                SharedPreferences p = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
                                pdaKey = body.getPdakey();
                                SharedPreferences.Editor editor = p.edit();
                                editor.putString("key", pdaKey);
                                editor.putString("guid", guid);
                                editor.apply();
                                try {
                                    if (navController != null) navController.navigate(navController.getCurrentDestination().getId());
                                }
                                catch (Exception e){
                                    boolean b = true;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PDAnswer> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
                        t.printStackTrace();
                    }

                });
    }
    public void setPdaKey(String name, String guid){
        PDBody pdBody = new PDBody();
        pdBody.setName(name);
        pdBody.setSysguid(guid);
        String localPda = gen_pda_key();
        pdBody.setPdakey(localPda);
        CommonService
                .getInstance()
                .getJSONApi()
                .setPda(pdBody)
                .enqueue(new retrofit2.Callback<PDAnswer>() {
                    @Override
                    public void onResponse(Call<PDAnswer> call, Response<PDAnswer> response) {
                        if (response.code() == 500){
                            toast("Ошибка PDA №500");
                            return;
                        }
                        PDAnswer body = response.body();
                        if (body == null) {
                            Log.e("NetworkError", response.toString());
                            return;
                        }
                        if (Objects.equals(body.getStatus(), "error")){
                            //set
                            toast("Ошибка PDA №2");
                        }
                        else if(Objects.equals(body.getStatus(), "ok")){
                            SharedPreferences p = context.getSharedPreferences("pda", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = p.edit();
                            editor.putString("key", localPda);
                            editor.putString("guid", guid);
                            editor.apply();
                            try {
                                if (navController != null) navController.navigate(navController.getCurrentDestination().getId());
                            }
                            catch (Exception e){
                                boolean b = true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PDAnswer> call, Throwable t) {
                        toast("На серверах pskovedu произошла ошибка");
                        t.printStackTrace();
                    }

                });
    }

}
