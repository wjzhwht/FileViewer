package com.xerofox.fileviewer.ui.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.SettingsActivityBinding;
import com.xerofox.fileviewer.ui.common.BaseActivity;

public class SettingActivity extends BaseActivity {
    private SettingsActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.settings_activity);
        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
