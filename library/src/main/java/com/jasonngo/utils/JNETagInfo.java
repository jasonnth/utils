package com.jasonngo.utils;

import android.content.Context;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jason Ngo on 9/15/17.
 */

public enum JNETagInfo {
    INSTANCE;

    public static final String UTILS_PREF_ETAG = "utils_etag";
    public void saveKey(Context pContext, String pKey, String pValue){
        JNAppPreferences.getInstance(pContext).put(UTILS_PREF_ETAG + pKey, pValue);
    }

    public String getETag(Context pContext, String pKey){
        return JNAppPreferences.getInstance(pContext).getStringValue(UTILS_PREF_ETAG + pKey, null);
    }

    public <T> T get(Context pContext, String key, Type typeOfT){
        return JNAppPreferences.getInstance(pContext).get(key, typeOfT);
    }

    public void saveData(Context pContext, String pKey, List<?> pData) {
        JNAppPreferences.getInstance(pContext).put(pKey, pData);
    }
}
