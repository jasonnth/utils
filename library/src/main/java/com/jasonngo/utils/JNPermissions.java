package com.jasonngo.utils;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Created by jasonngo on 10/12/17.
 */

public class JNPermissions {

    public static RxPermissions getInstance(@NonNull Activity pActivity) {
            return new RxPermissions(pActivity);
    }
}
