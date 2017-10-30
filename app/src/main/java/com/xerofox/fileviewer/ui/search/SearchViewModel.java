package com.xerofox.fileviewer.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> project = new MutableLiveData<>();

    private final MutableLiveData<String> towerType = new MutableLiveData<>();

    private final LiveData<List<TowerType>> towerTypes;

    private final LiveData<List<Project>> projects;

    @Inject
    SearchViewModel(TowerRepository towerRepository) {
        projects = Transformations.switchMap(project, search -> {
            if (search == null || search.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return towerRepository.searchProject(search);
            }
        });

        towerTypes = Transformations.switchMap(towerType, search -> {
            if (search == null || search.trim().length() == 0 || TextUtils.isEmpty(project.getValue())) {
                return AbsentLiveData.create();
            } else {
                return towerRepository.searchTowerType(project.getValue(),search);
            }
        });
    }

    public LiveData<List<Project>> getProjects() {
        return projects;
    }

    public LiveData<List<TowerType>> getTowerTypes() {
        return towerTypes;
    }

    public void setProject(@NonNull String originInput) {
        String input = originInput.toLowerCase(Locale.getDefault()).trim();
        if (Objects.equals(input, project.getValue())) {
            return;
        }
        project.setValue(input);
    }

    public void setTowerType(@NonNull String originInput) {
        String input = originInput.toLowerCase(Locale.getDefault()).trim();
        if (Objects.equals(input, towerType.getValue())) {
            return;
        }
        towerType.setValue(input);
    }
}
