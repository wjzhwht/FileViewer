package com.xerofox.fileviewer.ui.index;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.List;

import javax.inject.Inject;

public class TaskViewModel extends ViewModel {

    private final LiveData<Resource<List<Task>>> tasks;

    @Inject
    TaskViewModel(TowerRepository repository) {
        tasks = repository.loadAllTasks();
    }

    public LiveData<Resource<List<Task>>> getTasks() {
        return tasks;
    }
}
