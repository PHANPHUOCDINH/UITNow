package com.uit.uitnow;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

// Lưu vào bộ nhớ app
public class PrefUtil {
    public static void savePref(Context c,String key, String value) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.putString(key, value);
        edCaches.commit();
    }
    public static void saveSetPref(Context c, String key, Set<String> value)
    {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.putStringSet(key, value);
        edCaches.commit();
    }
    public static Set<String> loadSetPref(Context c,String key)
    {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        return p.getStringSet(key,null);
    }
    public static String loadPref(Context c,String key) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        return p.getString(key,null);
    }

    public static void removePref(Context c,String key) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.remove(key);
        edCaches.commit();
    }

    public static void clearPref(Context c,String key) {
        SharedPreferences p=c.getSharedPreferences("caches",c.MODE_PRIVATE);
        SharedPreferences.Editor edCaches=p.edit();
        edCaches.clear();
        edCaches.commit();
    }
}
