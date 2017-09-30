package com.xerofox.fileviewer.helper;

import com.xerofox.fileviewer.util.SPUtils;

public class SettingHelper {
    private static final String SP_SERVER_PORT = "server port";


    public static Boolean isServerPortSetted() {
        return !SPUtils.getInstance().getString(SP_SERVER_PORT, "").isEmpty();
    }

    public static String getServerPort() {
        return SPUtils.getInstance().getString(SP_SERVER_PORT);
    }

    public static void setServerPort(String port) {
        SPUtils.getInstance().put(SP_SERVER_PORT, port);
    }
}
