package com.xerofox.fileviewer.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xerofox.fileviewer.ui.index.SearchViewModel;
import com.xerofox.fileviewer.ui.part.TowerPartViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    abstract ViewModel bindSearchViewModel(SearchViewModel searchViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TowerPartViewModel.class)
    abstract ViewModel bindTowerPartViewModel(TowerPartViewModel towerPartViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FileViewModelFactory factory);
}
