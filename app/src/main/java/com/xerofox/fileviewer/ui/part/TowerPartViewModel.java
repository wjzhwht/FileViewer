package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Filter;
import com.xerofox.fileviewer.vo.FilterQuery;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class TowerPartViewModel extends ViewModel {
    @VisibleForTesting
    private final MutableLiveData<Param> param = new MutableLiveData<>();
    private final MutableLiveData<Task> task = new MutableLiveData<>();
    private final LiveData<ArrayList<TowerPart>> towerParts;
    private final LiveData<List<Filter>> filters;

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        towerParts = Transformations.switchMap(param, input -> {
            if (input == null || input.task == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getTowerParts(input.task, input.filter);
            }
        });

        filters = Transformations.switchMap(task, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            } else {
//                List<FilterQuery> filter = Collections.emptyList();
//                if (param.getValue() != null) {
//                    filter = param.getValue().filter;
//                }
//                return repository.getFilters(input, filter);
                return repository.getFilters(input);
            }
        });
    }

    void setTask(Task task) {
        if (Objects.equals(this.task.getValue(), task)) {
            return;
        }
        this.task.setValue(task);
        Param param = new Param(task, Collections.emptyList());
        this.param.setValue(param);
    }

    void clearFilters() {
        setFilters(Collections.emptyList());
    }

    private void setFilters(List<FilterQuery> filters) {
        if (this.param.getValue() == null || this.param.getValue().task == null || Objects.equals(this.param.getValue().filter, filters)) {
            return;
        }
        Param param = new Param(this.param.getValue().task, filters);
        this.param.setValue(param);
    }

    void doFilters() {
        if (this.filters.getValue() == null
                || this.filters.getValue().isEmpty()) {
            return;
        }
        List<FilterQuery> filterQueries = new ArrayList<>();
        for (Filter filter : filters.getValue()) {
            FilterQuery filterQuery = new FilterQuery(filter.getName(), filter.getType());
            for (Filter.Item item : filter.getItems()) {
                if (item.isSelected()) {
                    filterQuery.getItems().add(item.getName());
                }
            }
            if (!filterQuery.getItems().isEmpty()) {
                filterQueries.add(filterQuery);
            }
        }
        setFilters(filterQueries);
    }

    LiveData<ArrayList<TowerPart>> getTowerParts() {
        return towerParts;
    }

    public LiveData<List<Filter>> getFilters() {
        return filters;
    }

//    public List<FilterQuery> getQueryFilter() {
//        if (param.getValue() == null) {
//            return null;
//        }
//        return param.getValue().filter;
//    }

    static class Param {
        public final Task task;
        public final List<FilterQuery> filter;

        Param(Task task, List<FilterQuery> filter) {
            this.task = task;
            this.filter = filter;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Param param = (Param) o;

            if (task != null ? !task.equals(param.task) : param.task != null) return false;
            return filter != null ? filter.equals(param.filter) : param.filter == null;
        }

        @Override
        public int hashCode() {
            int result = task != null ? task.hashCode() : 0;
            result = 31 * result + (filter != null ? filter.hashCode() : 0);
            return result;
        }
    }

}
