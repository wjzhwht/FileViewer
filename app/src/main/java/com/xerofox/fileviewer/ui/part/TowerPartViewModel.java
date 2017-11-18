package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.ArrayList;

import javax.inject.Inject;

public class TowerPartViewModel extends ViewModel {
    @VisibleForTesting
    private final MutableLiveData<TowerType> towerType = new MutableLiveData<>();
    private final LiveData<ArrayList<TowerPart>> towerParts;

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        towerParts = Transformations.switchMap(towerType, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getTowerType(input);
            }
        });



    }

    void setTowerType(TowerType towerType) {
        if (Objects.equals(this.towerType.getValue(), towerType)) {
            return;
        }
        this.towerType.setValue(towerType);
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
