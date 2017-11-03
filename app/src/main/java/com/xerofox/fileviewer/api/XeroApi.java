package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;

import com.xerofox.fileviewer.vo.Project;

import java.util.List;

public interface XeroApi {

    LiveData<ApiResponse<List<Project>>> getProjects();
}
