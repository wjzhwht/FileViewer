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
import com.xerofox.fileviewer.api.XeroApiImpl;
import com.xerofox.fileviewer.api.XeroNetApi;
import com.xerofox.fileviewer.databinding.TowerPartActivityBinding;
import com.xerofox.fileviewer.ui.common.BaseActivity;
import com.xerofox.fileviewer.ui.viewer.ViewerActivity;
import com.xerofox.fileviewer.util.ToastUtils;
import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.Status;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class TowerPartActivity extends BaseActivity {

    public static final String ARG_TASK = "task";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private TowerPartViewModel viewModel;

    @Inject
    DataBindingComponent dataBindingComponent;
    TowerPartActivityBinding binding;
    TowerPartAdapter adapter;
    private PartMenuAdapter partMenuAdapter;

    private boolean checkUpdate;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.input_part_id));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
                viewModel.setFilter(3, new PartNoMenuFilter(query));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewModel.setFilter(3, new PartNoMenuFilter(newText));
                return true;
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
                FilterDialogFragment fragment = FilterDialogFragment.newInstance();
                fragment.setCallback(filter -> viewModel.setFilter(4, filter));
                fragment.show(getSupportFragmentManager(), "filter");
                return true;
            case R.id.download:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.tower_part_activity, dataBindingComponent);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TowerPartViewModel.class);

        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initFilter();
        adapter = new TowerPartAdapter(this::jumpViewer, this::onItemDownload);
        binding.list.setAdapter(adapter);
        initPartList();
        viewModel.setTask(getTask());
    }

    private void onItemDownload(TowerPart part) {
        new Thread(() -> {
            try {
                new XeroApiImpl().queryTaskParts(getTask().getId(), new int[]{part.getId()});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initFilter() {
        viewModel.getFilterTitles().observe(this, data -> {
            if (partMenuAdapter == null) {
                if (data != null && !data.isEmpty()) {
                    partMenuAdapter = new PartMenuAdapter(this, viewModel.getFilterTitles().getValue(), viewModel.getFilterLists().getValue(), this::onFilterItemClick);
                    binding.dropMenu.setMenuAdapter(partMenuAdapter, viewModel.getFilterTitles().getValue());
                }
            }
        });

    }

    private void onFilterItemClick(int position, MenuFilter menuFilter) {
        viewModel.setFilter(position, menuFilter);
        binding.dropMenu.setPositionIndicatorText(position, menuFilter.getText());
        binding.dropMenu.close();
    }

    private Task getTask() {
        return getIntent().getParcelableExtra(ARG_TASK);
    }

    private void jumpViewer(TowerPart part) {
        ArrayList<TowerPart> towerParts = viewModel.getTowerParts().getValue();
        int position = towerParts.indexOf(part);
        startActivity(ViewerActivity.newIntent(this, getTask(), towerParts, position));
    }

    private void initPartList() {
        viewModel.getTowerParts().observe(this, data -> {
            if (data != null && !data.isEmpty()) {
                checkUpdate(data);
                adapter.replace(data);
            } else {
                adapter.replace(Collections.emptyList());
            }
        });

        viewModel.getCheckUpdateParts().observe(this, resource -> {
            if (resource.status == Status.SUCCESS) {
                if (resource.data != null && !resource.data.isEmpty()) {
                    adapter.setUpdateParts(resource.data);
                    adapter.notifyDataSetChanged();
                }
            } else if (resource.status == Status.ERROR) {
                ToastUtils.showToast(resource.message);
            }
        });
    }

    private void checkUpdate(ArrayList<TowerPart> data) {
        if (!checkUpdate) {
            checkUpdate = true;
            viewModel.checkUpdate(getTask().getId(), data);
        }
    }
}
