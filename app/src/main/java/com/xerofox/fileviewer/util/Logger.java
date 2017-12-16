package com.xerofox.fileviewer.util;
import android.util.Log;

public class Logger {
    private static final String TAG = "FileViewer";

    public static void v(String message) {
        Log.v(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void w(String message) {
        Log.w(TAG, message);
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }
    public static void writeLogFile(){
        try{
//            LocalFileHelper fileHelper = new LocalFileHelper();
//            String logFilePath= fileHelper.GetRootPath()+ File.separator+"logFile.log";
//            Runtime.getRuntime().exec("logcat -f -v time "+logFilePath);
        }catch(Exception e){
        	//CrashHandler.saveExceptionInfo2File(e);
        }
    }
}

