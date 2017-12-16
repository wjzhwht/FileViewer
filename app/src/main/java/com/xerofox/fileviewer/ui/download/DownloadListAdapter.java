package com.xerofox.fileviewer.ui.download;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.api.OnItemClickListener;
import com.xerofox.fileviewer.databinding.DownloadItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Task;

import javax.inject.Inject;

public class DownloadListAdapter extends DataBoundListAdapter<Task, DownloadItemBinding> {

    @Inject
    DataBindingComponent dataBindingComponent;
    private OnItemClickListener<Task> onItemClickListener;

    public DownloadListAdapter(OnItemClickListener<Task> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected DownloadItemBinding createBinding(ViewGroup parent) {
        DownloadItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.download_item,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Task task = binding.getTask();
            if (task != null && onItemClickListener != null) {
                onItemClickListener.onClick(task);
            }
        });
        return binding;
    }

    @Override
    protected void bind(DownloadItemBinding binding, Task task) {
        binding.setTask(task);
    }

    @Override
    protected boolean areItemsTheSame(Task oldItem, Task newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId()) &&
                Objects.equals(oldItem.getName(), newItem.getName());
    }

    @Override
    protected boolean areContentsTheSame(Task oldItem, Task newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId()) &&
                Objects.equals(oldItem.getName(), newItem.getName());
    }
}
