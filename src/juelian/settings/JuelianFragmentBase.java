package juelian.settings;

import miui.app.ActionBar;
import android.os.Bundle;

public class JuelianFragmentBase extends miui.app.Activity {
	
	public static final String other = PreferenceOthers.class.getSimpleName();
	public static final String customize = PreferenceCustomize.class.getSimpleName();
	public static final String system = PreferenceSystem.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle arg0) {
		setTheme(miui.R.style.Theme_Light);
		super.onCreate(arg0);
		ActionBar actionBar = this.getActionBar();
		actionBar.setFragmentViewPagerMode(this, getFragmentManager());
		actionBar.addFragmentTab(customize, actionBar.newTab().setText(R.string.section1), PreferenceCustomize.class, null, false);
		actionBar.addFragmentTab(system, actionBar.newTab().setText(R.string.section2), PreferenceSystem.class, null, false);
		actionBar.addFragmentTab(other, actionBar.newTab().setText(R.string.section3), PreferenceOthers.class, null, false);
	}
}