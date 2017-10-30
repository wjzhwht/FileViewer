package com.xerofox.fileviewer.di;

import com.xerofox.fileviewer.ui.repo.TowerPartViewModel;
import com.xerofox.fileviewer.ui.search.SearchViewModel;

import dagger.Subcomponent;

@Subcomponent
public interface ViewModelSubComponent {
    @Subcomponent.Builder
    interface Builder {
        ViewModelSubComponent build();
    }
    SearchViewModel searchViewModel();
    TowerPartViewModel towerPartViewModel();
}
