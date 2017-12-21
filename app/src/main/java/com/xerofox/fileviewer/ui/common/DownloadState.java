package com.xerofox.fileviewer.ui.common;

import java.util.List;

public class DownloadState<T> {
    private final boolean downloading;
    private final String errorMessage;
    private boolean handledError = false;
    private List<T> data;

    public DownloadState(boolean downloading, String errorMessage) {
        this.downloading = downloading;
        this.errorMessage = errorMessage;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorMessageIfNotHandled() {
        if (handledError) {
            return null;
        }
        handledError = true;
        return errorMessage;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
