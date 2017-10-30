package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;

import com.xerofox.fileviewer.ui.init.InitStatus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InitializationRepository {

    @Inject
    public InitializationRepository() {
    }

    public LiveData<InitStatus> init(String string) {
        return null;
    }
}
