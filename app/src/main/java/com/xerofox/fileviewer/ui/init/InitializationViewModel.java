package com.xerofox.fileviewer.ui.init;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.xerofox.fileviewer.repository.InitializationRepository;
import com.xerofox.fileviewer.util.AbsentLiveData;

import javax.inject.Inject;

public class InitializationViewModel extends ViewModel {
    private final LiveData<InitStatus> status;
    private final MutableLiveData<String> port = new MutableLiveData<>();

    @Inject
    public InitializationViewModel(InitializationRepository repository) {
        status = Transformations.switchMap(port, string -> {
            if (string == null || string.trim().length() == 0) {
                return AbsentLiveData.create();
            } else {
                return repository.init(string);
            }
        });
    }

    void doInit(String originPort) {
        if (TextUtils.equals(originPort, port.getValue())) {
            return;
        }
        port.setValue(originPort);
    }

    LiveData<InitStatus> getStatus() {
        return status;
    }
}
