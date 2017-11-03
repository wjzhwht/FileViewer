package com.xerofox.fileviewer.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.xerofox.fileviewer.ui.init.InitStatus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InitializationRepository {
    private MutableLiveData<InitStatus> status;

    @Inject
    public InitializationRepository() {
    }

    public LiveData<InitStatus> init(String string) {
        return null;
    }
}
