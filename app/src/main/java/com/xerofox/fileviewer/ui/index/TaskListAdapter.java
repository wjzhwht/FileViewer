package com.xerofox.fileviewer.ui.index;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.api.OnItemClickListener;
import com.xerofox.fileviewer.databinding.TaskItemBinding;
import com.xerofox.fileviewer.ui.common.DataBoundListAdapter;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Task;

public class TaskListAdapter extends DataBoundListAdapter<Task, TaskItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private OnItemClickListener<Task> onItemClickListener;

    public TaskListAdapter(DataBindingComponent dataBindingComponent,
                           OnItemClickListener<Task> onItemClickListener) {
        this.dataBindingComponent = dataBindingComponent;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected TaskItemBinding createBinding(ViewGroup parent) {
        TaskItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.task_item,
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
    protected void bind(TaskItemBinding binding, Task task) {
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
