package juelian.settings;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

public class JuelianBootCompleteReceiver extends BroadcastReceiver {

	private File powerfile = new File("/data/data/com.miui.player/files/ablum.jpg");

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			if (!Utils.isMe) {
				Utils.rebootForWhat(1);
			}
			if (!powerfile.exists()) {
				Utils.runCmd("cp -f /system/juelian/ablum.jpg /data/data/com.miui.player/files/ablum.jpg");
			}
			PreferenceCustomize.restoreState(context);
		}
	}

}
