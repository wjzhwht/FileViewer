package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskRepository {
    private final AppExecutors appExecutors;
    private final FileHelper fileHelper;
    private final XeroApi api;

    @Inject
    public TaskRepository(AppExecutors appExecutors, FileHelper fileHelper, XeroApi api) {
        this.appExecutors = appExecutors;
        this.fileHelper = fileHelper;
        this.api = api;
    }


    public LiveData<Resource<List<Task>>> getDownloadTasks() {
        return api.getServerTasks(appExecutors, fileHelper.getLocalTaskIds());
    }

    public LiveData<Resource<Boolean>> downloadTasks(List<Task> data) {
//        return new NetworkBoundResource<Boolean,Boolean>(appExecutors){
//
//            @Override
//            protected void saveCallResult(@NonNull Boolean item) {
//
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable Boolean data) {
//                return true;
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<Boolean> loadFromDb() {
//                return AbsentLiveData.create();
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<Boolean>> createCall() {
//                return null;
//            }
//        }.asLiveData();
        return api.downloadTasks(appExecutors, fileHelper, data);
    }
}
