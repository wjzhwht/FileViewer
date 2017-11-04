package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.api.ApiResponse;
import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.helper.LocalFileHelper;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Status;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TowerRepository {

    private final AppExecutors appExecutors;
    private final LocalFileHelper fileHelper;
    private final XeroApi api;
    private LiveData<Resource<List<Project>>> projects;
    private LiveData<Resource<List<Project>>> allProjects;

    @Inject
    public TowerRepository(AppExecutors appExecutors, LocalFileHelper fileHelper, XeroApi api) {
        this.appExecutors = appExecutors;
        this.fileHelper = fileHelper;
        this.api = api;
    }

    public LiveData<Resource<List<Project>>> loadProject() {
        if (allProjects == null) {
            allProjects = new NetworkBoundResource<List<Project>, List<Project>>(appExecutors) {

                @Override
                protected void saveCallResult(@NonNull List<Project> item) {
                    fileHelper.saveProjects(item);
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
        return allProjects;
    }

    public LiveData<Resource<List<Project>>> searchProject(String query) {
        if (TextUtils.isEmpty(query)) {
            return allProjects;
        }
        return new LiveData<Resource<List<Project>>>() {
            @Override
            protected void onActive() {
                super.onActive();
                List<Project> projects = new ArrayList<>();
                if (allProjects.getValue() != null && allProjects.getValue().data != null) {
                    for (Project project : allProjects.getValue().data) {
                        if (project.getName().contains(query)) {
                            projects.add(project);
                        }
                    }
                }
                postValue(new Resource<>(Status.SUCCESS, projects, null));
            }
        };

    }

    public LiveData<Resource<List<TowerType>>> searchTowerType(String inputProject, String inputTower) {
        return new LiveData<Resource<List<TowerType>>>() {
            @Override
            protected void onActive() {
                super.onActive();
                List<TowerType> towers = new ArrayList<>();
                if (TextUtils.isEmpty(inputTower)) {
                    postValue(new Resource<>(Status.SUCCESS, towers, null));
                    return;
                }
                if (allProjects.getValue() == null
                        || allProjects.getValue().data == null
                        || allProjects.getValue().data.isEmpty()) {
                    postValue(new Resource<>(Status.SUCCESS, towers, null));
                    return;
                }
                for (Project project : allProjects.getValue().data) {
                    if (TextUtils.isEmpty(inputProject) || project.getName().contains(inputProject)) {
                        for (TowerType towerType : project.getTowerTypeArr()) {
                            if (towerType.getName().contains(inputTower)) {
                                towers.add(towerType);
                            }
                        }
                    }
                }
                postValue(new Resource<>(Status.SUCCESS, towers, null));
            }
        };
    }

    public LiveData<List<TowerPart>> getTowerParts() {
        return null;
    }
}
