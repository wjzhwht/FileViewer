package com.xerofox.fileviewer.helper;

import android.arch.lifecycle.LiveData;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.FileUtil;
import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.PartFile;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

public class LocalFileHelper implements FileHelper {
    File directory = Environment.getExternalStorageDirectory();

    @Inject
    public LocalFileHelper() {
    }

    @Override
    @NonNull
    public LiveData<List<Task>> loadAllTasks() {
        return new LiveData<List<Task>>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    List<Task> tasks = new ArrayList<>();
                    String rootPath = directory.getPath() + File.separator + PATH_ROOT;
                    if (!FileUtil.isFileExists(rootPath)) {
                        postValue(tasks);
                        return;
                    }
                    List<File> taskFiles = FileUtil.listFilesInDir(rootPath);
                    if (taskFiles == null || taskFiles.isEmpty()) {
                        postValue(tasks);
                        return;
                    }
                    for (File file : taskFiles) {
                        Task task = new Task(file.getName());
                        tasks.add(task);
                    }
                    postValue(tasks);
                }
            }
        };
    }

    @Override
    public void saveTasks(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        if (!directory.exists()) {
            directory.mkdir();
        }
        File rootFile = new File(directory, PATH_ROOT);
        for (Task task : tasks) {
            String taskPath = task.getTaskDirectoryName();
            File taskFile = new File(rootFile, taskPath);
            if (!taskFile.exists()) {
                taskFile.mkdirs();
            }
            String tppName = task.getTaskFileName();
            File tppFile = new File(taskFile, tppName);
            saveTask(task, tppFile);
            if (task.getPartList() != null && !task.getPartList().isEmpty()) {
                for (TowerPart part : task.getPartList()) {
                    PartFile partFile = part.getPartFile();
                    if (partFile != null) {
                        File folder = new File(taskFile, partFile.getFileType() + PART_FOLDER_SUFFIX);
                        if (!folder.exists()) {
                            folder.mkdirs();
                        }
                        File pngFile = new File(folder.getPath() + File.separator + partFile.getName());
                        save(pngFile, partFile.getBytes());
                    }
                }
            }

        }
    }

    private void saveTask(Task task, File file) {
        RandomAccessFile raf;
        try {
            FileUtil.createFileByDeleteOldFile(file);
            raf = new RandomAccessFile(file, "rw");
            ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
            ByteBufferWriter writer = new ByteBufferWriter(output);
            task.saveByteArray(writer);
            byte[] bytes = writer.toByteArray();
            raf.write(bytes);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(File file, byte[] data) {
        RandomAccessFile raf;
        try {
            FileUtil.createFileByDeleteOldFile(file);
            raf = new RandomAccessFile(file, "rw");
            ByteArrayOutputStream output = new ByteArrayOutputStream(data.length);
//            output.write(data);
            ByteBufferWriter writer = new ByteBufferWriter(output);
            writer.write(data);
            byte[] bytes = writer.toByteArray();
            raf.write(bytes);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Task loadTask(Task task) {
        if (task != null && task.getPartList() != null && !task.getPartList().isEmpty()) {
            return task;
        }
        String path = directory.getPath() + File.separator
                + PATH_ROOT + File.separator
                + task.getTaskDirectoryName() + File.separator
                + task.getTaskFileName();
        if (!FileUtil.isFileExists(path)) {
            return task;
        }
        File file = new File(path);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            byte[] byteArr = new byte[(int) raf.length()];
            raf.read(byteArr);
            raf.close();

            ByteBufferReader br = new ByteBufferReader(byteArr);
            return new Task(br, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return task;
    }

    @Override
    public LiveData<ArrayList<TowerPart>> loadTowerParts(Task task, SparseArray<MenuFilter> filters) {
        return new LiveData<ArrayList<TowerPart>>() {
            @Override
            protected void onActive() {
                super.onActive();
                postValue(doFilter(loadTask(task), filters));
            }
        };
    }

    @NonNull
    private ArrayList<TowerPart> doFilter(Task taskNew, SparseArray<MenuFilter> filters) {
        if (filters == null || filters.size() == 0) {
            return taskNew.getPartList();
        }
        ArrayList<TowerPart> partList = new ArrayList<>();
        for (TowerPart part : taskNew.getPartList()) {
            boolean add = true;
            for (int i = 0; i < filters.size(); i++) {
                MenuFilter filter = filters.valueAt(i);
                if (filter != null) {
                    add = filter.match(part);
                }
                if (!add) {
                    break;
                }
            }
            if (add) {
                partList.add(part);
            }
        }
        return partList;
    }

    @Override
    public int[] getLocalTaskIds() {
        String rootPath = directory.getPath() + File.separator + PATH_ROOT;
        if (!FileUtil.isFileExists(rootPath)) {
            return new int[0];
        }
        List<File> taskFiles = FileUtil.listFilesInDir(rootPath);
        if (taskFiles == null || taskFiles.isEmpty()) {
            return new int[0];
        }
        int[] array = new int[taskFiles.size()];
        for (int i = 0; i < taskFiles.size(); i++) {
            File file = taskFiles.get(i);
            Task task = new Task(file.getName());
            array[i] = task.getId();
        }
        return array;

    }
}
