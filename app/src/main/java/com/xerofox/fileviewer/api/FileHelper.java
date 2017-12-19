package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.List;

public interface FileHelper {
    String PATH_DATA_ROOT = "xerodata";
    String PATH_ROOT = "xerofox";
    String PART_FOLDER_SUFFIX = "Files";

    @NonNull
    LiveData<List<Task>> loadAllTasks();

    int[] getLocalTaskIds();

    void saveTasks(List<Task> tasks);

    LiveData<ArrayList<TowerPart>> loadTowerParts(Task task, SparseArray<MenuFilter> filters);

    Task loadTask(Task task);

    void saveUpdateParts(Task task, List<TowerPart> towerParts);

    void updateTask(Task task);

    LiveData<List<Task>> loadActiveTasks();
}
