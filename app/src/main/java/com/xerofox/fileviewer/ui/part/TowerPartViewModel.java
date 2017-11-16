package com.xerofox.fileviewer.ui.part;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.TowerType;

import javax.inject.Inject;

public class TowerPartViewModel extends ViewModel {
    @VisibleForTesting
    private final MutableLiveData<TowerType> query = new MutableLiveData<>();
    private final LiveData<TowerType> towerParts;
    private final LiveData<String> rootPath;

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        towerParts = Transformations.switchMap(query, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getTowerType(input);
            }
        });

        rootPath = Transformations.switchMap(query, input -> {
            if (input == null) {
                return AbsentLiveData.create();
            } else {
                return repository.getRootPath(input);
            }
        });
    }

    void setTowerType(TowerType towerType) {
        if (Objects.equals(query.getValue(), towerType)) {
            return;
        }
        query.setValue(towerType);
    }

    LiveData<TowerType> getTowerParts() {
        return towerParts;
    }

    LiveData<String> geRootPath(){
        return rootPath;
    }
}
