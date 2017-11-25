package com.xerofox.fileviewer.ui.index;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.databinding.SearchFragmentBinding;
import com.xerofox.fileviewer.ui.common.BaseFragment;
import com.xerofox.fileviewer.ui.part.TowerPartActivity;
import com.xerofox.fileviewer.util.AutoClearedValue;

import javax.inject.Inject;

public class SearchFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(getActivity());

    AutoClearedValue<SearchFragmentBinding> binding;

    AutoClearedValue<TaskListAdapter> adapter;

    private SearchViewModel searchViewModel;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                return true;
            case R.id.settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        initRecyclerView();
        TaskListAdapter rvAdapter = new TaskListAdapter(dataBindingComponent, task -> {
            Intent intent = new Intent(getActivity(), TowerPartActivity.class);
            intent.putExtra(TowerPartActivity.ARG_TASK, task);
            startActivity(intent);
        });
        binding.get().list.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);
        setHasOptionsMenu(true);
    }

    private void initRecyclerView() {
        searchViewModel.getTasks().observe(this, result -> {
            binding.get().setResultCount(result == null || result.data == null ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

    }
}
