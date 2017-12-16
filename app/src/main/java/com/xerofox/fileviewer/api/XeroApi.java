package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import java.util.List;

public interface XeroApi {

    LiveData<ApiResponse<List<Task>>> loadAllTasks();

    LiveData<Resource<List<Task>>> getServerTasks(AppExecutors appExecutors, int[] localTaskIds);
}
