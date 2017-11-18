package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.ArrayList;

import javax.inject.Inject;

public class TowerPartViewModel extends ViewModel {
    @VisibleForTesting
    private final MutableLiveData<Task> task = new MutableLiveData<>();
    private final LiveData<ArrayList<TowerPart>> towerParts;

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        towerParts = Transformations.switchMap(task, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getTowerParts(input);
            }
        });
    }

    void setTask(Task task) {
        if (Objects.equals(this.task.getValue(), task)) {
            return;
        }
        this.task.setValue(task);
    }

    void setQuery(String query){

    }

    LiveData<ArrayList<TowerPart>> getTowerParts() {
        return towerParts;
    }

//    static class Param {
//        public final TowerType towerType;
//        public final String query;
//
//        public Param(TowerType towerType, String query) {
//            this.towerType = towerType;
//            this.query = query;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Param param = (Param) o;
//
//            if (towerType != null ? !towerType.equals(param.towerType) : param.towerType != null)
//                return false;
//            return query != null ? query.equals(param.query) : param.query == null;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = towerType != null ? towerType.hashCode() : 0;
//            result = 31 * result + (query != null ? query.hashCode() : 0);
//            return result;
//        }
//    }

}
