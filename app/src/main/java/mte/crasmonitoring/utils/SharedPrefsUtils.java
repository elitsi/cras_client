package mte.crasmonitoring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import mte.crasmonitoring.Auth.ChooseProfileTypeActivity;

/**
 * Created by lenovo on 11/20/2016.
 */

public class SharedPrefsUtils {
    public static final String KEY_USER_ID = "USER_ID";
    private static SharedPreferences getPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveUserId(Context context, String userId)
    {
        getPrefs(context).edit().putString(KEY_USER_ID, userId).apply();
    }

    public static String getUserId(Context context)
    {
        return getPrefs(context).getString(KEY_USER_ID, "");
    }

}
