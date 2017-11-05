package com.xerofox.fileviewer.ui.search;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.binding.FragmentDataBindingComponent;
import com.xerofox.fileviewer.databinding.SearchFragmentBinding;
import com.xerofox.fileviewer.di.Injectable;
import com.xerofox.fileviewer.ui.common.NavigationController;
import com.xerofox.fileviewer.util.AutoClearedValue;
import com.xerofox.fileviewer.util.KeyboardUtil;
import com.xerofox.fileviewer.vo.TowerType;

import javax.inject.Inject;

public class SearchFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<SearchFragmentBinding> binding;

    AutoClearedValue<TowerTypeListAdapter> adapter;

    AutoClearedValue<ProjectAdapter> projectAdapter;

    private SearchViewModel searchViewModel;

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
        TowerTypeListAdapter rvAdapter = new TowerTypeListAdapter(dataBindingComponent, repo -> {
        });
        binding.get().towerList.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);

        ProjectAdapter pAdapter = new ProjectAdapter(dataBindingComponent);
        binding.get().projectList.setAdapter(pAdapter);
        binding.get().projectList.setOnChildClickListener((expandableListView, view, i, i1, l) -> {
            TowerType towerType = searchViewModel.getProjects().getValue().data.get(i).getTowerTypeArr().get(i1);
            navigationController.navigateToPart(towerType);
            return true;
        });
        projectAdapter = new AutoClearedValue<>(this, pAdapter);

        doSearch(null);
        initSearchInputListener();
    }

    private void initSearchInputListener() {
        binding.get().project.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(v);
                return true;
            }
            return false;
        });
        binding.get().project.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(v);
                return true;
            }
            return false;
        });
        binding.get().towerType.setOnEditorActionListener((v, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(v);
                return true;
            }
            return false;
        });

        binding.get().towerType.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                    && (i == KeyEvent.KEYCODE_ENTER)) {
                doSearch(view);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View v) {
        String project = binding.get().project.getText().toString().trim();
        String tower = binding.get().towerType.getText().toString().trim();
        KeyboardUtil.hideSoftInput(v);
        searchViewModel.setSearch(project, tower);
    }

    private void initRecyclerView() {
        searchViewModel.getProjects().observe(this, result -> {
            binding.get().setProjectCount(result == null || result.data == null ? 0 : result.data.size());
            projectAdapter.get().setProjects(result == null ? null : result.data);
            projectAdapter.get().notifyDataSetChanged();
            binding.get().executePendingBindings();
        });

        searchViewModel.getTowerTypes().observe(this, result -> {
            binding.get().setTowerCount(result == null || result.data == null ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

    }
}
