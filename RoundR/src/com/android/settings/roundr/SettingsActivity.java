package com.android.settings.roundr;

import wei.mark.standout.StandOutWindow;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.WindowManager.LayoutParams;

public class SettingsActivity extends PreferenceActivity {
	static Intent mBkService = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		//registerCheckBoxChangeListener(R.string.pref_lock_on_key);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		//Intent yun = new Intent(this, kbLight.class);
		//startActivity(yun);
		((Preference) findPreference(getString(R.string.pref_lock_on_key))).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean isChecked = (Boolean) newValue;
				LightsController lc = new LightsController();
				if (isChecked) {
					lc.lockOnButtonBkLight();
				//	startBkService(this);
					 if (mBkService == null)
				        {
						 Intent mBkService = new Intent(getApplicationContext(), BkService.class);
							startService(mBkService);
							
				        }
				} else {
					lc.lockOffButtonBkLight();
					 if (mBkService == null)
				        {
						 Intent mBkService = new Intent(SettingsActivity.this, BkService.class);
							stopService(mBkService);
				        }
				}
				return true;
			}
			private void stopBkService(Context cntxt) {
				if (mBkService != null)
		        {
		            cntxt.stopService(mBkService);
		            mBkService = null;
		        }
				
			}
			private void startBkService(Context cntxt) {
				if (mBkService == null)
    	        {
    	            mBkService = new Intent(cntxt.getApplicationContext(), BkService.class);
    	            cntxt.startService(mBkService);
    	        }
				// TODO Auto-generated method stub
				
			}
			
		});
		((Preference) findPreference("enable")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean isChecked = (Boolean) newValue;
				if (isChecked) {
					StandOutWindow.show(SettingsActivity.this, Corner.class, 0);
					StandOutWindow.show(SettingsActivity.this, Corner.class, 1);
					StandOutWindow.show(SettingsActivity.this, Corner.class, 2);
					StandOutWindow.show(SettingsActivity.this, Corner.class, 3);
				} else {
					StandOutWindow.closeAll(SettingsActivity.this, Corner.class);
				}
				return true;
			}
		});
		
		
		((Preference) findPreference("overlap2")).setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean isChecked = (Boolean) newValue;
				if (isChecked) {
					if (prefs.getBoolean("overlap", true))
						prefs.edit().putInt("flags", LayoutParams.FLAG_SHOW_WHEN_LOCKED | LayoutParams.FLAG_LAYOUT_IN_SCREEN).commit();
					else
						prefs.edit().putInt("flags", LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | LayoutParams.FLAG_LAYOUT_IN_SCREEN).commit();
				} else {
					if (prefs.getBoolean("overlap", true))
						prefs.edit().putInt("flags", LayoutParams.FLAG_SHOW_WHEN_LOCKED).commit();
					else
						prefs.edit().putInt("flags", LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH).commit();
				}
				new Thread(new Runnable() {

					@Override
					public void run() {
						StandOutWindow.closeAll(SettingsActivity.this, Corner.class);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						StandOutWindow.show(SettingsActivity.this, Corner.class, 0);
						StandOutWindow.show(SettingsActivity.this, Corner.class, 1);
						StandOutWindow.show(SettingsActivity.this, Corner.class, 2);
						StandOutWindow.show(SettingsActivity.this, Corner.class, 3);
					}

				}).start();
				return true;
			}
		});
		/**
		 * TODO: Figure out if Developer Options is enabled. If so, show a
		 * GitHub Source Code Link preference:
		 * "Seems like you are a developer? Check out the RoundR source code on GitHub!"
		 */
	}
	
	/*
	 * Sends a signal to all the corners to refresh their layout parameters,
	 * which in turn refreshes their size.
	 */
	public void refresh() {
		StandOutWindow.sendData(this, Corner.class, Corner.wildcard, Corner.UPDATE_CODE, new Bundle(), Corner.class, StandOutWindow.DISREGARD_ID);
	}

	@SuppressLint("InlinedApi")
	public void showInstalledAppDetails(String packageName) {
		Intent intent = new Intent();
		intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		Uri uri = Uri.fromParts("package", packageName, null);
		intent.setData(uri);
		startActivity(intent);
	}
	
}
