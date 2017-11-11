package com.xerofox.fileviewer.ui.tower;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.databinding.TowerPartFragmentBinding;
import com.xerofox.fileviewer.di.Injectable;
import com.xerofox.fileviewer.ui.common.NavigationController;
import com.xerofox.fileviewer.util.AutoClearedValue;
import com.xerofox.fileviewer.util.ToastUtils;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.Collections;

import javax.inject.Inject;

public class TowerPartFragment extends Fragment implements Injectable {

    private static final String ARG_TOWER_TYPE = "tower type";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private TowerPartViewModel towerPartViewModel;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    AutoClearedValue<TowerPartFragmentBinding> binding;
    AutoClearedValue<TowerPartAdapter> adapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        towerPartViewModel = ViewModelProviders.of(this, viewModelFactory).get(TowerPartViewModel.class);

        TowerPartAdapter adapter = new TowerPartAdapter(dataBindingComponent,
                part -> ToastUtils.showToast(part.getPartFile().getName()));
        this.adapter = new AutoClearedValue<>(this, adapter);
        binding.get().list.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.get().list.setAdapter(adapter);
        initPartList();
        TowerType type = getArguments().getParcelable(ARG_TOWER_TYPE);
        towerPartViewModel.setTowerType(type);
    }

    private void initPartList() {
        towerPartViewModel.getTowerParts().observe(this, data -> {
            if (data != null) {
                adapter.get().replace(data.getPartArr());
            } else {
                adapter.get().replace(Collections.emptyList());
            }
        });
        towerPartViewModel.geRootPath().observe(this, path -> {
            adapter.get().setRootPath(path);
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TowerPartFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.tower_part_fragment, container, false);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    public static TowerPartFragment create(TowerType towerType) {
        TowerPartFragment repoFragment = new TowerPartFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TOWER_TYPE, towerType);
        repoFragment.setArguments(args);
        return repoFragment;
    }
}
