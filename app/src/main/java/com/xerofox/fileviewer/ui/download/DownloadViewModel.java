package com.xerofox.fileviewer.ui.download;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.xerofox.fileviewer.repository.TaskRepository;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.List;

import javax.inject.Inject;

public class DownloadViewModel extends ViewModel {

    private final LiveData<Resource<List<Task>>> tasks;

    @Inject
    DownloadViewModel(TaskRepository repository) {
        tasks = repository.getDownloadTasks();
    }

    public LiveData<Resource<List<Task>>> getTasks() {
        return tasks;
    }
}
