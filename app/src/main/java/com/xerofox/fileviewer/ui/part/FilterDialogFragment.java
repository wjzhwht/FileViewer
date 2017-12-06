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
import com.xerofox.fileviewer.util.ToastUtils;
import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

public class FilterDialogFragment extends BottomSheetDialogFragment implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> childFragmentInjector;
    private FilterDialogFragmentBinding binding;
    private Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    interface Callback {
        void callback(MenuFilter filter);
    }

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

        binding.done.setOnClickListener(v -> done());
        binding.clear.setOnClickListener(v -> clear());

    }

    private void done() {
        try {
            String segStr = binding.segStr.getText().toString().trim();
            List<Integer> segList = SegStringParser.parse(segStr);

            List<String> materials = getMaterials();

            int buttonId1 = binding.groupCutAngel.getCheckedRadioButtonId();
            int buttonId2 = binding.groupZhiwan.getCheckedRadioButtonId();
            int buttonId3 = binding.groupWeld.getCheckedRadioButtonId();
            int buttonId4 = binding.groupKaihe.getCheckedRadioButtonId();

            MenuFilter filter = new MenuFilter() {

                @Override
                public String getText() {
                    return "";
                }

                @Override
                public boolean match(TowerPart part) {
                    int segNo = Integer.parseInt(part.getSegStr(), 16);
                    boolean b = segList.isEmpty() || segList.contains(segNo);

                    boolean b1 = materials.isEmpty() || materials.contains(part.getMaterialMark());

                    boolean b2 = true;
                    if (buttonId1 == R.id.manu_cut_angel_yes){
                        b2 = part.getManuHourCutAngle()>0;
                    } else if (buttonId1 == R.id.manu_cut_angel_no){
                        b2 = part.getManuHourCutAngle()==0;
                    }

                    boolean b3 = true;
                    if (buttonId2 ==R.id.manu_zhiwan_yes){
                        b3 = part.getManuHourZhiWan()>0;
                    } else if (buttonId2 ==R.id.manu_zhiwan_no){
                        b3 = part.getManuHourZhiWan()==0;
                    }

                    boolean b4 = true;
                    if (buttonId3 == R.id.manu_weld_yes){
                        b4 = part.getManuHourWeld()>0;
                    } else if (buttonId3 == R.id.manu_weld_no){
                        b4 = part.getManuHourWeld()==0;
                    }

                    boolean b5 = true;
                    if (buttonId4 == R.id.manu_kaihe_yes){
                        b5 = part.getManuHourKaiHe()>0;
                    } else if (buttonId4 == R.id.manu_kaihe_no){
                        b5 = part.getManuHourKaiHe()==0;
                    }
                    return b && b1 && (b2 && b3 && b4 && b5);
                }
            };
            if (callback != null) {
                callback.callback(filter);
            }
            dismiss();
        } catch (RuntimeException e) {
            ToastUtils.showToast(e.getMessage());
        }
    }

    @NonNull
    private List<String> getMaterials() {
        List<String> materials = new ArrayList<>(5);
        if (binding.checkMaterial1.isChecked()){
            materials.add(binding.checkMaterial1.getText().toString());
        }
        if (binding.checkMaterial2.isChecked()){
            materials.add(binding.checkMaterial2.getText().toString());
        }
        if (binding.checkMaterial3.isChecked()){
            materials.add(binding.checkMaterial3.getText().toString());
        }
        if (binding.checkMaterial4.isChecked()){
            materials.add(binding.checkMaterial4.getText().toString());
        }
        if (binding.checkMaterial5.isChecked()){
            materials.add(binding.checkMaterial5.getText().toString());
        }
        return materials;
    }

    private void clear() {
        MenuFilter filter = new MenuFilter() {
            @Override
            public String getText() {
                return "";
            }

            @Override
            public boolean match(TowerPart part) {
                return true;
            }
        };
        if (callback != null) {
            callback.callback(filter);
        }
        dismiss();
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
