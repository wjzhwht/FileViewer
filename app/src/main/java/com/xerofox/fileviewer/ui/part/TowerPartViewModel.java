package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.MenuFilter;
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

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        towerParts = Transformations.switchMap(param, input -> {
            if (input == null || input.task == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getTowerParts(input.task, input.filter, input.filter1, input.filter2);
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

    void setTask(Task task) {
        if (Objects.equals(this.task.getValue(), task)) {
            return;
        }
        this.task.setValue(task);
        Param param = new Param(task, null, null, null);
        this.param.setValue(param);
    }

    void setQuery(String query) {
        MenuFilter filter = new PartNoMenuFilter(query);
        Param param = new Param(this.task.getValue(), filter, this.param.getValue().getFilter1(), this.param.getValue().getFilter2());
        if (Objects.equals(param, this.param.getValue())) {
            return;
        }
        this.param.setValue(param);
    }

    void setFilter1(MenuFilter filter1) {
        Param param = new Param(this.task.getValue(), this.param.getValue().getFilter(), filter1, this.param.getValue().getFilter2());
        if (Objects.equals(param, this.param.getValue())) {
            return;
        }
        this.param.setValue(param);
    }

    void setFilter2(MenuFilter filter2) {
        Param param = new Param(this.task.getValue(), this.param.getValue().getFilter(), this.param.getValue().getFilter1(), filter2);
        if (Objects.equals(param, this.param.getValue())) {
            return;
        }
        this.param.setValue(param);
    }

    LiveData<ArrayList<TowerPart>> getTowerParts() {
        return towerParts;
    }

    public LiveData<List<List<MenuFilter>>> getFilterLists() {
        return filterLists;
    }

    public LiveData<List<String>> getFilterTitles() {
        return filterTitles;
    }

    static class Param {
        public final Task task;
        public final MenuFilter filter;
        public final MenuFilter filter1;
        public final MenuFilter filter2;

        public Param(Task task, MenuFilter filter, MenuFilter filter1, MenuFilter filter2) {
            this.task = task;
            this.filter = filter;
            this.filter1 = filter1;
            this.filter2 = filter2;
        }

        public Task getTask() {
            return task;
        }

        public MenuFilter getFilter() {
            return filter;
        }

        public MenuFilter getFilter1() {
            return filter1;
        }

        public MenuFilter getFilter2() {
            return filter2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Param param = (Param) o;

            if (task != null ? !task.equals(param.task) : param.task != null) return false;
            if (filter != null ? !filter.equals(param.filter) : param.filter != null) return false;
            if (filter1 != null ? !filter1.equals(param.filter1) : param.filter1 != null)
                return false;
            return filter2 != null ? filter2.equals(param.filter2) : param.filter2 == null;
        }

        @Override
        public int hashCode() {
            int result = task != null ? task.hashCode() : 0;
            result = 31 * result + (filter != null ? filter.hashCode() : 0);
            result = 31 * result + (filter1 != null ? filter1.hashCode() : 0);
            result = 31 * result + (filter2 != null ? filter2.hashCode() : 0);
            return result;
        }
    }

}
