package com.xerofox.fileviewer.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.xerofox.fileviewer.repository.TowerRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;
import com.xerofox.fileviewer.util.Objects;
import com.xerofox.fileviewer.vo.Project;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.TowerType;

import java.util.List;

import javax.inject.Inject;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<Param> query = new MutableLiveData<>();

    private final LiveData<Resource<List<TowerType>>> towerTypes;

    private final LiveData<Resource<List<Project>>> projects;

    @Inject
    SearchViewModel(TowerRepository repository) {
        projects = repository.loadProject();

//        projects = Transformations.switchMap(query,search ->{
//            if (search == null || TextUtils.isEmpty(search.tower)) {
//                return AbsentLiveData.create();
//            } else {
//                return repository.searchProject(search.project);
//            }
//        });

        towerTypes = Transformations.switchMap(query, search -> {
            if (search == null || TextUtils.isEmpty(search.tower)) {
                return AbsentLiveData.create();
            } else {
                return repository.searchTowerType(search.project, search.tower);
            }
        });
    }

    LiveData<Resource<List<Project>>> getProjects() {
        return projects;
    }

    LiveData<Resource<List<TowerType>>> getTowerTypes() {
        return towerTypes;
    }

    void setSearch(String project, String tower) {
        Param param = new Param(project, tower);
        if (Objects.equals(query.getValue(), param)) {
            return;
        }
        query.setValue(param);
    }

    static class Param {
        public final String project;
        public final String tower;

        Param(String project, String tower) {
            this.project = project;
            this.tower = tower;
        }
    }
}
