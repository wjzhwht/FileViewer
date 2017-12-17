package com.xerofox.fileviewer.ui.download;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TaskRepository;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class DownloadViewModel extends ViewModel {

    private final LiveData<Resource<List<Task>>> tasks;
    private final DownloadTaskHandler downloadTaskHandler;


    @Inject
    DownloadViewModel(TaskRepository repository) {
        downloadTaskHandler = new DownloadTaskHandler(repository);
        tasks = repository.getDownloadTasks();

    }

    LiveData<Resource<List<Task>>> getTasks() {
        return tasks;
    }

    LiveData<DownloadState> getDownloadState() {
        return downloadTaskHandler.getDownloadState();
    }

    void download(Task task) {
        downloadTaskHandler.download(task);
    }

    void download(List<Task> tasks) {
        downloadTaskHandler.download(tasks);
    }

    static class DownloadState {
        private final boolean downloading;
        private final String errorMessage;
        private boolean handledError = false;

        DownloadState(boolean downloading, String errorMessage) {
            this.downloading = downloading;
            this.errorMessage = errorMessage;
        }

        public boolean isDownloading() {
            return downloading;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        String getErrorMessageIfNotHandled() {
            if (handledError) {
                return null;
            }
            handledError = true;
            return errorMessage;
        }
    }

    static class DownloadTaskHandler implements Observer<Resource<Boolean>> {
        private final TaskRepository repository;
        private LiveData<Resource<Boolean>> downloadLiveData;
        private final MutableLiveData<DownloadState> downloadState = new MutableLiveData<>();
        private List<Task> taskList;

        @VisibleForTesting
        DownloadTaskHandler(TaskRepository repository) {
            this.repository = repository;
            reset();
        }

        void download(Task task) {
            if (task == null) {
                return;
            }
            List<Task> tasks = new ArrayList<>(1);
            tasks.add(task);
            download(tasks);
        }

        void download(List<Task> tasks) {
            if (tasks == null || tasks.isEmpty()) {
                return;
            }
            unregister();
            downloadLiveData = repository.downloadTasks(tasks);
            downloadState.setValue(new DownloadState(true, null));
            downloadLiveData.observeForever(this);
        }

        @Override
        public void onChanged(@Nullable Resource<Boolean> result) {
            if (result == null) {
                reset();
            } else {
                switch (result.status) {
                    case SUCCESS:
                        unregister();
                        downloadState.setValue(new DownloadState(false, null));
                        break;
                    case ERROR:
                        unregister();
                        downloadState.setValue(new DownloadState(false, result.message));
                        break;
                }
            }
        }

        private void unregister() {
            if (downloadLiveData != null) {
                downloadLiveData.removeObserver(this);
                downloadLiveData = null;
            }
        }

        private void reset() {
            unregister();
            downloadState.setValue(new DownloadState(false, null));
        }

        public LiveData<DownloadState> getDownloadState() {
            return downloadState;
        }
    }
}
