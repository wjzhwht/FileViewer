package com.xerofox.fileviewer.ui.index;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.FunctionFragmentBinding;
import com.xerofox.fileviewer.ui.common.BaseFragment;

import javax.inject.Inject;

public class FunctionFragment extends BaseFragment {

    @Inject
    NavigationController navigationController;

    private FunctionFragmentBinding binding;

    public static FunctionFragment newInstance() {

        Bundle args = new Bundle();

        FunctionFragment fragment = new FunctionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DataBindingUtil.inflate(inflater, R.layout.function_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.app_name);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding.layoutLookOver.setOnClickListener(v -> navigationController.navigateToSearch());
        binding.layoutSettings.setOnClickListener(v -> navigationController.navigateToSettings());
    }
}
