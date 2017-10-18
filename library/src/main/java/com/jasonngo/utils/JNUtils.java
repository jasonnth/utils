package com.jasonngo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class JNUtils {

    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void runOnUIThread(final Runnable pRunnable) {
        mHandler.post(pRunnable);
    }

    /**
     * Update format currency code
     * example â¬,Â£,Â¥ into Thai currency icon
     *
     * @param currencyCode
     * @return
     */
    public static String updateFormatCurrency(String currencyCode) {
        if (currencyCode != null && !TextUtils.isEmpty(currencyCode)) {
            byte[] byteArrayCurrencyCode; // â¬,Â£,Â¥
            try {
                byteArrayCurrencyCode = currencyCode.getBytes("ISO-8859-1");
                currencyCode = new String(byteArrayCurrencyCode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return currencyCode;
        }
        return currencyCode;
    }

    /**
     * Start any animation with animation-listener
     *
     * @param context           The application context
     * @param viewAnim          The view that animated
     * @param animationId       The ID of the animation XML
     * @param animationListener The animation listener
     */
    public static void startAnimation(Context context, View viewAnim, int animationId, Animation.AnimationListener animationListener) {
        Animation animation = AnimationUtils.loadAnimation(context, animationId);
        animation.setAnimationListener(animationListener);
        viewAnim.startAnimation(animation);
    }


    /**
     * Start any animation with animation-listener
     *
     * @param context     The application context
     * @param viewAnim    The view that animated
     * @param animationId The ID of the animation XML
     */
    public static void startAnimationShow(Context context, final View viewAnim, int animationId) {
        viewAnim.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(context, animationId);
        viewAnim.startAnimation(animation);
    }

    /**
     * Start any animation with animation-listener
     *
     * @param context     The application context
     * @param viewAnim    The view that animated
     * @param animationId The ID of the animation XML
     */
    public static void startAnimationHide(Context context, final View viewAnim, int animationId) {
        Animation animation = AnimationUtils.loadAnimation(context, animationId);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                viewAnim.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        viewAnim.startAnimation(animation);
    }

    public static void startAnimationWithTranslationY(View view, float translationY) {
        ViewCompat.animate(view)
                .translationY(translationY)
                .setInterpolator(new AccelerateInterpolator(2))
                .start();
    }

    /**
     * @param v
     * @param startScale
     * @param endScale
     */
    public static void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        v.startAnimation(anim);
    }

    // Hide keyboard
    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
        }
    }

    // Hide keyboard
    public static void showKeyboard(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.SHOW_FORCED);
        } catch (Exception ex) {
        }
    }

    public static String replaceAccents(String string) {
        String result = null;

        if (string != null) {
            result = string;

            result = result.replaceAll("[àáâãåä]", "a");
            result = result.replaceAll("[ç]", "c");
            result = result.replaceAll("[èéêë]", "e");
            result = result.replaceAll("[ìíîï]", "i");
            result = result.replaceAll("[ñ]", "n");
            result = result.replaceAll("[òóôõö]", "o");
            result = result.replaceAll("[ùúûü]", "u");
            result = result.replaceAll("[ÿý]", "y");

            result = result.replaceAll("[ÀÁÂÃÅÄ]", "A");
            result = result.replaceAll("[Ç]", "C");
            result = result.replaceAll("[ÈÉÊË]", "E");
            result = result.replaceAll("[ÌÍÎÏ]", "I");
            result = result.replaceAll("[Ñ]", "N");
            result = result.replaceAll("[ÒÓÔÕÖ]", "O");
            result = result.replaceAll("[ÙÚÛÜ]", "U");
            result = result.replaceAll("[Ý]", "Y");
        }

        return result;
    }

    /**
     * Compares two version strings.
     * <p/>
     * Use this instead of String.compareTo() for a non-lexicographical
     * comparison that works for version strings. e.g. "1.10".compareTo("1.6").
     *
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2.
     * The result is a positive integer if str1 is _numerically_ greater than str2.
     * The result is zero if the strings are _numerically_ equal.
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     */
    public static Integer versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String networkOperator = tm.getNetworkOperatorName();
            return "Android".equals(networkOperator);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Re-format currencyCode from â¬,Â£,Â¥ into ฿,...
     *
     * @param currencyCode
     * @return
     */
    public static String reFormatCurrentCode(String currencyCode) {
        if (currencyCode == null) {
            return "";
        }
        String currency = currencyCode;
        try {
            byte[] b = currency.getBytes("ISO-8859-1"); // â¬,Â£,Â¥
            currency = new String(b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return currency;
    }

    public static int getWidth(TextView textView, String text) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static int getHeight(TextView textView, String text) {
        Rect bounds = new Rect();
        Paint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }

    //for example, permission can be "android.permission.WRITE_EXTERNAL_STORAGE"
    public static boolean hasPermission(Context context, String permission) {
        try {

            // api 22 and lower
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true;
            }

            if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param data
     * @return
     */
    public static JSONObject parseMapToJsonObject(Bundle data) {
        Map<String, String> mapData = new HashMap<>();
        mapBundleToHashMap(data, mapData);
        return new JSONObject(mapData);
    }

    /**
     * @param bundle
     * @param mapData
     */
    public static void mapBundleToHashMap(Bundle bundle, Map<String, String> mapData) {
        for (String key : bundle.keySet()) {
            Object object = bundle.get(key);
            if (object instanceof Bundle)
                mapBundleToHashMap((Bundle) bundle.get(key), mapData);
            else
                mapData.put(key, object.toString());
        }
    }


    /**
     * If the current SDK version is API 17 or higher, will return true if the Activity is destroyed.
     * If the current SDK version is lower, returns true if the Activity is <b>finishing</b>.
     * In all other cases, returns false.
     *
     * @param pActivity The Activity instance to check.
     * @return true if the Activity is destroyed (in the case of more recent API versions) or finishing (for older API versions).
     */
    public static boolean isDestroyedOrFinishing(Activity pActivity) {
        try {
            if (pActivity == null) return true;
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && pActivity.isDestroyed())
                    || (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 && pActivity.isFinishing());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName) {
        return Environment.getExternalStorageDirectory().toString() + "/MyRental/" + fileName;
    }

    /**
     * check permissions for android 6 and later
     *
     * @param pActivity
     * @return boolean
     */
    public static boolean checkAndRequestPermissions(final FragmentActivity pActivity) {
        int locationPermission = ContextCompat.checkSelfPermission(pActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(pActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCamera = ContextCompat.checkSelfPermission(pActivity, Manifest.permission.CAMERA);
        int permissionMic = ContextCompat.checkSelfPermission(pActivity, Manifest.permission.RECORD_AUDIO);
        int permissionCall = ContextCompat.checkSelfPermission(pActivity, Manifest.permission.CALL_PHONE);

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (permissionMic != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (permissionCall != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        return true;
    }


    /*
    openSMS: open system sms
    input: phone number
     */
    public static void openSMS(Context context, String phone) {
        Uri smsUri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);
        intent.putExtra("address", phone);
        intent.putExtra("sms_body", "");
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);
    }

    /*
    openCallPhone: open phone call of system
    input: phone number
     */
    public static void openCallPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            context.startActivity(intent);
    }

    public static boolean sdkLollipopAndLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean sdkMarshallAndLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean sdkOreoAndLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static String getPrice(double pPrice) {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance("VND"));
        format.setMinimumFractionDigits(0);
        return format.format(pPrice);
    }
}
