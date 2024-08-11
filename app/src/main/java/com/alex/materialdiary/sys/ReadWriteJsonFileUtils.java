package com.alex.materialdiary.sys;

import static xdroid.toaster.Toaster.toast;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.labijie.caching.TimePolicy;
import com.labijie.caching.memory.MemoryCacheManager;
import com.labijie.caching.memory.MemoryCacheOptions;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class ReadWriteJsonFileUtils {
    Context context;
    public static MemoryCacheManager memoryCache = new MemoryCacheManager(new MemoryCacheOptions());

    public ReadWriteJsonFileUtils(Context context) {
        this.context = context;
    }

    public void createJsonFileData(String filename, String mJsonResponse) {
        try {
                File checkFile = new File(context.getFilesDir() + "/users/");
            if (!checkFile.exists()) {
                checkFile.mkdir();
            }
            FileWriter file = new FileWriter(checkFile.getAbsolutePath() + "/" + filename);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Nullable
    public String readJsonFileData(String filename) {
        Object cached = memoryCache.get(filename, new TypeToken<String>(){}.getType(), "ru");
        if (cached != null && filename != "shared.json") {
            return (String) cached;
        }
        try {
            File f = new File(context.getFilesDir() + "/users/" + filename);
            if (!f.exists()) {
                return null;
            }
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            memoryCache.set(filename, new String(buffer), 300000L, TimePolicy.Sliding, "ru");
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Nullable
    public String readJsonFileData(String filename, Boolean bypassCache) {
        if (!bypassCache) return readJsonFileData(filename);
        try {
            File f = new File(context.getFilesDir() + "/users/" + filename);
            if (!f.exists()) {
                return null;
            }
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void deleteFiles() {
        File f = new File(context.getFilesDir() + "/users/");
        File[] files = f.listFiles();
        for (File fInDir : files) {
            fInDir.delete();
        }
    }

    public void deleteFile(String fileName) {
        File f = new File(context.getFilesDir() + "/users/" + fileName);
        if (f.exists()) {
            f.delete();
        }
    }
}