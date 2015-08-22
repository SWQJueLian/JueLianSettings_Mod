package juelian.settings;


import android.R.integer;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class PreferenceOthers extends PreferenceFragment implements OnPreferenceChangeListener {
	
	private CheckBoxPreference mCameraSoundBoxPreference;
	public static final String KEY_CAMERA_SOUND_STRING = "camera_sound";

	private Preference mDobly;
	public static final String KEY_DOUBLY = "dobly";
	
	private CheckBoxPreference mRing;
	public static final String KEY_RING = "ring";
	
	private CheckBoxPreference mBootSound;
	public static final String KEY_SOUND = "sound";
	
	private Preference mRebootWhat;
	public static final String KEY_REBOOT_STRING= "rb";
	
    private Preference mLauncherIcon;
    public static final String KEY_LAUNCHER = "icon";
    
    private ListPreference mStorage;
    public static final String KEY_STORAGE = "storage";

    /*
    private ListPreference mImmerBarColor;
    public static final String KEY_Color = "color";
    */
    public String[] titleString = {"重启","重启到Recovery","快速重启","关机"};
    
    
    
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		getActivity().setTheme(miui.R.style.Theme_Light_Settings);
		super.onCreate(arg0);
        addPreferencesFromResource(R.xml.others_settings_preference);

        PreferenceScreen pfs = getPreferenceScreen();
		
		mDobly = pfs.findPreference(KEY_DOUBLY);
		mBootSound = (CheckBoxPreference) pfs.findPreference(KEY_SOUND);
		mRing = (CheckBoxPreference) pfs.findPreference(KEY_RING);
		mCameraSoundBoxPreference = (CheckBoxPreference) pfs.findPreference(KEY_CAMERA_SOUND_STRING);
        mCameraSoundBoxPreference.setChecked(Settings.System.getInt(getActivity().getContentResolver(), "camera_sound_switch", 0) == 1 ? true : false);

		mRing.setChecked(Settings.System.getInt(getActivity().getContentResolver(), "ring_increasing_enabled", 0) == 1 ? true : false);
		
		mStorage = (ListPreference) pfs.findPreference(KEY_STORAGE);
		mStorage.setOnPreferenceChangeListener(this);
		mStorage.setSummary(mStorage.getEntries()[Settings.System.getInt(getActivity().getContentResolver(), "save_location",0)]);

        mRebootWhat = pfs.findPreference(KEY_REBOOT_STRING);
        
        mLauncherIcon = pfs.findPreference(KEY_LAUNCHER);


	}
    private void showNotFoundToast(){
    	Toast.makeText(getActivity().getApplicationContext(), R.string.not_found_app, 1).show();
    }
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if (preference == mLauncherIcon) {
			try {
				Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
				//图标名
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
				//图标资源
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getActivity(), R.drawable.icon));
				//启动的intent
				Intent intentStartActivity = new Intent(getActivity(), JuelianFragmentBase.class);
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intentStartActivity);
				//发送广播
				getActivity().sendBroadcast(shortcut);
				Toast.makeText(getActivity(), "发送快捷方式成功", 0).show();
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				Log.d("juelian96", "send shorcut fail");
				Toast.makeText(getActivity(), "发送快捷方式失败", 0).show();
				return false;
			}
			
		}
		
		if (preference == mRebootWhat) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			
			dialog.setSingleChoiceItems(titleString, -1, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					final int tag = which;
					AlertDialog.Builder dialog2 = new AlertDialog.Builder(getActivity());
					dialog2.setTitle("提示");
					dialog2.setMessage("您确定要"+titleString[which]+"吗?");
					dialog2.setPositiveButton(android.R.string.ok, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//Toast.makeText(getActivity(), "哈哈"+which, 0).show();
							// {"重启","重启到Recovery","快速重启","关机"};
							switch (tag) {
							case 0:
								Utils.rebootForWhat(0);
								break;
							case 1:
								Utils.rebootForWhat(2);
								break;
							case 2:
								Utils.rebootForWhat(4);
								break;
							case 3:
								Utils.rebootForWhat(1);
								break;
							}
						}
					});
					dialog2.setNegativeButton(android.R.string.cancel, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					dialog2.show();
					dialog.dismiss();
				}
			});
			
			dialog.setNegativeButton(android.R.string.cancel, new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			});
			
			dialog.show();
			return true;
		}
		
		if (preference == mCameraSoundBoxPreference) {
			Utils.runCmd("mount -o rw,remount /dev/block/platform/msm_sdcc.1/by-name/system /system");
			if (mCameraSoundBoxPreference.isChecked()) {
				Utils.runCmd("mv /system/media/audio/ui/camera_click.ogg /system/media/audio/ui/camera_click.ogg.bak;" +
						"mv /system/media/audio/ui/camera_focus.ogg /system/media/audio/ui/camera_focus.ogg.bak");				
			}else {
				Utils.runCmd("mv /system/media/audio/ui/camera_click.ogg.bak /system/media/audio/ui/camera_click.ogg;" +
						"mv /system/media/audio/ui/camera_focus.ogg.bak /system/media/audio/ui/camera_focus.ogg");
			}
			Utils.runCmd("mount -o ro,remount /dev/block/platform/msm_sdcc.1/by-name/system /system");
			Settings.System.putInt(getActivity().getContentResolver(), "camera_sound_switch", mCameraSoundBoxPreference.isChecked() ? 1 : 0);
			return true;
		}
		if (preference == mBootSound) {
			Utils.runCmd("setprop persist.sys.silent_powerOn "+String.valueOf(mBootSound.isChecked()));
			return true;
		}
		if (preference == mRing) {
			Settings.System.putInt(getActivity().getContentResolver(),"ring_increasing_enabled", mRing.isChecked() ? 1 : 0);
			//SettingSystemPutInt(cr, "ring_increasing_enabled", mRing.isChecked() ? 1 : 0);
			return true;
		}
		if (preference == mDobly) {
			try {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setClassName("com.dolby.ds1appUI","com.dolby.ds1appUI.MainActivity");
				startActivity(intent);
			} catch (Exception e) {
				showNotFoundToast();
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mStorage) {
			int index = mStorage.findIndexOfValue((String) newValue);
			Settings.System.putInt(getActivity().getContentResolver(), "save_location", index);
			mStorage.setSummary(mStorage.getEntries()[index]);
			PreferenceCustomize.DialogShow(getActivity());
			return true;
		}
		return false;
	}

}
