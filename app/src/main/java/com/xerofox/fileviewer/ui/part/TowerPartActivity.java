package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.databinding.TowerPartActivityBinding;
import com.xerofox.fileviewer.ui.common.BaseActivity;
import com.xerofox.fileviewer.ui.viewer.ViewerActivity;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.Collections;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.HasSupportFragmentInjector;

public class TowerPartActivity extends BaseActivity {

    public static final String ARG_TOWER_TYPE = "tower_type";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private TowerPartViewModel towerPartViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    TowerPartActivityBinding binding;
    TowerPartAdapter adapter;

    public Intent newIntent(Context context, TowerPart towerPart) {
        Intent intent = new Intent(context, TowerPartActivity.class);
        intent.putExtra(ARG_TOWER_TYPE, towerPart);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tower_part_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.tower_part_activity, dataBindingComponent);
        towerPartViewModel = ViewModelProviders.of(this, viewModelFactory).get(TowerPartViewModel.class);

        adapter = new TowerPartAdapter(dataBindingComponent,
                part -> jumpViewer());
        binding.list.setAdapter(adapter);
        initPartList();
        TowerType type = getIntent().getParcelableExtra(ARG_TOWER_TYPE);
        towerPartViewModel.setTowerType(type);
    }

    private void jumpViewer() {
        TowerType towerType = towerPartViewModel.getTowerParts().getValue();
        startActivity(ViewerActivity.newIntent(this, towerType));
    }

    private void initPartList() {
        towerPartViewModel.getTowerParts().observe(this, data -> {
            if (data != null) {
                adapter.replace(data.getPartArr());
            } else {
                adapter.replace(Collections.emptyList());
            }
        });
        towerPartViewModel.geRootPath().observe(this, path -> adapter.setRootPath(path));
    }

}
