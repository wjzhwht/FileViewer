package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.os.Environment;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.vo.Task;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class XeroApiImpl implements XeroApi {

    @Inject
    public XeroApiImpl() {
    }

    @Override
    public LiveData<ApiResponse<List<Task>>> loadAllTasks() {
        // FIXME: 2017/11/3  mock server api
        return new LiveData<ApiResponse<List<Task>>>() {
            @Override
            protected void onActive() {
                super.onActive();
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "n_54.tpp");
                RandomAccessFile raf;
                try {
                    raf = new RandomAccessFile(file, "r");
                    byte[] byteArr = new byte[(int) raf.length()];
                    raf.read(byteArr);
                    raf.close();
                    Task task = new Task(new ByteBufferReader(byteArr));
                    List<Task> tasks = new ArrayList<>();
                    tasks.add(task);
                    ApiResponse<List<Task>> response = new ApiResponse<>(200, tasks, null);
                    postValue(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
