package com.xerofox.fileviewer.ui.download;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.xerofox.fileviewer.repository.TaskRepository;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DownloadViewModel extends ViewModel {

    private final LiveData<Resource<List<Task>>> tasks;
    private final MutableLiveData<Param> param = new MutableLiveData<>();
    private final LiveData<Resource<Boolean>> downloading;
    private TaskRepository repository;

    @Inject
    DownloadViewModel(TaskRepository repository) {
        this.repository = repository;
        tasks = repository.getDownloadTasks();

//        downloading = Transformations.switchMap(param, data -> {
//            if (data == null || data.tasks.isEmpty()) {
//                return AbsentLiveData.create();
//            } else {
//                return repository.downloadTasks(data.tasks);
//            }
//        });
        downloading = new MutableLiveData<>();
    }

    void downloadTask(Task task) {
        List<Task> tasks = new ArrayList<>(1);
        tasks.add(task);
        downloadTasks(tasks);

    }

    void downloadTasks(List<Task> tasks) {
//        Param param = new Param(tasks);
//        this.param.setValue(param);
        repository.downloadTasks(tasks);
    }

    LiveData<Resource<List<Task>>> getTasks() {
        return tasks;
    }

    public LiveData<Resource<Boolean>> getDownloading() {
        return downloading;
    }

    static class Param{
        final List<Task> tasks;
        final long timeStamp;


        Param(List<Task> tasks) {
            this.tasks = tasks;
            timeStamp = System.currentTimeMillis();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Param param = (Param) o;

            if (timeStamp != param.timeStamp) return false;
            return tasks != null ? tasks.equals(param.tasks) : param.tasks == null;
        }

        @Override
        public int hashCode() {
            int result = tasks != null ? tasks.hashCode() : 0;
            result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
            return result;
        }
    }
}
