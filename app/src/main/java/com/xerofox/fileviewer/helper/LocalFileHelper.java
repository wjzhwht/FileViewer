package com.xerofox.fileviewer.helper;

import android.arch.lifecycle.LiveData;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.FileUtil;
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
    private static final String PATH_ROOT = "xerofox";
    private static final String TASK_PRIFIX = "n_";
    private static final String TASK_SEPARATION = "#";
    private static final String TOWER_FILE_EXSTENSION = ".tpp";
    //    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File directory = Environment.getExternalStorageDirectory();

    @Inject
    public LocalFileHelper() {
    }

//    @Override
//    public LiveData<String> getRootPath(TowerType towerType) {
//        return new LiveData<String>() {
//            @Override
//            protected void onActive() {
//                super.onActive();
//                String path = directory.getPath() + File.separator
//                        + PATH_ROOT + File.separator
//                        + PROJECT_PRIFIX + towerType.getProjectId() + PROJECT_SEPARATION + towerType.getProjectName() + File.separator
//                        + TOWER_PRIFIX + towerType.getId() + TOWER_SEPARATION + towerType.getName() + File.separator;
//                setValue(path);
//            }
//        };
//    }

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
                        String name = file.getName();
                        if (!TextUtils.isEmpty(name) && name.startsWith(TASK_PRIFIX) && name.contains(TASK_SEPARATION)) {
                            String id = name.substring(name.indexOf(TASK_PRIFIX) + TASK_PRIFIX.length(), name.indexOf(TASK_SEPARATION));
                            String taskName = name.substring(name.indexOf(TASK_SEPARATION) + 1);
                            Task task = new Task(Integer.parseInt(id), taskName);
                            tasks.add(task);
                        }
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
            String taskPath = TASK_PRIFIX + task.getId() + TASK_SEPARATION + task.getName();
            File taskFile = new File(rootFile, taskPath);
            if (!taskFile.exists()) {
                taskFile.mkdirs();
            }
            String tppName = TASK_PRIFIX + task.getId() + TOWER_FILE_EXSTENSION;
            File tppFile = new File(taskFile, tppName);
            saveTask(task, tppFile);
            if (task.getPartList() != null && !task.getPartList().isEmpty()) {
                for (TowerPart part : task.getPartList()) {
                    PartFile partFile = part.getPartFile();
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
    public LiveData<ArrayList<TowerPart>> loadTowerParts(Task task) {
        return new LiveData<ArrayList<TowerPart>>() {
            @Override
            protected void onActive() {
                super.onActive();
                if (task.getPartList() != null && !task.getPartList().isEmpty()) {
                    postValue(task.getPartList());
                    return;
                }
                String path = directory.getPath() + File.separator
                        + PATH_ROOT + File.separator
                        + TASK_PRIFIX + task.getId() + TASK_SEPARATION + task.getName() + File.separator
                        + TASK_PRIFIX + task.getId() + TOWER_FILE_EXSTENSION;
                if (!FileUtil.isFileExists(path)) {
                    postValue(task.getPartList());
                }
                File file = new File(path);
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile(file, "r");
                    byte[] byteArr = new byte[(int) raf.length()];
                    raf.read(byteArr);
                    raf.close();

                    ByteBufferReader br = new ByteBufferReader(byteArr);
                    Task taskNew = new Task(br);
                    postValue(taskNew.getPartList());
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
            }
        };
    }
}
