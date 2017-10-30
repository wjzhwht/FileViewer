package com.xerofox.fileviewer.ui.init;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.EditorInfo;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.ActivityInitializationBinding;
import com.xerofox.fileviewer.ui.BaseActivity;
import com.xerofox.fileviewer.util.KeyboardUtil;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class InitializationActivity extends BaseActivity implements HasSupportFragmentInjector{
    @Inject
    ViewModelProvider.Factory factory;

    ActivityInitializationBinding binding;
    InitializationViewModel viewModel;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_initialization);
        viewModel = ViewModelProviders.of(this, factory).get(InitializationViewModel.class);
        initStatus();
        binding.port.setOnEditorActionListener((view, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtil.hideSoftInput(view);
                doInit();
                return true;
            }
            return false;
        });
        binding.ok.setOnClickListener(view -> doInit());
    }

    private void initStatus() {
        viewModel.getStatus().observe(this, status -> {
            if (status == null) {
            }
        });
    }

    private void doInit() {
        String port = binding.port.getText().toString().trim();
        viewModel.doInit(port);
    }
}

