package mte.crasmonitoring.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by lenovo on 11/12/2016.
 */

public class PermissionsManager {

    public static final int REQUEST_USAGE_PERMISSION = 1;
    public static final int REQUEST_OVERLAY_PERMISSION = 2;

    public interface PermissionsListener
    {
        void onPermissionsGranted();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static boolean hasUsageStatsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        return granted;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestOverlayPermission(Activity activity, PermissionsListener permissionsListener)
    {
        if(needsOverlayPermissions(activity))
        {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
        }
        else
            permissionsListener.onPermissionsGranted();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void requestUsageStatsPermission(Activity activity, PermissionsListener permissionsListener) {
        if(needsUsageStatsPermission(activity))
            activity.startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), REQUEST_USAGE_PERMISSION);
        else
            permissionsListener.onPermissionsGranted();
    }

    public static boolean needsUsageStatsPermission(Context context) {
        return VersionsUtils.hasLollipop() && !hasUsageStatsPermission(context);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean needsOverlayPermissions(Activity activity)
    {
        return VersionsUtils.hasMarshmallow() && !Settings.canDrawOverlays(activity);
    }



}
