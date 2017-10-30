package com.xerofox.fileviewer.util;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ToastUtils {
    private static final String TAG = "ToastUtils";
    private static Toast toast;

    private static View view;

    private ToastUtils() {
    }

    private static void getToast() {
        if (toast == null) {
            toast = new Toast(Util.getApp());
        }
        if (view == null) {
            view = Toast.makeText(Util.getApp(), "", Toast.LENGTH_SHORT).getView();
        }
        toast.setView(view);
    }


    public static void showToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(CharSequence msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    private static void showToast(CharSequence msg, int duration) {
        try {
            if (TextUtils.isEmpty(msg)) return;
            getToast();
            toast.setText(msg);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static void showToast(int resId, int duration) {
        try {
            if (resId == 0) {
                return;
            }
            getToast();
            toast.setText(resId);
            toast.setDuration(duration);
            toast.show();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }

}
