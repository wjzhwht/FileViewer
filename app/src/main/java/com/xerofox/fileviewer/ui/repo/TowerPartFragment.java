package com.xerofox.fileviewer.ui.repo;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.databinding.TowerPartFragmentBinding;
import com.xerofox.fileviewer.di.Injectable;
import com.xerofox.fileviewer.ui.common.NavigationController;
import com.xerofox.fileviewer.util.AutoClearedValue;

import java.util.Collections;

import javax.inject.Inject;

public class TowerPartFragment extends Fragment implements Injectable {

    private static final String REPO_OWNER_KEY = "repo_owner";

    private static final String REPO_NAME_KEY = "repo_name";

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
        Bundle args = getArguments();

        TowerPartAdapter adapter = new TowerPartAdapter(dataBindingComponent,
                contributor -> {
                });
        this.adapter = new AutoClearedValue<>(this, adapter);
        binding.get().list.setAdapter(adapter);
        initPartList();
    }

    private void initPartList() {
        towerPartViewModel.getTowerParts().observe(this, data -> {
            if (data != null) {
                adapter.get().replace(data);
            } else {
                adapter.get().replace(Collections.emptyList());
            }
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

    public static TowerPartFragment create(String owner, String name) {
        TowerPartFragment repoFragment = new TowerPartFragment();
        Bundle args = new Bundle();
        args.putString(REPO_OWNER_KEY, owner);
        args.putString(REPO_NAME_KEY, name);
        repoFragment.setArguments(args);
        return repoFragment;
    }
}
