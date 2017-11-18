package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.ArrayList;
import java.util.List;

public interface FileHelper {
    String PART_FOLDER_SUFFIX = "Files";

    LiveData<String> getRootPath(TowerType towerType);

    @NonNull
    LiveData<List<Project>> loadProjects();

    void saveProjects(List<Project> projects);

    LiveData<ArrayList<TowerPart>> loadTowerParts(TowerType towerType);
}
