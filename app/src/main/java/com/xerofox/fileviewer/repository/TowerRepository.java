package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.api.ApiResponse;
import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.helper.LocalFileHelper;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TowerRepository {

    private final AppExecutors appExecutors;
    private final LocalFileHelper fileHelper;
    private final XeroApi api;
    private LiveData<Resource<List<Project>>> projects;

    @Inject
    public TowerRepository(AppExecutors appExecutors, LocalFileHelper fileHelper, XeroApi api) {
        this.appExecutors = appExecutors;
        this.fileHelper = fileHelper;
        this.api = api;
    }

    public LiveData<Resource<List<Project>>> loadProject() {
        if (projects == null){
            projects = new NetworkBoundResource<List<Project>, List<Project>>(appExecutors) {

                @Override
                protected void saveCallResult(@NonNull List<Project> item) {

                }

                @Override
                protected boolean shouldFetch(@Nullable List<Project> data) {
                    return data == null || data.isEmpty();
                }

                @NonNull
                @Override
                protected LiveData<List<Project>> loadFromDb() {
                    return fileHelper.loadProjects();
                }

                @NonNull
                @Override
                protected LiveData<ApiResponse<List<Project>>> createCall() {
                    return api.getProjects();
                }
            }.asLiveData();
        }
        return projects;
    }

    public LiveData<Resource<List<Project>>> getProjects() {
        return projects;
    }

    public LiveData<List<Project>> searchProject(String search) {
        return null;
    }

    public LiveData<List<TowerType>> searchTowerType(String project, String search) {
        return null;
    }

    public LiveData<List<TowerPart>> getTowerParts() {
        return null;
    }
}
