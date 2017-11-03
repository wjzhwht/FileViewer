package com.xerofox.fileviewer.ui.tower;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.vo.TowerPart;

import java.util.List;

import javax.inject.Inject;

public class TowerPartViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> query = new MutableLiveData<>();
    private final LiveData<List<TowerPart>> towerParts;

    @Inject
    public TowerPartViewModel(TowerRepository repository) {
        towerParts = Transformations.switchMap(query, input -> {
            if (input.isEmpty()) {
                return AbsentLiveData.create();
            } else {
                return repository.getTowerParts();
            }

        });
    }

    public LiveData<List<TowerPart>> getTowerParts() {
        return towerParts;
    }
}
