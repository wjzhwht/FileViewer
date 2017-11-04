package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.os.Environment;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerType;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class XeroApiImpl implements XeroApi {
    @Override
    public LiveData<ApiResponse<List<Project>>> getProjects() {
        // FIXME: 2017/11/3  mock server api
        return new LiveData<ApiResponse<List<Project>>>() {
            @Override
            protected void onActive() {
                super.onActive();
                List<Project> projects = new ArrayList<>();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "n_245.tpp");
//                File file = new File("n_245.tpp");
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
                    ApiResponse<List<Project>> response = new ApiResponse<>(200, projects, null);
                    postValue(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
