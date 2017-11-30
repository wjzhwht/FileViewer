package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.api.ApiResponse;
import com.xerofox.fileviewer.api.FileHelper;
import com.xerofox.fileviewer.api.XeroApi;
import com.xerofox.fileviewer.vo.Filter;
import com.xerofox.fileviewer.vo.ManuMenuFilter;
import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.SpecificationMenuFilter;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TowerRepository {

    private final AppExecutors appExecutors;
    private final FileHelper fileHelper;
    private final XeroApi api;

    @Inject
    TowerRepository(AppExecutors appExecutors, FileHelper fileHelper, XeroApi api) {
        this.appExecutors = appExecutors;
        this.fileHelper = fileHelper;
        this.api = api;
    }

    public LiveData<Resource<List<Task>>> loadAllTasks() {
        return new NetworkBoundResource<List<Task>, List<Task>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<Task> item) {
                fileHelper.saveTasks(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Task> data) {
                return data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<Task>> loadFromDb() {
                return fileHelper.loadAllTasks();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Task>>> createCall() {
                return api.loadAllTasks();
            }
        }.asLiveData();
    }

    public LiveData<ArrayList<TowerPart>> getTowerParts(Task task, MenuFilter... menuFilters) {
        return fileHelper.loadTowerParts(task, menuFilters);
    }

//    public LiveData<List<Filter>> getFilters(Task task, List<FilterQuery> filter) {
//        return fileHelper.loadFilters(task, filter);
//    }

    public LiveData<List<Filter>> getFilters(Task task) {
        return fileHelper.loadFilters(task);
    }

    public List<String> getFilterTitles() {
        List<String> list = new ArrayList<>();
        list.add("规格");
        list.add("工艺");
        return list;
    }

    public List<List<MenuFilter>> getFilterLists() {
        List<MenuFilter> menuFilters1 = new ArrayList<>();
        SpecificationMenuFilter specificationMenuFilter = new SpecificationMenuFilter(0, 50);
        SpecificationMenuFilter specificationMenuFilter2 = new SpecificationMenuFilter(51, 100);
        SpecificationMenuFilter specificationMenuFilter3 = new SpecificationMenuFilter(101, 200);
        menuFilters1.add(specificationMenuFilter);
        menuFilters1.add(specificationMenuFilter2);
        menuFilters1.add(specificationMenuFilter3);

        ManuMenuFilter manuMenuFilter = new ManuMenuFilter(TowerPart.MANU_ZHIWAN) {
            @Override
            public boolean match(TowerPart part) {
                return part.getManuHourZhiWan() > 0;
            }
        };
        ManuMenuFilter manuMenuFilter2 = new ManuMenuFilter(TowerPart.MANU_KAIHE) {
            @Override
            public boolean match(TowerPart part) {
                return part.getManuHourKaiHe() > 0;
            }
        };
        ManuMenuFilter manuMenuFilter3 = new ManuMenuFilter(TowerPart.MANU_CUT_ANGEL) {
            @Override
            public boolean match(TowerPart part) {
                return part.getManuHourCutAngle() > 0;
            }
        };
        List<MenuFilter> menuFilters2 = new ArrayList<>();
        menuFilters2.add(manuMenuFilter);
        menuFilters2.add(manuMenuFilter2);
        menuFilters2.add(manuMenuFilter3);

        List<List<MenuFilter>> lists = new ArrayList<>();
        lists.add(menuFilters1);
        lists.add(menuFilters2);
        return lists;
    }
}
