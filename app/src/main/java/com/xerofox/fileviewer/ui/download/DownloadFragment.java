package com.xerofox.fileviewer.ui.download;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.SearchFragmentBinding;
import com.xerofox.fileviewer.ui.common.BaseFragment;
import com.xerofox.fileviewer.util.AutoClearedValue;

import javax.inject.Inject;

public class DownloadFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    DataBindingComponent dataBindingComponent;

    AutoClearedValue<SearchFragmentBinding> binding;

    AutoClearedValue<DownloadListAdapter> adapter;

    private DownloadViewModel viewModel;

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        getActivity().setTitle(R.string.download_list);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(DownloadViewModel.class);
        initRecyclerView();
        DownloadListAdapter rvAdapter = new DownloadListAdapter(task ->
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle))
                        .setTitle(R.string.warning)
                        .setMessage(getString(R.string.download_warning, task.getName()))
                        .setPositiveButton(R.string.ok, ((dialog, which) -> {
                            // FIXME: 2017/12/16 start download
//                            new Thread(() -> {
//                                XeroNetApi net = new XeroNetApi();
//                                ArrayList<Task> taskList = net.DownloadTaskArr();
//                                LocalFileHelper fileHelper = new LocalFileHelper();
//                                fileHelper.saveTasks(taskList);
//                            }).start();
                        }))
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show());
        binding.get().list.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);
        setHasOptionsMenu(true);
    }

    private void initRecyclerView() {
        viewModel.getTasks().observe(this, result -> {
            binding.get().setResultCount(result == null || result.data == null ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

    }
}
