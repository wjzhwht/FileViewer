package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.xerofox.fileviewer.vo.Filter;
import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.List;

public interface FileHelper {
    String PATH_DATA_ROOT = "xerodata";
    String PATH_ROOT = "xerofox";
    String TASK_PRIFIX = "n_";
    String TASK_SEPARATION = "#";
    String TOWER_FILE_EXSTENSION = ".tpp";
    String PART_FOLDER_SUFFIX = "Files";

    @NonNull
    LiveData<List<Task>> loadAllTasks();

    void saveTasks(List<Task> tasks);

    LiveData<ArrayList<TowerPart>> loadTowerParts(Task task, MenuFilter[] filters);

    Task loadTask(Task task);

}
