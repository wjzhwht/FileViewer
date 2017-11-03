package com.xerofox.fileviewer.helper;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.FileUtil;
import com.xerofox.fileviewer.vo.PartFile;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LocalFileHelper {
    private static final String PATH_ROOT = "xerofox";
    private static final String PROJECT_PRIFIX = "n_";
    private static final String PROJECT_SEPARATION = "@";
    private static final String TOWER_PRIFIX = "n_";
    private static final String TOWER_SEPARATION = "#";
    private static final String TOWER_FILE_EXSTENSION = ".tpp";
    private static final String PART_FOLDER_SUFFIX = "Files";

    @Inject
    public LocalFileHelper() {
    }

    @NonNull
    public LiveData<List<Project>> loadProjects() {
        MutableLiveData<List<Project>> liveData = new MutableLiveData<>();
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String rootPath = directory.getPath() + PATH_ROOT;
        if (FileUtil.isFileExists(rootPath)) {
            return liveData;
        }
        List<File> projectFiles = FileUtil.listFilesInDir(rootPath);
        if (projectFiles.isEmpty()) {
            return liveData;
        }
        List<Project> projects = new ArrayList<>();
        for (File file : projectFiles) {
            String name = file.getName();
            if (!TextUtils.isEmpty(name) && name.startsWith(PROJECT_PRIFIX) && name.contains(PROJECT_SEPARATION)) {
                String id = name.substring(name.indexOf(PROJECT_PRIFIX) + 2, name.indexOf(PROJECT_SEPARATION));
                String projectName = name.substring(name.indexOf(PROJECT_SEPARATION));
                List<TowerType> types = new ArrayList<>();
                List<File> towerFiles = FileUtil.listFilesInDir(file);
                for (File towerFile : towerFiles) {
                    String towerFileName = towerFile.getName();
                    if (!TextUtils.isEmpty(towerFileName) && towerFileName.startsWith(TOWER_PRIFIX) && towerFileName.contains(TOWER_SEPARATION)) {
                        String tId = towerFileName.substring(towerFileName.indexOf(TOWER_PRIFIX) + 2, towerFileName.indexOf(TOWER_SEPARATION));
                        String tName = towerFileName.substring(towerFileName.indexOf(TOWER_SEPARATION));
                        TowerType towerType = new TowerType(Integer.parseInt(tId), tName);
                        types.add(towerType);
                    }
                }
                Project project = new Project(Integer.parseInt(id), projectName, types);
                projects.add(project);
            }
        }
        liveData.postValue(projects);
        return liveData;
    }

    public void saveProjects(List<Project> projects) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String rootPath = directory.getPath() + PATH_ROOT;
        for (Project project : projects) {
            String projectPath = rootPath + File.separator + PROJECT_PRIFIX + project.getId() + PROJECT_SEPARATION + project.getName();
            File file = new File(projectPath);
            if (!file.exists()) {
                file.mkdir();
            }
            if (project.getTowerTypeArr() != null && !project.getTowerTypeArr().isEmpty()) {
                for (TowerType towerType : project.getTowerTypeArr()) {
                    String towerPath = projectPath + File.separator + TOWER_PRIFIX + towerType.getId() + TOWER_SEPARATION + towerType.getName();
                    File towerFile = new File(towerPath);
                    if (!towerFile.exists()) {
                        towerFile.mkdir();
                    }
                    String tppFile = towerPath + File.separator + TOWER_PRIFIX + towerType.getId() + TOWER_FILE_EXSTENSION;
                    saveTower(towerType, tppFile);

                    if (towerType.getPartArr() != null && !towerType.getPartArr().isEmpty()) {
                        for (TowerPart part : towerType.getPartArr()) {
                            PartFile partFile = part.getPartFile();
                            String partPath = towerPath + File.separator + partFile.getFileType() + PART_FOLDER_SUFFIX + File.separator + partFile.getName();
                            save(partPath, partFile.getBytes());
                        }
                    }

                }
            }
        }
    }

    private void saveTower(TowerType towerType, String path) {
        File file = new File(path);
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "rw");
            ByteArrayOutputStream output = new ByteArrayOutputStream(1024);
            ByteBufferWriter writer = new ByteBufferWriter(output);
            towerType.saveByteArray(writer);
            byte[] bytes = writer.toByteArray();
            raf.write(bytes);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(String path, byte[] data) {
        File file = new File(path);
        RandomAccessFile raf;
        try {
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
}
