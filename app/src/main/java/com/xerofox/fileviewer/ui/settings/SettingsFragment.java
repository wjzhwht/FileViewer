package com.xerofox.fileviewer.ui.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.SettingsFragmentBinding;
import com.xerofox.fileviewer.helper.SettingHelper;
import com.xerofox.fileviewer.ui.common.BaseFragment;

public class SettingsFragment extends BaseFragment {
    private SettingsFragmentBinding binding;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false);
        initInputListener();
        return binding.getRoot();
    }

    private void initInputListener() {
        binding.ip.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = v.getText().toString().trim();
                SettingHelper.setServerPort(text);
                return true;
            }
            return false;
        });
        binding.ip.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String text = ((EditText) v).getText().toString().trim();
                SettingHelper.setServerPort(text);
                return true;
            }
            return false;
        });

        binding.name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = v.getText().toString().trim();
                SettingHelper.setUserName(text);
                return true;
            }
            return false;
        });

        binding.name.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String text = ((EditText) v).getText().toString().trim();
                SettingHelper.setUserName(text);
                return true;
            }
            return false;
        });

        binding.password.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String text = v.getText().toString().trim();
                SettingHelper.setPassword(text);
                return true;
            }
            return false;
        });

        binding.password.setOnKeyListener((View v, int keyCode, KeyEvent event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String text = ((EditText) v).getText().toString().trim();
                SettingHelper.setPassword(text);
                return true;
            }
            return false;
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.settings);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.setIp(SettingHelper.getServerPort());
        binding.setName(SettingHelper.getUserName());
        binding.setPassword(SettingHelper.getPassword());
        binding.executePendingBindings();
        binding.ok.setOnClickListener(v -> {
            SettingHelper.setServerPort(binding.ip.getText().toString().trim());
            SettingHelper.setUserName(binding.name.getText().toString().trim());
            SettingHelper.setPassword(binding.password.getText().toString().trim());
        });
    }
}
