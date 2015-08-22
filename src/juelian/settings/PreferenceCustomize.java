package juelian.settings;


import android.R.raw;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import miui.preference.PreferenceActivity;

public class PreferenceCustomize extends PreferenceFragment implements OnPreferenceChangeListener {
	
	public static final String TAG = "mijl-->";
	
	public static final String ZTESETTINGSPACKNAME = "com.android.jlsettings";
	
	private EditTextPreference mDpi;
	public static final String KEY_DPI = "dpi";
	
	private CheckBoxPreference mCrtEffect;
	public static final String KEY_CRT = "crt";
	public static final String KEY_CRT_NAME = "juelian_crt";

	private ListPreference mReverseKey;
	public static final String KEY_KEY_STRING = "key_back";

	private CheckBoxPreference mDoublewake;
	public static final String KEY_DOUBLEWAKE = "doublewake";

	private CheckBoxPreference mGlovesMode;
	public static final String KEY_GLOVESMODE = "glovesmode";

	private Preference mGesture;
	public static final String KEY_GESTURE = "gesture";

	private Preference mAuthor;
	public static final String KEY_AUTHOR = "author";

	private Preference mSmartScreen;
	public static final String KEY_SMARTSCREEN = "smartscreen";

	private Preference mScreenEffect;
	public static final String KEY_SCREENEFFECT = "screeneffect";
	
	private ListPreference mSwitchCamera;
	public static final String KEY_CAMERA_STRING="camera";
	
	private CheckBoxPreference mBackLongSmartScreen;
	public static final String KEY_BACK_LONG_SMART_SCREEN = "backlong";
	
	private CheckBoxPreference mDoubleTapHome;
	public static final String KEY_DOUBLE_TAP_HOME = "doublehome";
	
	private ContentResolver cr;

	@Override
	public void onCreate(Bundle arg0) {
		getActivity().setTheme(miui.R.style.Theme_Light_Settings);
		super.onCreate(arg0);
		addPreferencesFromResource(R.xml.customize_settings_preference);
		cr = getActivity().getContentResolver();
		
		PreferenceScreen pfs = getPreferenceScreen();
		pfs.getPreferenceManager().setSharedPreferencesMode(Context.MODE_WORLD_READABLE);
		String defaultDpi = SystemProperties.get("persist.jldensity");
		
		mDpi = (EditTextPreference) pfs.findPreference(KEY_DPI);
		mDpi.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		mDpi.getEditText().setHint(defaultDpi);
		mDpi.setSummary("获取到的值："+defaultDpi+"，默认值：480.");
		mDpi.setOnPreferenceChangeListener(this);
		
		mReverseKey = (ListPreference) pfs.findPreference(KEY_KEY_STRING);
		mReverseKey.setSummary(mReverseKey.getEntries()[SettingSystemGetInt(cr, "choose_what", 0)]);
		mReverseKey.setOnPreferenceChangeListener(this);
		
		mDoublewake = (CheckBoxPreference) pfs.findPreference(KEY_DOUBLEWAKE);

		mGlovesMode = (CheckBoxPreference) pfs.findPreference(KEY_GLOVESMODE);

		mGesture = (Preference) pfs.findPreference(KEY_GESTURE);

		mAuthor = (Preference) pfs.findPreference(KEY_AUTHOR);

		mSmartScreen = (Preference) pfs.findPreference(KEY_SMARTSCREEN);

		mScreenEffect = (Preference) pfs.findPreference(KEY_SCREENEFFECT);

		mGlovesMode.setChecked(Settings.Global.getInt(getActivity().getContentResolver(), "glove_mode_on", 0) == 1 ? true : false);
		
		mBackLongSmartScreen = (CheckBoxPreference) findPreference(KEY_BACK_LONG_SMART_SCREEN);
		mBackLongSmartScreen.setChecked(SettingSystemGetInt(cr, "juelian_support_longback_smartscreen", 0) == 1 ? true : false);
		
		mDoubleTapHome = (CheckBoxPreference) findPreference(KEY_DOUBLE_TAP_HOME);
		mDoubleTapHome.setChecked(SettingSystemGetInt(cr, "juelian_support_doubleclick_home", 0) == 1 ? true : false);

		mSwitchCamera = (ListPreference) pfs.findPreference(KEY_CAMERA_STRING);
		mSwitchCamera.setSummary(mSwitchCamera.getEntries()[SettingSystemGetInt(cr, "choose_what_3", 0)]);
		mSwitchCamera.setOnPreferenceChangeListener(this);

		rmPreferenceFromBoolean();
	}
	
	public void rmPreferenceFromBoolean(){
		if (SettingSystemGetInt(cr, "choose_what_2", 0)!=0) {
			getPreferenceScreen().removePreference(findPreference(KEY_SMARTSCREEN));
			getPreferenceScreen().removePreference(findPreference(KEY_BACK_LONG_SMART_SCREEN));
		}
	}
	
	public void chooseReport(){
		View view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.juelian_custdialog_report_way, null);
		TextView weiboTextView = (TextView) view.findViewById(R.id.gotoweibo_web);
		weiboTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("https://www.weibo.com/lovejuelian"));
					startActivity(intent);
				} catch (Exception e) {
					Log.d(TAG, "can not start to view my weibo");
				}
			}
		});
		
		AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(getActivity());
		mDialogBuilder.setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		mDialogBuilder.setView(view);
		mDialogBuilder.show();
	}

	public static void restoreState(Context context){
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		boolean flag = sp.getBoolean(KEY_DOUBLEWAKE, false);
		Utils.runCmd(flag ? "echo 1 > /proc/touchscreen_gesture_enable" : "echo 0 > /proc/touchscreen_gesture_enable");
	}
	
    public void showNotFoundToast(){
    	Toast.makeText(getActivity().getApplicationContext(), R.string.not_found_app, 1).show();
    }

	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if (preference == mCrtEffect) {
			SettingSystemPutInt(cr, KEY_CRT_NAME, mCrtEffect.isChecked() ? 1 : 0);
			return true;
		}

		if (preference == mDoublewake) {
			Utils.runCmd(mDoublewake.isChecked() ? "echo 1 > /proc/touchscreen_gesture_enable" : "echo 0 > /proc/touchscreen_gesture_enable");
			return true;
		}
		if (preference == mGlovesMode) {
			Settings.Global.putInt(getActivity().getContentResolver(),"glove_mode_on", mGlovesMode.isChecked() ? 1 : 0);
			return true;
		}
		if (preference == mDoubleTapHome) {
			Settings.System.putInt(getActivity().getContentResolver(),"juelian_support_doubleclick_home", mDoubleTapHome.isChecked() ? 1 : 0);
			return true;
		}
		if (preference == mBackLongSmartScreen) {
			Settings.System.putInt(getActivity().getContentResolver(),"juelian_support_longback_smartscreen", mBackLongSmartScreen.isChecked() ? 1 : 0);
			return true;
		}
		
		if (preference == mGesture) {
			try {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setClassName("com.zte.zgesture","com.zte.zgesture.MainSettingActivity");
				startActivity(intent);
			} catch (Exception e) {
				showNotFoundToast();
				return false;
			}
			return true;
		}

		if (preference == mSmartScreen) {
			try {
				Intent intent = new Intent();
				intent.setClassName("com.powermo.SmartBar","com.powermo.SmartBar.MasterEntryActivity");
				startActivity(intent);
			} catch (Exception e) {
				showNotFoundToast();
				return false;
			}
			return true;
		}
		if (preference == mAuthor) {
			chooseReport();
			return true;
		}

		if (preference == mScreenEffect) {
			try {
				Intent intent = new Intent();
				intent.setClassName(ZTESETTINGSPACKNAME, ZTESETTINGSPACKNAME + ".ScreenEffectSettings");
				startActivity(intent);
			} catch (Exception e) {
				showNotFoundToast();
				return false;
			}
			return true;
		}
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mReverseKey) {
			int index = mReverseKey.findIndexOfValue((String) newValue);
			Utils.runCmd("mount -o rw,remount /dev/block/platform/msm_sdcc.1/by-name/system /system");
			if (index==1) {
				Utils.runCmd("cp -rf /system/juelian/reverse/* /system/usr/keylayout");
			}else if (index==0) {
				Utils.runCmd("cp -rf /system/juelian/stockrom/* /system/usr/keylayout");
			}
			Utils.runCmd("mount -o ro,remount /dev/block/platform/msm_sdcc.1/by-name/system /system");
			mReverseKey.setSummary(mReverseKey.getEntries()[index]);
			Settings.System.putInt(getActivity().getContentResolver(),"choose_what", index);
			DialogShow(getActivity());
			return true;
		}
		if (preference == mDpi) {
			String valueString = (String)newValue;
			if (!TextUtils.isEmpty(valueString)) {
				int index = Integer.parseInt(valueString);
				if (index < 300 || index > 600) {
					Toast.makeText(getActivity().getApplicationContext(), "输入的值小于300或大于600!", 1).show();
					return false;
				}else {
					Utils.runCmd("setprop persist.jldensity " + valueString);
					mDpi.setSummary("您输入的值："+valueString+", 默认为480.");
					DialogShow(getActivity());
					return true;
				}
			}else {
				Toast.makeText(getActivity().getApplicationContext(), "输入的值不能为空!", 1).show();
				return false;
			}
		}
		if (preference == mSwitchCamera) {
			int index = mSwitchCamera.findIndexOfValue((String) newValue);
			if (index == 0) {
				Utils.runCmd("pm disable com.zte.camera");
				Utils.runCmd("pm enable com.android.camera");
			} else if (index == 1) {
				boolean flag = IsInstall("com.zte.camera");
				Log.d(TAG, "is install zte camera: " + String.valueOf(flag));
				if (!flag) {
					Toast.makeText(getActivity().getApplicationContext(), "正在切换..请稍后一会", 0).show();
					Utils.runCmd("pm install -r /system/juelian/Camera_ZTE.apk");
				}
				Utils.runCmd("pm enable com.zte.camera;"
						+ "pm disable com.android.camera");
			}
			mSwitchCamera.setSummary(mSwitchCamera.getEntries()[index]);
			Settings.System.putInt(getActivity().getContentResolver(),"choose_what_3", index);
			Toast.makeText(getActivity().getApplicationContext(), "切换相机完成，你现在可以使用了。", 0).show();
			return true;
		}
		return false;
	}
	
	public boolean IsInstall(String packName){
		PackageInfo pInfo = null;
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(packName, PackageManager.GET_UNINSTALLED_PACKAGES);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			pInfo = null;
		}
		if (pInfo == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public static int SettingSystemGetInt(ContentResolver cr, String key, int defValues){
		return Settings.System.getInt(cr,key, defValues);
	}
	
	public static void SettingSystemPutInt(ContentResolver cr, String key, int values){
		Settings.System.putInt(cr, key, values);
	}
	
	public static void DialogShow(Context context){
		DialogShow(context,"1");
	}
	
	public static void DialogShow(Context context,String msg){
		AlertDialog.Builder mDialogBuilder = new Builder(context);
		if (msg.equals("1")) {
			mDialogBuilder.setMessage("你必须重启后才能完整生效设置!");
		}else {
			mDialogBuilder.setMessage(msg);
		}
		mDialogBuilder.setCancelable(false);
		mDialogBuilder.setPositiveButton("确定重启", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Utils.rebootForWhat(0);
			}
		});
		mDialogBuilder.show();
	}
}
