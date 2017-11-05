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
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class LocalFileHelper implements FileHelper {
    private static final String PATH_ROOT = "xerofox";
    private static final String PROJECT_PRIFIX = "n_";
    private static final String PROJECT_SEPARATION = "@";
    private static final String TOWER_PRIFIX = "n_";
    private static final String TOWER_SEPARATION = "#";
    private static final String TOWER_FILE_EXSTENSION = ".tpp";
    private static final String PART_FOLDER_SUFFIX = "Files";
    //    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    File directory = Environment.getExternalStorageDirectory();

    public LocalFileHelper() {
    }

    @Override
    @NonNull
    public LiveData<List<Project>> loadProjects() {
        return new LiveData<List<Project>>() {
            AtomicBoolean started = new AtomicBoolean(false);

            @Override
            protected void onActive() {
                super.onActive();
                if (started.compareAndSet(false, true)) {
                    List<Project> projects = new ArrayList<>();
                    String rootPath = directory.getPath() + File.separator + PATH_ROOT;
                    if (!FileUtil.isFileExists(rootPath)) {
                        postValue(projects);
                        return;
                    }
                    List<File> projectFiles = FileUtil.listFilesInDir(rootPath);
                    if (projectFiles == null || projectFiles.isEmpty()) {
                        postValue(projects);
                        return;
                    }
                    for (File file : projectFiles) {
                        String name = file.getName();
                        if (!TextUtils.isEmpty(name) && name.startsWith(PROJECT_PRIFIX) && name.contains(PROJECT_SEPARATION)) {
                            String id = name.substring(name.indexOf(PROJECT_PRIFIX) + PROJECT_PRIFIX.length(), name.indexOf(PROJECT_SEPARATION));
                            String projectName = name.substring(name.indexOf(PROJECT_SEPARATION) + 1);
                            List<TowerType> types = new ArrayList<>();
                            List<File> towerFiles = FileUtil.listFilesInDir(file);
                            for (File towerFile : towerFiles) {
                                String towerFileName = towerFile.getName();
                                if (!TextUtils.isEmpty(towerFileName) && towerFileName.startsWith(TOWER_PRIFIX) && towerFileName.contains(TOWER_SEPARATION)) {
                                    String tId = towerFileName.substring(towerFileName.indexOf(TOWER_PRIFIX) + TOWER_PRIFIX.length(), towerFileName.indexOf(TOWER_SEPARATION));
                                    String tName = towerFileName.substring(towerFileName.indexOf(TOWER_SEPARATION) + 1);
                                    TowerType towerType = new TowerType(Integer.parseInt(tId), tName, Integer.parseInt(id), projectName);
                                    types.add(towerType);
                                }
                            }
                            Project project = new Project(Integer.parseInt(id), projectName, types);
                            projects.add(project);
                        }
                    }
                    postValue(projects);
                }
            }
        };
    }

    @Override
    public void saveProjects(List<Project> projects) {
        if (!directory.exists()) {
            directory.mkdir();
        }
        File rootFile = new File(directory, PATH_ROOT);
        for (Project project : projects) {
            String projectPath = PROJECT_PRIFIX + project.getId() + PROJECT_SEPARATION + project.getName();
            File projectFile = new File(rootFile, projectPath);
            if (!projectFile.exists()) {
                projectFile.mkdirs();
            }
            if (project.getTowerTypeArr() != null && !project.getTowerTypeArr().isEmpty()) {
                for (TowerType towerType : project.getTowerTypeArr()) {
                    String towerPath = TOWER_PRIFIX + towerType.getId() + TOWER_SEPARATION + towerType.getName();
                    File towerFile = new File(projectFile, towerPath);
                    if (!towerFile.exists()) {
                        towerFile.mkdirs();
                    }
                    String tppName = TOWER_PRIFIX + towerType.getId() + TOWER_FILE_EXSTENSION;
                    File tppFile = new File(towerFile, tppName);
                    saveTower(towerType, tppFile);

                    if (towerType.getPartArr() != null && !towerType.getPartArr().isEmpty()) {
                        for (TowerPart part : towerType.getPartArr()) {
                            PartFile partFile = part.getPartFile();
                            File folder = new File(towerFile, partFile.getFileType() + PART_FOLDER_SUFFIX);
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
    }

    private void saveTower(TowerType towerType, File file) {
        RandomAccessFile raf;
        try {
            FileUtil.createFileByDeleteOldFile(file);
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
    public LiveData<TowerType> loadTowerType(TowerType towerType) {
        return new LiveData<TowerType>() {
            @Override
            protected void onActive() {
                super.onActive();
                if (towerType.getPartArr() != null && !towerType.getPartArr().isEmpty()) {
                    postValue(towerType);
                    return;
                }
                String path = directory.getPath() + File.separator
                        + PATH_ROOT + File.separator
                        + PROJECT_PRIFIX + towerType.getProjectId() + PROJECT_SEPARATION + towerType.getProjectName() + File.separator
                        + TOWER_PRIFIX + towerType.getId() + TOWER_SEPARATION + towerType.getName() + File.separator
                        + TOWER_PRIFIX + towerType.getId() + TOWER_FILE_EXSTENSION;
                if (!FileUtil.isFileExists(path)) {
                    postValue(towerType);
                }
                File file = new File(path);
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile(file, "r");
                    byte[] byteArr = new byte[(int) raf.length()];
                    raf.read(byteArr);
                    raf.close();

                    ByteBufferReader br = new ByteBufferReader(byteArr);
                    TowerType type = new TowerType(br);
                    postValue(type);
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
