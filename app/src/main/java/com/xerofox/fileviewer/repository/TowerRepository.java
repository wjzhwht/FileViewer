package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TowerRepository {

    private final AppExecutors appExecutors;

    @Inject
    public TowerRepository(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
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
