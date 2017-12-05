package com.xerofox.fileviewer.ui.part;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.FilterDialogFragmentBinding;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public class FilterDialogFragment extends BottomSheetDialogFragment implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;
    private FilterDialogFragmentBinding binding;

    public static FilterDialogFragment newInstance() {

        Bundle args = new Bundle();

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

        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.filter_dialog_fragment, null, false);
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
        binding.done.setOnClickListener(v -> {

        });
        binding.clear.setOnClickListener(v -> {

        });
        binding.checkAllMaterial.setOnClickListener(v -> {
            boolean checked = binding.checkAllMaterial.isChecked();
            binding.checkMaterial1.setChecked(checked);
            binding.checkMaterial2.setChecked(checked);
            binding.checkMaterial3.setChecked(checked);
            binding.checkMaterial4.setChecked(checked);
            binding.checkMaterial5.setChecked(checked);
        });
        binding.checkMaterial1.setOnClickListener(v -> updateMaterialAll());
        binding.checkMaterial2.setOnClickListener(v -> updateMaterialAll());
        binding.checkMaterial3.setOnClickListener(v -> updateMaterialAll());
        binding.checkMaterial4.setOnClickListener(v -> updateMaterialAll());
        binding.checkMaterial5.setOnClickListener(v -> updateMaterialAll());


        binding.manuAllYes.setOnClickListener(v -> updateManu(true));
        binding.manuAllNo.setOnClickListener(v -> updateManu(false));

        binding.manuCutAngelYes.setOnClickListener(v -> updateManuAll());
        binding.manuCutAngelNo.setOnClickListener(v -> updateManuAll());
        binding.manuZhiwanYes.setOnClickListener(v -> updateManuAll());
        binding.manuZhiwanNo.setOnClickListener(v -> updateManuAll());
        binding.manuWeldYes.setOnClickListener(v -> updateManuAll());
        binding.manuWeldNo.setOnClickListener(v -> updateManuAll());
        binding.manuKaiheYes.setOnClickListener(v -> updateManuAll());
        binding.manuKaiheNo.setOnClickListener(v -> updateManuAll());

    }

    private void updateManuAll() {
        boolean b = binding.groupCutAngel.getCheckedRadioButtonId() == R.id.manu_cut_angel_yes;
        boolean b1 = binding.groupZhiwan.getCheckedRadioButtonId() == R.id.manu_zhiwan_yes;
        boolean b2 = binding.groupWeld.getCheckedRadioButtonId() == R.id.manu_weld_yes;
        boolean b3 = binding.groupKaihe.getCheckedRadioButtonId() == R.id.manu_kaihe_yes;
        if (b && b1 && b2 && b3) {
            binding.groupManuAll.check(R.id.manu_all_yes);
        } else if (!b && !b1 && !b2 && !b3) {
            binding.groupManuAll.check(R.id.manu_all_no);
        } else {
            binding.groupManuAll.clearCheck();
        }
    }

    private void updateManu(boolean check) {
        binding.groupCutAngel.check(check ? R.id.manu_cut_angel_yes : R.id.manu_cut_angel_no);
        binding.groupZhiwan.check(check ? R.id.manu_zhiwan_yes : R.id.manu_zhiwan_no);
        binding.groupWeld.check(check ? R.id.manu_weld_yes : R.id.manu_weld_no);
        binding.groupKaihe.check(check ? R.id.manu_kaihe_yes : R.id.manu_kaihe_no);
    }

    private void updateMaterialAll() {
        boolean checked1 = binding.checkMaterial1.isChecked();
        boolean checked2 = binding.checkMaterial2.isChecked();
        boolean checked3 = binding.checkMaterial3.isChecked();
        boolean checked4 = binding.checkMaterial4.isChecked();
        boolean checked5 = binding.checkMaterial5.isChecked();
        binding.checkAllMaterial.setChecked(checked1 && checked2 && checked3 && checked4 && checked5);

    }

}
