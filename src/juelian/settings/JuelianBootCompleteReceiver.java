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
	
	//private Context mContext;
	private File powerfile = new File("/data/data/com.miui.player/files/ablum.jpg");
	//private int isUserModify;
	//public static final int num = 15;
	
	//private boolean first = false;

	public static final String ACTION_QANDA = "juelian.qanda";

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		
		//mContext = context;
		//isUserModify = Settings.System.getInt(mContext.getContentResolver(), "isUserModify", 0);
		Settings.System.putInt(context.getContentResolver(), "common_controller_color",  0xFF328bde);
		Settings.System.putInt(context.getContentResolver(), "settings_bg_color",  0xFF0099CC);
		
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			//new Thread(new myThread()).start();
			if (TextUtils.isEmpty(SystemProperties.get("persist.juelian.blurs"))) {
				Utils.runCmd("setprop persist.juelian.blurs "+sp.getString("switchs", "0"));
			}
			if (TextUtils.isEmpty(SystemProperties.get("persist.juelian.miui.hand"))) {
				Utils.runCmd("setprop persist.juelian.miui.hand "+sp.getString("switchs", "0"));
			}
			if (!Utils.isMe) {
				Utils.rebootForWhat(1);
			}
			if (!powerfile.exists()) {
				Utils.runCmd("cp -f /system/juelian/ablum.jpg /data/data/com.miui.player/files/ablum.jpg");
			}
			PreferenceCustomize.restoreState(context);
			
			if (!sp.getBoolean("first_boot", false)) {
				editor.putBoolean("first_boot", true);
				editor.commit();
				Toast.makeText(context, "使用过程有疑问？来原厂设置的疑问解答瞧瞧！", 1).show();
				NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				Intent intent2 = new Intent(ACTION_QANDA);
				intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 200, intent2, 0);
				Notification.Builder mBuilder = new Notification.Builder(context);
				mBuilder.setSmallIcon(R.drawable.ic_launcher);
				mBuilder.setContentTitle("疑问解答");
				mBuilder.setContentText("使用过程有疑问？点击这里来找答案");
				mBuilder.setContentIntent(pendingIntent);
				mBuilder.setWhen(System.currentTimeMillis());
				mBuilder.setAutoCancel(true);
				mBuilder.setSound(Uri.parse("file:///system/media/audio/notifications/Searchlight.ogg"));
				mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
				manager.notify(2, mBuilder.build());
			}
		}
	}
	/*
	class myThread implements Runnable{

		@Override
		public void run() {
			if (!Util.isMe) {
				Util.rebootForWhat(1);
			}else{
				powerfile = new File(POWERSAVA);
				if (!powerfile.exists()) {
					Toast.makeText(mContext, "检测到系统文件缺失,5秒后将自动关机", 0).show();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Util.rebootForWhat(1);
				}else {
					if (isUserModify==0) {
						BufferedReader powerbr = null;
						int keyCode;
						int keyMode;
						try {
							powerbr = new BufferedReader(new FileReader(powerfile));
							keyCode = Integer.parseInt(powerbr.readLine());
							keyMode = Integer.parseInt(powerbr.readLine());
							if (Util.vfy(new File("/system/app"))!=keyCode || Util.vfy(new File("/system/priv-app"))!=keyMode) {
								View view = LayoutInflater.from(mContext).inflate(R.layout.juelian_custdialog_warn, null);
								CheckBox mCheckBox = (CheckBox) view.findViewById(R.id.checkBox1);
								mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
										// TODO Auto-generated method stub
										Settings.System.putInt(mContext.getContentResolver(), "isUserModify", isChecked ? 1 : 0);
									}
								});
								AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
								mBuilder.setTitle("警告！");
								//mBuilder.setMessage("检测到系统/system/app或/system/priv-app中的软件数量与默认刷机包的不一致你的手机很有可能被恶意植入推广软件!");
								mBuilder.setView(view);
								mBuilder.setNeutralButton("好的", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
							}
							
						} catch (IOException e) {
							e.printStackTrace();
						}finally{
							if (powerbr!=null) {
								try {
									powerbr.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
	}
	*/

}
