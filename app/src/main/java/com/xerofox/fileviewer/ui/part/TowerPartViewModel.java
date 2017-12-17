package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.SparseArray;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.MenuFilter;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TowerPartViewModel extends ViewModel {
    @VisibleForTesting
    private final MutableLiveData<Param> param = new MutableLiveData<>();
    private final MutableLiveData<Task> task = new MutableLiveData<>();
    private final LiveData<ArrayList<TowerPart>> towerParts;
    private final LiveData<List<String>> filterTitles;
    private final LiveData<List<List<MenuFilter>>> filterLists;
    private final CheckUpdateHandler checkUpdateHandler;

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        checkUpdateHandler = new CheckUpdateHandler(repository);
        towerParts = Transformations.switchMap(param, input -> {
            if (input == null || input.task == null) {
                return AbsentLiveData.create();
            } else {
                LiveData<ArrayList<TowerPart>> towerParts = repository.getTowerParts(input.task, input.array);
                return towerParts;
            }
        });

        filterLists = Transformations.switchMap(task, data -> {
            if (data == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getFilterLists(data);
            }
        });

        filterTitles = Transformations.switchMap(filterLists, data -> {
            if (data == null || data.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return repository.getFilterTitles();
            }
        });
    }

    LiveData<ArrayList<TowerPart>> getTowerParts() {
        return towerParts;
    }

    LiveData<List<List<MenuFilter>>> getFilterLists() {
        return filterLists;
    }

    LiveData<List<String>> getFilterTitles() {
        return filterTitles;
    }

    void setTask(Task task) {
        if (Objects.equals(this.task.getValue(), task)) {
            return;
        }
        this.task.setValue(task);
        Param param = new Param(task);
        this.param.setValue(param);
    }

    void setFilter(int position, MenuFilter menuFilter) {
        Param param = this.param.getValue().setFilter(position, menuFilter);
        if (Objects.equals(param, this.param.getValue())) {
            return;
        }
        this.param.setValue(param);
    }

    void checkUpdate(int id, ArrayList<TowerPart> data) {
        checkUpdateHandler.checkUpdate(id, data);
    }

    LiveData<Resource<List<Integer>>> getCheckUpdateParts() {
        return checkUpdateHandler.getUpdateLiveData();
    }

    static class CheckUpdateHandler implements Observer<Resource<List<Integer>>> {
        private final TowerRepository repository;
        private LiveData<Resource<List<Integer>>> updateLiveData;
        private final MutableLiveData<Resource<List<Integer>>> result = new MutableLiveData<>();

        @VisibleForTesting
        CheckUpdateHandler(TowerRepository repository) {
            this.repository = repository;
            unregister();
        }

        void checkUpdate(int id, List<TowerPart> tasks) {
            if (tasks == null || tasks.isEmpty()) {
                return;
            }
            unregister();
            updateLiveData = repository.checkUpdate(id, tasks);
            updateLiveData.observeForever(this);
        }

        @Override
        public void onChanged(@Nullable Resource<List<Integer>> result) {
            unregister();
            this.result.setValue(result);
        }

        private void unregister() {
            if (updateLiveData != null) {
                updateLiveData.removeObserver(this);
                updateLiveData = null;
            }
        }

        LiveData<Resource<List<Integer>>> getUpdateLiveData() {
            return result;
        }
    }

    static class Param {
        public final Task task;
        final SparseArray<MenuFilter> array;
        final long timeStamp;

        Param(Task task) {
            this.task = task;
            array = new SparseArray<>(5);
            timeStamp = System.currentTimeMillis();
        }

        Param(Task task, SparseArray<MenuFilter> array) {
            this.task = task;
            this.array = array;
            timeStamp = System.currentTimeMillis();
        }

        Param setFilter(int position, MenuFilter filter) {
            if (position < array.size() && Objects.equals(array.get(position), filter)) {
                return this;
            }
            array.put(position, filter);
            return new Param(task, array);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Param param = (Param) o;

            return timeStamp == param.timeStamp;
        }

        @Override
        public int hashCode() {
            return (int) (timeStamp ^ (timeStamp >>> 32));
        }
    }

}
