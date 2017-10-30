package com.xerofox.fileviewer.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xerofox.fileviewer.ui.repo.TowerPartViewModel;
import com.xerofox.fileviewer.ui.search.SearchViewModel;
import com.xerofox.fileviewer.viewmodel.XeroFoxViewModelFactory;

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
    abstract ViewModel bindRepoViewModel(TowerPartViewModel towerPartViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(XeroFoxViewModelFactory factory);
}
