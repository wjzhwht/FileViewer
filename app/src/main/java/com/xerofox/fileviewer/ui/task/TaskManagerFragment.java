package com.xerofox.fileviewer.ui.task;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.databinding.SearchFragmentBinding;
import com.xerofox.fileviewer.ui.common.BaseFragment;
import com.xerofox.fileviewer.util.AutoClearedValue;
import com.xerofox.fileviewer.vo.Task;

import javax.inject.Inject;

public class TaskManagerFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    DataBindingComponent dataBindingComponent;

    AutoClearedValue<SearchFragmentBinding> binding;

    AutoClearedValue<TaskListAdapter> adapter;

    private TaskViewModel viewModel;

    public static TaskManagerFragment newInstance() {

        Bundle args = new Bundle();

        TaskManagerFragment fragment = new TaskManagerFragment();
        fragment.setArguments(args);
        return fragment;
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
        getActivity().setTitle(R.string.task_manage);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TaskViewModel.class);
        initRecyclerView();
        TaskListAdapter rvAdapter = new TaskListAdapter(this::onTaskClick);
        binding.get().list.setAdapter(rvAdapter);
        adapter = new AutoClearedValue<>(this, rvAdapter);
        viewModel.setIsActive(false);
    }

    private void onTaskClick(Task task) {
        new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle)
                .setTitle(R.string.operation)
                .setItems(R.array.task_option, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            task.setState(Task.STATE_INT_ACTIVE);
                            break;
                        case 1:
                            task.setState(Task.STATE_INT_FINISH);
                            break;
                        case 2:
                            task.setState(Task.STATE_INT_HIDE);
                            break;
                    }
                    viewModel.updateTaskState(task);
                })
                .create().show();

    }

    private void initRecyclerView() {
        viewModel.getTasks().observe(this, result -> {
            binding.get().setResultCount(result == null || result.data == null ? 0 : result.data.size());
            adapter.get().replace(result == null ? null : result.data);
            binding.get().executePendingBindings();
        });

    }
}
