package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class XeroApiImpl implements XeroApi {
    @Override
    public LiveData<ApiResponse<List<Project>>> getProjects() {
        // FIXME: 2017/11/3  mock server api
        List<Project> projects = new ArrayList<>();
        File file = new File("n_245.tpp");
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
            byte[] byteArr = new byte[(int) raf.length()];
            raf.read(byteArr);
            raf.close();
            TowerType towerType1 = new TowerType(new ByteBufferReader(byteArr));
            List<TowerType> types = new ArrayList<>();
            types.add(towerType1);
            Project project = new Project(towerType1.getProjectId(), towerType1.getProjectName(), types);
            projects.add(project);
            MutableLiveData<ApiResponse<List<Project>>> liveData = new MutableLiveData<>();
            ApiResponse<List<Project>> response = new ApiResponse<>(200, projects, null);
            liveData.postValue(response);
            return liveData;
        } catch (IOException e) {
            Timber.e(e.getMessage());
        }
        return null;
    }
}
