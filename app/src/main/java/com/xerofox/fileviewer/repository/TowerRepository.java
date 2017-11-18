package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.api.ApiResponse;
import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TowerRepository {

    private final AppExecutors appExecutors;
    private final FileHelper fileHelper;
    private final XeroApi api;

    @Inject
    TowerRepository(AppExecutors appExecutors, FileHelper fileHelper, XeroApi api) {
        this.appExecutors = appExecutors;
        this.fileHelper = fileHelper;
        this.api = api;
    }

    public LiveData<Resource<List<Task>>> loadAllTasks() {
        return new NetworkBoundResource<List<Task>, List<Task>>(appExecutors) {

                @Override
                protected void saveCallResult(@NonNull List<Task> item) {
                    fileHelper.saveTasks(item);
                }

                @Override
                protected boolean shouldFetch(@Nullable List<Task> data) {
                    return data == null || data.isEmpty();
                }

                @NonNull
                @Override
                protected LiveData<List<Task>> loadFromDb() {
                    return fileHelper.loadAllTasks();
                }

                @NonNull
                @Override
                protected LiveData<ApiResponse<List<Task>>> createCall() {
                    return api.loadAllTasks();
                }
            }.asLiveData();
    }

    public LiveData<ArrayList<TowerPart>> getTowerParts(Task task) {
        return fileHelper.loadTowerParts(task);
    }

    public LiveData<List<TowerPart>> searchPart(TowerType towerType, String id) {
        return new LiveData<List<TowerPart>>() {
            @Override
            protected void onActive() {
                super.onActive();
                List<TowerPart> partList = new ArrayList<>();
                if (towerType == null || towerType.getPartArr() == null || towerType.getPartArr().isEmpty()){
                    postValue(partList);
                }
                for (TowerPart part:towerType.getPartArr()){
                    if (String.valueOf(part.getId()).contains(id)){
                        partList.add(part);
                    }
                }
                postValue(partList);
            }
        };
    }

}
