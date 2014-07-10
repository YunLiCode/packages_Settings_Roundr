package com.android.settings.roundr;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.android.settings.roundr.R;

public class kbLight extends PreferenceActivity implements OnPreferenceChangeListener {
	static Intent mBkService = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        System.out.print("holo");
        registerCheckBoxChangeListener(R.string.pref_lock_on_key);
    }
    
    private void registerCheckBoxChangeListener(int resId)
    {
        CheckBoxPreference cbPref = (CheckBoxPreference) findPreference(getString(resId));
        if (cbPref != null)
        {
            cbPref.setOnPreferenceChangeListener(this);
        }
    }
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
        String strPrefKey = preference.getKey();
    		LightsController lc = new LightsController();
    		if ((Boolean)newValue.equals(true))
    		{
    			lc.lockOnButtonBkLight();
                	startBkService(this);
    		}
    		else if((Boolean)newValue.equals(false))
    		{
    			lc.lockOffButtonBkLight();
                CheckBoxPreference cbPref = (CheckBoxPreference) findPreference(getString(R.string.pref_lock_on_key));
                cbPref.setChecked(false);

                stopBkService(this);
    		}
    		return true;
	}
    private void startBkService(Context cntxt)
    {
        if (mBkService == null)
        {
            mBkService = new Intent(cntxt.getApplicationContext(), BkService.class);
            cntxt.startService(mBkService);
        }
    }
    private void stopBkService(Context cntxt)
    {
        if (mBkService != null)
        {
            cntxt.stopService(mBkService);
            mBkService = null;
        }
    }
}