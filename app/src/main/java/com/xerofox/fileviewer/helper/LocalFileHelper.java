package com.xerofox.fileviewer.helper;

import android.arch.lifecycle.LiveData;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.FileUtil;
import com.xerofox.fileviewer.vo.Filter;
import com.xerofox.fileviewer.vo.FilterQuery;
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
    //    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
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

    private Task loadTask(Task task) {
        if (task.getPartList() != null && !task.getPartList().isEmpty()) {
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
    public LiveData<ArrayList<TowerPart>> loadTowerParts(Task task, MenuFilter[] filters) {
        return new LiveData<ArrayList<TowerPart>>() {
            @Override
            protected void onActive() {
                super.onActive();
                postValue(doFilter(loadTask(task), filters));
            }
        };
    }

    @NonNull
    private ArrayList<TowerPart> doFilter(Task taskNew, MenuFilter[] filters) {
        if (filters == null || filters.length == 0) {
            return taskNew.getPartList();
        }
        ArrayList<TowerPart> partList = new ArrayList<>();
        for (TowerPart part : taskNew.getPartList()) {
            boolean add = true;
            for (MenuFilter filter : filters) {
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

    public LiveData<List<Filter>> loadFilters(Task task, List<FilterQuery> filters) {
        return new LiveData<List<Filter>>() {
            @Override
            protected void onActive() {
                super.onActive();
                postValue(getFilters(loadTask(task), filters));
            }
        };
    }

    public LiveData<List<Filter>> loadFilters(Task task) {
        return new LiveData<List<Filter>>() {
            @Override
            protected void onActive() {
                super.onActive();
                postValue(getFilters(loadTask(task)));
            }
        };
    }

    private List<Filter> getFilters(Task task) {
        Filter towerTypeFilter = new Filter(Filter.TYPE_TOWER_TYPE);
        Filter segStrFilter = new Filter(Filter.TYPE_SEG_STR);
        Filter materialFilter = new Filter(Filter.TYPE_MATERIAL_MARK);
        Filter specificationFilter = new Filter(Filter.TYPE_SPECIFICATION);
        Filter manuFilter = new Filter(Filter.TYPE_MANU);
        for (TowerPart part : task.getPartList()) {

            addItem(towerTypeFilter, part.getTowerTypeName());
            addItem(segStrFilter, part.getSegStr());
            addItem(materialFilter, part.getMaterialMark());
            addItem(specificationFilter, part.getSpecification());
            if (part.getManuHourWeld() > 0) {
                addItem(manuFilter, TowerPart.MANU_WELD);
            }
            if (part.getManuHourZhiWan() > 0) {
                addItem(manuFilter, TowerPart.MANU_ZHIWAN);
            }
            if (part.getManuHourCutAngle() > 0) {
                addItem(manuFilter, TowerPart.MANU_CUT_ANGEL);
            }
            if (part.getManuHourCutBer() > 0) {
                addItem(manuFilter, TowerPart.MANU_CUT_BER);
            }
            if (part.getManuHourCutRoot() > 0) {
                addItem(manuFilter, TowerPart.MANU_CUT_ROOT);
            }
            if (part.getManuHourClashHole() > 0) {
                addItem(manuFilter, TowerPart.MANU_CLASH_HOLE);
            }
            if (part.getManuHourBore() > 0) {
                addItem(manuFilter, TowerPart.MANU_BORE);
            }
            if (part.getManuHourKaiHe() > 0) {
                addItem(manuFilter, TowerPart.MANU_KAIHE);
            }
            if (part.getManuHourFillet() > 0) {
                addItem(manuFilter, TowerPart.MANU_FILLET);
            }
            if (part.getManuHourPushFlat() > 0) {
                addItem(manuFilter, TowerPart.MANU_PUSH_FLAT);
            }
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(towerTypeFilter);
        filters.add(segStrFilter);
        filters.add(materialFilter);
        filters.add(specificationFilter);
        filters.add(manuFilter);
        return filters;

    }

    private List<Filter> getFilters(Task task, List<FilterQuery> filterQueries) {
        Filter towerTypeFilter = new Filter(Filter.TYPE_TOWER_TYPE);
        Filter segStrFilter = new Filter(Filter.TYPE_SEG_STR);
        Filter materialFilter = new Filter(Filter.TYPE_MATERIAL_MARK);
        Filter specificationFilter = new Filter(Filter.TYPE_SPECIFICATION);
        Filter manuFilter = new Filter(Filter.TYPE_MANU);
        for (TowerPart part : task.getPartList()) {

            addItem(filterQueries, towerTypeFilter, part.getTowerTypeName());
            addItem(filterQueries, segStrFilter, part.getSegStr());
            addItem(filterQueries, materialFilter, part.getMaterialMark());
            addItem(filterQueries, specificationFilter, part.getSpecification());
            if (part.getManuHourWeld() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_WELD);
            }
            if (part.getManuHourZhiWan() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_ZHIWAN);
            }
            if (part.getManuHourCutAngle() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_CUT_ANGEL);
            }
            if (part.getManuHourCutBer() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_CUT_BER);
            }
            if (part.getManuHourCutRoot() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_CUT_ROOT);
            }
            if (part.getManuHourClashHole() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_CLASH_HOLE);
            }
            if (part.getManuHourBore() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_BORE);
            }
            if (part.getManuHourKaiHe() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_KAIHE);
            }
            if (part.getManuHourFillet() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_FILLET);
            }
            if (part.getManuHourPushFlat() > 0) {
                addItem(filterQueries, manuFilter, TowerPart.MANU_PUSH_FLAT);
            }
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(towerTypeFilter);
        filters.add(segStrFilter);
        filters.add(materialFilter);
        filters.add(specificationFilter);
        filters.add(manuFilter);
        return filters;

    }

    private void addItem(Filter towerTypeFilter, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        Filter.Item item = new Filter.Item(text, false);
        if (!towerTypeFilter.getItems().contains(item)) {
            towerTypeFilter.getItems().add(item);
        }
    }

    private void addItem(List<FilterQuery> filterQueries, Filter towerTypeFilter, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        FilterQuery filterQuery = getFilterQuery(filterQueries, towerTypeFilter.getType());
        Filter.Item item = new Filter.Item(text, filterQuery.getItems().contains(text));
        if (!towerTypeFilter.getItems().contains(item)) {
            towerTypeFilter.getItems().add(item);
        }
    }

    private FilterQuery getFilterQuery(List<FilterQuery> filterQueries, int type) {
        for (FilterQuery filterQuery : filterQueries) {
            if (filterQuery.getType() == type) {
                return filterQuery;
            }
        }
        return new FilterQuery();
    }
}
