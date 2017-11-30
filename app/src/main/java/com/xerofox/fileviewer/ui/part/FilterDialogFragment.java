package com.xerofox.fileviewer.ui.part;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.FilterDialogFragmentBinding;

import java.util.Collections;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public class FilterDialogFragment extends BottomSheetDialogFragment implements HasSupportFragmentInjector {
    private static final String ARG_VIEW_MODEL = "view model";

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;

    abstract static class ViewModelProvider implements Parcelable {
        abstract TowerPartViewModel viewModel();

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }

    public static FilterDialogFragment newInstance(ViewModelProvider viewModelProvider) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_VIEW_MODEL, viewModelProvider);

        FilterDialogFragment fragment = new FilterDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return childFragmentInjector;
    }

    protected final BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        FilterDialogFragmentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.filter_dialog_fragment, null, false);
        initView(binding);
        dialog.setContentView(binding.getRoot());

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) binding.getRoot().getParent())
                .getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    protected void initView(FilterDialogFragmentBinding binding) {
        binding.clear.setOnClickListener(v -> {
//            getViewModelProvider().viewModel().clearFilters();
            dismiss();
        });
        binding.done.setOnClickListener(v -> {
//            getViewModelProvider().viewModel().doFilters();
            dismiss();
        });
        FilterAdapter adapter = new FilterAdapter(getActivity());
        binding.list.setAdapter(adapter);
//        getViewModelProvider().viewModel().getFilters().observe(this, data -> {
//            if (data == null) {
//                adapter.replace(Collections.emptyList());
//            } else {
//                adapter.replace(data);
//            }
//        });
//        adapter.replace(getViewModelProvider().viewModel().getFilters().getValue());
    }

    private ViewModelProvider getViewModelProvider() {
        return getArguments().getParcelable(ARG_VIEW_MODEL);
    }

}
