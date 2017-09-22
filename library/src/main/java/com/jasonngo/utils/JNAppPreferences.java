package com.jasonngo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;

import java.lang.reflect.Type;

public class JNAppPreferences {
	public static final String UTILS_SHARED_PREFERENCES = "Utils_Preferences_key";
	private static JNAppPreferences instance;

	public static JNAppPreferences getInstance(Context pContext){
		if (instance == null)
			instance = new JNAppPreferences(pContext);
		return instance;
	}

	private SharedPreferences prefs;
	public JNAppPreferences(Context pContext) {
		prefs = pContext.getSharedPreferences(UTILS_SHARED_PREFERENCES, Context.MODE_PRIVATE);
	}

	public void putBooleanValue(String KEY, boolean value) {
		prefs.edit().putBoolean(KEY, value).apply();
	}

	public boolean getBooleanValue(String KEY, boolean defvalue) {
		return prefs.getBoolean(KEY, defvalue);
	}

	public void putStringValue(String KEY, String value) {
		prefs.edit().putString(KEY, value).apply();
	}

	public String getStringValue(String KEY, String defValue) {
		return prefs.getString(KEY, defValue);
	}

	public void put(String KEY, Object value) {
		if (value instanceof String) {
			prefs.edit().putString(KEY, (String) value).apply();
		} else {
			prefs.edit().putString(KEY, new Gson().toJson(value)).apply();
		}
	}

	public  <T> T get(String KEY, Class<T> mModelClass) {
		Object object = null;
		try {
			object = new Gson().fromJson(prefs.getString(KEY, ""), mModelClass);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		return Primitives.wrap(mModelClass).cast(object);
	}

	public  <T> T get(String KEY, Type typeOfT) {
		return new Gson().fromJson(prefs.getString(KEY, ""), typeOfT);
	}

	public void putIntValue(String KEY, int value) {
		prefs.edit().putInt(KEY, value).apply();
	}

	public int getIntValue(String KEY, int defValue) {
		return prefs.getInt(KEY, defValue);
	}

	public void putLongValue(String KEY, long value) {
		prefs.edit().putLong(KEY, value).apply();
	}

	public long getLongValue(String KEY, long defValue) {
		return prefs.getLong(KEY, defValue);
	}

	public void putFloatValue(String KEY, float value) {
		prefs.edit().putFloat(KEY, value).apply();
	}

	public float getFloatValue(String KEY, float defValue) {
		return prefs.getFloat(KEY, defValue);
	}

	public void removeValue(String KEY) {
		prefs.edit().remove(KEY).apply();
	}

	public void removeAll() {
		prefs.edit().clear().apply();
	}

}
