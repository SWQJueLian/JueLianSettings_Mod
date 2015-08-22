package juelian.settings;
import java.util.Iterator;

import android.R.integer;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PreferenceSystem extends PreferenceFragment implements OnPreferenceChangeListener {
	
	private Preference mScreenFilter;
	public static final String KEY_SCREENFILTER = "sf";
	
	private Preference mNetWorkType;
	public static final String KEY_NETWORK_TYPE = "networktype";

	private Preference mPC;
	public static final String KEY_PC = "pc";
	
	private Preference mDefaultApp;
	public static final String KEY_APP = "default_app";
	
    private Preference mPppoe;
    public static final String KEY_PPPOE = "pppoe";
    
    private Preference mWIFISHARE;
    private static final String KEY_WIFISHANRE = "wifi";
    
    private Preference mPOWERMGR;
    private static final String KEY_POWERMGR = "powermgr";
    
    private Preference mQuickOn;
    public static final String KEY_QUICKON = "quickon";
    
    private Preference mTimerPower;
    public static final String KEY_TIMERPOWER = "timerpower";

    private PreferenceScreen mAppMgr;
    public static final String KEY_APP_MGR = "appmgr";

    private ListPreference mHomeLayout;
    public static final String KEY_HOME_LAYOUT = "home_layout";
    
    private Preference mSfIcon;
    public static final String KEY_SF_ICON = "sficon";
    
    private ContentResolver cr;

    
	@Override
	public void onCreate(Bundle arg0) {
		getActivity().setTheme(miui.R.style.Theme_Light_Settings);
		super.onCreate(arg0);
        addPreferencesFromResource(R.xml.system_settings_preference);
        
        cr = getActivity().getContentResolver();

        PreferenceScreen pfs = getPreferenceScreen();
        
		mAppMgr = (PreferenceScreen) pfs.findPreference(KEY_APP_MGR);
		
        mPppoe = pfs.findPreference(KEY_PPPOE);
        
        mWIFISHARE = pfs.findPreference(KEY_WIFISHANRE);
        
        mPOWERMGR = pfs.findPreference(KEY_POWERMGR);
        
        mQuickOn = pfs.findPreference(KEY_QUICKON);
        
        mTimerPower = pfs.findPreference(KEY_TIMERPOWER);
        
        mPC = pfs.findPreference(KEY_PC);//com.android.jlsettings.connectpc.ConnectPCType
        

        
       //getActivity().getCurrentFocus().setFitsSystemWindows(true);
        
        mDefaultApp = pfs.findPreference(KEY_APP);
        
        mNetWorkType = pfs.findPreference(KEY_NETWORK_TYPE);
        
        mScreenFilter = pfs.findPreference(KEY_SCREENFILTER);
        
        int k = PreferenceCustomize.SettingSystemGetInt(cr, "choose_what_4", 2);
        mHomeLayout = (ListPreference) pfs.findPreference(KEY_HOME_LAYOUT);
        mHomeLayout.setSummary(mHomeLayout.getEntries()[k]);
        mHomeLayout.setOnPreferenceChangeListener(this);
        mSfIcon = pfs.findPreference(KEY_SF_ICON);
        
		rmPreList(new String[] { KEY_PPPOE, KEY_WIFISHANRE, KEY_APP });
	}
	
	private void rmPreList(String keyList[]){
		for (String k : keyList) {
			getPreferenceScreen().removePreference(findPreference(k));
		}
	}
    private void showNotFoundToast(){
    	Toast.makeText(getActivity().getApplicationContext(), R.string.not_found_app, 1).show();
    }
    
    /**
     * @param subClassName based on com.android.jlsettings.xxxxxx
     * @param actionName what action you want start?
     */
    private boolean myStartActivity(String subClassName,String actionName){
		try {
			Intent intent = new Intent(actionName);
			intent.setClassName(PreferenceCustomize.ZTESETTINGSPACKNAME, PreferenceCustomize.ZTESETTINGSPACKNAME+subClassName);
			startActivity(intent);
			return true;
		} catch (Exception e) {
			showNotFoundToast();
			return false;
		}
    }
    /**
     * @param subClassName based on com.android.jlsettings.xxxxxx
     * @param actionName what action you want start?
     */
    private boolean myStartActivity(String subClassName,String actionName,int flags){
		try {
			Intent intent = new Intent(actionName);
			intent.setFlags(flags);
			intent.setClassName(PreferenceCustomize.ZTESETTINGSPACKNAME, PreferenceCustomize.ZTESETTINGSPACKNAME+subClassName);
			startActivity(intent);
			return true;
		} catch (Exception e) {
			showNotFoundToast();
			return false;
		}
    }
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if (preference == mSfIcon) {
			try {
				Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
				//图标名
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.sf_sc));
				//图标资源
				shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getActivity(), R.drawable.sf));
				//启动的intent
				Intent intentStartActivity = new Intent(Intent.ACTION_MAIN);
				intentStartActivity.setClassName("com.haxor", "com.haxor.ScreenFilter");
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
		if (preference == mScreenFilter) {
			Intent intent3 = new Intent(Intent.ACTION_MAIN);
			intent3.setClassName("com.haxor", "com.haxor.ScreenFilter");
			intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent3);
			return true;
		}
		if (preference == mAppMgr) {
			return myStartActivity(".applications.ManageApplications", Intent.ACTION_MAIN);
		}
		
		if (preference == mDefaultApp) {
			return myStartActivity(".applications.AppDefaultSettings", "android.intent.action.APP_MULTI_SELECTION_NOTIFICATION");
		}
		if (preference == mPC) {
			return myStartActivity(".connectpc.ConnectPCType",Intent.ACTION_MAIN);
		}
		

		
		if (preference == mPOWERMGR) {
			try {
				Intent intent = new Intent();
				intent.setClassName("com.zte.powersavemode", "com.zte.powersavemode.power.PowerSaveModeActivity");
				startActivity(intent);
				return true;
			} catch (Exception e) {
				showNotFoundToast();
				return false;
			}
		}
		
		if (preference == mQuickOn) {
			return myStartActivity(".QuickStartHelper",Intent.ACTION_MAIN);
			
		}
		if (preference == mTimerPower) {
			return myStartActivity(".powercontrol.ScheduledPowerOnOffActivity" ,Intent.ACTION_MAIN);
			
		}
		
		
		if (preference == mWIFISHARE) {
			return myStartActivity(".wifi.HotspotSettings",Intent.ACTION_MAIN);
			
		}
		
		if (preference == mPppoe) {
			return myStartActivity(".pppoe.PppoeSettings",Intent.ACTION_MAIN);
			
		}
		
		if (preference == mNetWorkType) {
			return myStartActivity(".RadioInfo", Intent.ACTION_MAIN, Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		
		
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mHomeLayout) {
			int index = mHomeLayout.findIndexOfValue((String) newValue);
			Utils.runCmd("mount -o rw,remount /dev/block/platform/msm_sdcc.1/by-name/system /system");
			switch (index) {
			case 0:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home34 /system/media/theme/default/com.miui.home");
				break;
			case 1:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home44 /system/media/theme/default/com.miui.home");
				break;
			case 2:
				Utils.runCmd("rm -f /system/media/theme/default/com.miui.home");
				break;
			case 3:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home46 /system/media/theme/default/com.miui.home");
				break;
			case 4:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home55 /system/media/theme/default/com.miui.home");
				break;
			case 5:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home56 /system/media/theme/default/com.miui.home");
				break;
			case 6:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home57 /system/media/theme/default/com.miui.home");
				break;
			case 7:
				Utils.runCmd("cp /system/juelian/homelayout/com.miui.home68 /system/media/theme/default/com.miui.home");
				break;
			}
			Utils.runCmd("mount -o ro,remount /dev/block/platform/msm_sdcc.1/by-name/system /system");
			Utils.runCmd("busybox pkill com.miui.home");
			Settings.System.putInt(getActivity().getContentResolver(),"choose_what_4", index);
			mHomeLayout.setSummary(mHomeLayout.getEntries()[index]);
			return true;
		}
		return false;
	}


}
