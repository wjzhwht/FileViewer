package com.xerofox.fileviewer.helper;

import com.xerofox.fileviewer.util.SPUtils;

public class SettingHelper {
    private static final String SP_SERVER_PORT = "server port";
    private static final String SP_USER_NAME = "user name";
    private static final String SP_PASSWORD = "password";


    public static Boolean isServerPortSetted() {
        return !SPUtils.getInstance().getString(SP_SERVER_PORT, "").isEmpty();
    }

    public static String getServerPort() {
        return SPUtils.getInstance().getString(SP_SERVER_PORT);
    }

    public static void setServerPort(String port) {
        SPUtils.getInstance().put(SP_SERVER_PORT, port);
    }

    public static String getUserName() {
        return SPUtils.getInstance().getString(SP_USER_NAME);
    }

    public static void setUserName(String name) {
        SPUtils.getInstance().put(SP_USER_NAME, name);
    }

    public static String getPassword() {
        return SPUtils.getInstance().getString(SP_PASSWORD);
    }

    public static void setPassword(String password) {
        SPUtils.getInstance().put(SP_PASSWORD, password);
    }


}
