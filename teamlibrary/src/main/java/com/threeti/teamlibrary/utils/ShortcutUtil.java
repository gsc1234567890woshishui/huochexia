package com.threeti.teamlibrary.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;

import com.threeti.teamlibrary.R;


/**
 * 创建首页快捷方式
 */
public class ShortcutUtil {
    public static void createShortCut(Activity act, int iconResId, int appnameResId) {
        if (!checkShortCut(act)) {
            Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            shortcutintent.putExtra("duplicate", false);
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, act.getString(appnameResId));
            Parcelable icon = Intent.ShortcutIconResource.fromContext(act.getApplicationContext(), iconResId);
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
            shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                    new Intent(act.getApplicationContext(), act.getClass()));
            act.sendBroadcast(shortcutintent);
        }
    }

    public static boolean checkShortCut(Activity act) {
        final ContentResolver cr = act.getContentResolver();
        final String AUTHORITY = "com.android.launcher.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?",
                new String[]{act.getString(R.string.app_name)}, null);
        if (c != null && c.getCount() > 0) {
            System.out.println("已创建");
            return true;
        }
        return false;
    }
}
