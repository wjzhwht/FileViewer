package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.databinding.TowerPartActivityBinding;
import com.xerofox.fileviewer.ui.common.BaseActivity;
import com.xerofox.fileviewer.ui.viewer.ViewerActivity;
import com.xerofox.fileviewer.util.ToastUtils;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class TowerPartActivity extends BaseActivity {

    public static final String ARG_TASK = "task";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private TowerPartViewModel towerPartViewModel;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    TowerPartActivityBinding binding;
    TowerPartAdapter adapter;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.input_part_id));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ToastUtils.showToast(query);
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
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
                FilterDialogFragment fragment = FilterDialogFragment.newInstance(new FilterDialogFragment.ViewModelProvider() {
                    @Override
                    TowerPartViewModel viewModel() {
                        return towerPartViewModel;
                    }
                });
                fragment.show(getSupportFragmentManager(), "filter");
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

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new TowerPartAdapter(dataBindingComponent,
                part -> jumpViewer(part));
        binding.list.setAdapter(adapter);
        initPartList();
        towerPartViewModel.setTask(getTask());
    }

    private Task getTask() {
        return getIntent().getParcelableExtra(ARG_TASK);
    }

    private void jumpViewer(TowerPart part) {
        ArrayList<TowerPart> towerParts = towerPartViewModel.getTowerParts().getValue();
        int position = towerParts.indexOf(part);
        startActivity(ViewerActivity.newIntent(this, getTask(), towerParts, position));
    }

    private void initPartList() {
        towerPartViewModel.getTowerParts().observe(this, data -> {
            if (data != null) {
                adapter.replace(data);
            } else {
                adapter.replace(Collections.emptyList());
            }
        });
    }
}
