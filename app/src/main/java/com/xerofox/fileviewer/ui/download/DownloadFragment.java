package com.xerofox.fileviewer.ui.download;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.elvishew.xlog.XLog;
import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.SearchFragmentBinding;
import com.xerofox.fileviewer.ui.common.BaseFragment;
import com.xerofox.fileviewer.util.AutoClearedValue;
import com.xerofox.fileviewer.util.ToastUtils;
import com.xerofox.fileviewer.vo.Status;

import javax.inject.Inject;

public class DownloadFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    DataBindingComponent dataBindingComponent;

    AutoClearedValue<SearchFragmentBinding> binding;

    AutoClearedValue<DownloadListAdapter> adapter;

    private DownloadViewModel viewModel;
    private ProgressDialog progressDialog;

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_download, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.download:
                new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
                        .setTitle(R.string.warning)
                        .setMessage(R.string.download_all_warning)
                        .setPositiveButton(R.string.ok, ((dialog, which) -> {
                            XLog.i("下载全部任务");
                            viewModel.download(viewModel.getTasks().getValue().data);
                        }))
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
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
        initObserve();
        DownloadListAdapter rvAdapter = new DownloadListAdapter(task ->
                new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
                        .setTitle(R.string.warning)
                        .setMessage(getString(R.string.download_warning, task.getName()))
                        .setPositiveButton(R.string.ok, ((dialog, which) -> {
                            viewModel.download(task);
                        }))
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show());
        binding.get().list.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);
        setHasOptionsMenu(true);
    }

    private void initObserve() {
        viewModel.getDownloadState().observe(this, downloadState -> {
            if (downloadState != null) {
                if (downloadState.isDownloading()) {
                    showProgressDialog();
                } else {
                    dismissProgressDialog();
                    if (downloadState.getData() != null && !downloadState.getData().isEmpty()) {
                        ToastUtils.showToast(R.string.download_success);
                    } else {
                        ToastUtils.showToast(downloadState.getErrorMessage());
                    }
                }
            }
        });
        viewModel.getTasks().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    binding.get().setResultCount(result.data == null ? 0 : result.data.size());
                    adapter.get().replace(result.data);
                    binding.get().executePendingBindings();
                } else {
                    ToastUtils.showToast(result.message);
                }
            }
        });

    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.downloading));
        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
