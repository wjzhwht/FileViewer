package com.xerofox.fileviewer.ui.common;

public class DownloadState {
    private final boolean downloading;
    private final String errorMessage;
    private boolean handledError = false;

    public DownloadState(boolean downloading, String errorMessage) {
        this.downloading = downloading;
        this.errorMessage = errorMessage;
    }

    public boolean isDownloading() {
        return downloading;
    }

    String getErrorMessage() {
        return errorMessage;
    }

    String getErrorMessageIfNotHandled() {
        if (handledError) {
            return null;
        }
        handledError = true;
        return errorMessage;
    }
}
