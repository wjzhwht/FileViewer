package com.xerofox.fileviewer.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.xerofox.fileviewer.ui.download.DownloadViewModel;
import com.xerofox.fileviewer.ui.part.TowerPartViewModel;
import com.xerofox.fileviewer.ui.task.TaskViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TaskViewModel.class)
    abstract ViewModel bindSearchViewModel(TaskViewModel taskViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DownloadViewModel.class)
    abstract ViewModel bindDownloadViewModel(DownloadViewModel downloadViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TowerPartViewModel.class)
    abstract ViewModel bindTowerPartViewModel(TowerPartViewModel towerPartViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FileViewModelFactory factory);
}
