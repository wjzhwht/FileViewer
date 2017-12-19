package com.xerofox.fileviewer.ui.task;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.xerofox.fileviewer.repository.TaskRepository;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.List;

import javax.inject.Inject;

public class TaskViewModel extends ViewModel {

    private final LiveData<Resource<List<Task>>> tasks;
    private final MutableLiveData<Boolean> isActive = new MutableLiveData<>();
    private final UpdateTaskHandler updateTaskHandler;

    @Inject
    TaskViewModel(TaskRepository repository) {
        updateTaskHandler = new UpdateTaskHandler(repository);
        tasks = Transformations.switchMap(isActive, data -> {
            if (data == null || !data) {
                return repository.loadAllTasks();
            } else {
                return repository.loadActiveTasks();
            }
        });
    }

    void setIsActive(boolean isActive) {
        if (this.isActive.getValue() != null && this.isActive.getValue() == isActive) {
            return;
        }
        this.isActive.setValue(isActive);
    }

    LiveData<Resource<List<Task>>> getTasks() {
        return tasks;
    }

    void updateTaskState(Task task) {
        updateTaskHandler.update(task);
    }

    static class UpdateTaskHandler {
        private final TaskRepository repository;

        public UpdateTaskHandler(TaskRepository repository) {
            this.repository = repository;
        }

        void update(Task task) {
            repository.updateTask(task);
        }

    }
}
