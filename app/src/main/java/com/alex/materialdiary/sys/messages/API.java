package com.alex.materialdiary.sys.messages;

import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;

import com.alex.materialdiary.sys.common.CommonAPI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class API {
    String cookies = "";
    String myuuid = "";
    public static API api = null;
    public static API getInstance(String cookies){
        if (api == null){
            return new API(cookies);
        }
        return api;
    }
    public interface Callback_Contacts{
        void Contacts(List<String> logins, List<String> names, List<Integer> unreaded, List<Boolean> isGroup);
        void NeedRestart();
    }
    public interface Callback_About{
        void About(String info);
    }
    public interface Callback_AddContact{
        void AddContact(String info);
    }
    public interface Callback_Available {
        void Available(JSONObject info);
    }
    public interface Callback_Chat {
        void Messages(String Name, String oth_id, List<String> messages, List<String> from, List<String> to,
                      List<Boolean> unread, List<String> dates, boolean isGroup, int users, List<String> names);
    }
    int num = 1;
    public API(String cookies){
        this.cookies = cookies;
        api = this;
    }
    public void GetContacts(Callback_Contacts callback){

        Log.d("cookies", String.valueOf(cookies));
        String req = "{'action':'X1API'," +
                "'method':'direct'," +
                "'data':[{'service':'messaging','method':'getContacts','params':[],'ctx':{}}]," +
                "'type':'rpc'," +
                "'tid':" + num + "}";
        num++;
        req = req.replace('\'', '"');
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), req);
        MessageService.getInstance(cookies)
                .getJSONApi()
                .getDirect(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        ResponseBody message = response.body();
                        Log.d("header", String.valueOf(response.headers().get("Set-Cookie")));
                        try {
                            List<String> logins = new ArrayList<String>();
                            List<String> names = new ArrayList<String>();
                            List<Integer> unread = new ArrayList<Integer>();
                            List<Boolean> group = new ArrayList<Boolean>();
                            if (message == null) { callback.NeedRestart(); }
                            String jsonString = String.valueOf(message.string());
                            JSONObject obj = new JSONObject(jsonString);
                            JSONArray arr = obj.getJSONArray("data");
                            for (int i = 0; i < arr.length(); i++)
                            {
                                logins.add(arr.getJSONObject(i).getString("LOGIN"));
                                names.add(arr.getJSONObject(i).getString("NAME"));
                                unread.add(arr.getJSONObject(i).getInt("UNREAD"));
                                try {group.add(arr.getJSONObject(i).getBoolean("IS_GROUP"));}
                                catch (org.json.JSONException Exception) { group.add(false); }
                            }
                            callback.Contacts(logins, names, unread, group);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        Log.d("log", "Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }
    public void GetUserInfo(Callback_About callback, String login, String name){
        String req = "{'action':'X1API'," +
                "'method':'direct'," +
                "'data':[{'service':'messaging','method':'userInfo','params':{'login':'" + login + "','name':'" + name + "'},'ctx':{}}]," +
                "'type':'rpc'," +
                "'tid':" + num + "}";
        num++;
        req = req.replace('\'', '"');
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), req);
        MessageService.getInstance(cookies)
                .getJSONApi()
                .getDirect(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        ResponseBody message = response.body();
                        try {
                            String jsonString = message.string();
                            JSONObject obj = new JSONObject(jsonString);
                            String info = obj.getString("data");
                            callback.About(info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        Log.d("log", "Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }
    public void GetAvailableContacts(Callback_Available callback){
        String req = "{'action':'X1API'," +
                "'method':'direct'," +
                "'data':[{'service':'messaging','method':'getAvailableContacts','params':[],'ctx':{}}]," +
                "'type':'rpc'," +
                "'tid':" + num + "}";
        num++;
        req = req.replace('\'', '"');
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), req);
        MessageService.getInstance(cookies)
                .getJSONApi()
                .getDirect(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        ResponseBody message = response.body();
                        try {
                            String jsonString = message.string();
                            JSONObject obj = new JSONObject(jsonString);
                            JSONObject info = obj.getJSONObject("data");
                            callback.Available(info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        Log.d("log", "Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }
    public void AddUserToContacts(Callback_AddContact callback, String login, String name){
        String req = "{'action':'X1API'," +
                "'method':'direct'," +
                "'data':[{'service':'messaging','method':'addContact','params':{'login':'" + login + "','name':'" + name + "'},'ctx':{}}]," +
                "'type':'rpc'," +
                "'tid':" + num + "}";
        num++;
        req = req.replace('\'', '"');
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), req);
        MessageService.getInstance(cookies)
                .getJSONApi()
                .getDirect(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        ResponseBody message = response.body();
                        try {
                            String jsonString = message.string();
                            JSONObject obj = new JSONObject(jsonString);
                            String info = obj.getString("data");
                            if (!info.equals("ok")) callback.AddContact(info);
                            //else MessageActivity.contextt.openMsg(name, login, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        Log.d("log", "Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }
    void tryCheckMyUuid(String MessageTo, String MessageFrom, String ContactLogin, Boolean isGroup){
        if (isGroup) return;
        String uuid = "";
        if (Objects.equals(MessageTo, MessageFrom))
        {
            uuid = MessageTo;
        }
        if (Objects.equals(MessageTo, ContactLogin)) uuid = MessageFrom;
        if (Objects.equals(MessageFrom, ContactLogin)) uuid = MessageTo;
        if (myuuid != uuid) {
            myuuid = uuid;
            CommonAPI.getInstance().myMessageUuid(uuid);
        }
    }
    public void GetMessages(Callback_Chat callback, String name, String id, boolean isGroup){
        String g = "";
        if (isGroup) g = "true";
        String req = "{'action':'X1API'," +
                "'method':'direct'," +
                "'data':[{'service':'messaging','method':'getMessages','params':{'user':'" +id + "','isGroup':'" + g + "','timeSpan':'all'},'ctx':{}}]," +
                "'type':'rpc'," +
                "'tid':" + num + "}";
        num++;
        if (isGroup) {
            String req2 = "{'action':'X1API'," +
                    "'method':'direct'," +
                    "'data':[{'service':'messaging','method':'getGroupMembers','params':{'groupId':'" + id + "'},'ctx':{}}]," +
                    "'type':'rpc'," +
                    "'tid':" + num + "}";
            req = "["+ req + ", " + req2 + "]";
        }
        req = req.replace('\'', '"');
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), req);
        MessageService.getInstance(cookies)
                .getJSONApi()
                .getDirect(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        ResponseBody message = response.body();
                        try {
                            List<String> msgs = new ArrayList<String>();
                            List<String> froms = new ArrayList<String>();
                            List<String> tos = new ArrayList<String>();
                            List<Boolean> unread = new ArrayList<Boolean>();
                            List<Long> udate = new ArrayList<Long>();
                            List<String> names = new ArrayList<>();
                            Map<String, String> ln = new HashMap<String, String>();
                            List<String> date = new ArrayList<>();
                            String jsonString = message.string();
                            JSONObject obj;
                            JSONObject ans2;
                            JSONArray arr = new JSONArray();
                            JSONArray arr2 = new JSONArray();
                            if (isGroup) {
                                obj = new JSONArray(jsonString).getJSONObject(0);
                                ans2 = new JSONArray(jsonString).getJSONObject(1);
                                arr = obj.getJSONArray("data");
                                arr2 = ans2.getJSONArray("data");
                            }
                            else {
                                obj = new JSONObject(jsonString);
                                arr = obj.getJSONArray("data");
                            }
                            if (isGroup) {
                                for (int i = 0; i < arr2.length(); i++) {
                                    ln.put(arr2.getJSONObject(i).getString("LOGIN"), arr2.getJSONObject(i).getString("NAME"));
                                }
                            }
                            for (int i = 0; i < arr.length(); i++)
                            {
                                //tryCheckMyUuid(arr.getJSONObject(i).getString("TO"), arr.getJSONObject(i).getString("FROM"), id, isGroup);
                                msgs.add(arr.getJSONObject(i).getString("TEXT"));
                                froms.add(arr.getJSONObject(i).getString("FROM"));
                                tos.add(arr.getJSONObject(i).getString("TO"));
                                unread.add(arr.getJSONObject(i).getBoolean("UNREAD"));
                                udate.add(arr.getJSONObject(i).getLong("DATE"));
                                date.add(new SimpleDateFormat("HH:mm").format(udate.get(i)));
                                tryCheckMyUuid(tos.get(i), froms.get(i), id, isGroup);
                                if (isGroup){
                                    if (!Objects.equals(CommonAPI.getInstance().message_id, "")){
                                        if (Objects.equals(froms.get(i), CommonAPI.getInstance().message_id)){
                                            names.add("Вы");
                                        }
                                        else names.add(ln.get(froms.get(i)));
                                    }
                                    else names.add(ln.get(froms.get(i)));
                                }
                                else
                                {
                                    if (String.valueOf(id).equals(String.valueOf(tos.get(i)))){
                                        names.add("Вы");
                                    }
                                    else {
                                        names.add(name);
                                    }
                                }
                            }
                            callback.Messages(name, id, msgs, froms, tos, unread, date, isGroup, arr2.length(), names);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        Log.d("log", "Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }
    public void SendMessage(Callback_Chat callback, String id, String text, boolean isGroup){
        String g = "''";
        if (isGroup) g = "true";
        String dict = "{'action':'X1API'," +
                "'method':'direct'," +
                "'data':[{'service':'messaging','method':'postMessage','params':{'to':'" + id + "','text':'" + text + "','isGroup':" + g + "},'ctx':{}}]," +
                "'type':'rpc'," +
                "'tid':" + num + "}";
        num++;
        dict = dict.replace('\'', '"');
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), dict);
        MessageService.getInstance(cookies)
                .getJSONApi()
                .getDirect(body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        ResponseBody message = response.body();
                        try {
                            Log.d("s", "Sended");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                        Log.d("log", "Error occurred while getting request!");
                        t.printStackTrace();
                    }
                });
    }
}
