package com.xerofox.fileviewer.ui.index;

import android.support.v4.app.FragmentManager;

import com.xerofox.fileviewer.MainActivity;
import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.helper.SettingHelper;
import com.xerofox.fileviewer.ui.download.DownloadFragment;
import com.xerofox.fileviewer.ui.settings.SettingsFragment;
import com.xerofox.fileviewer.ui.task.TaskFragment;
import com.xerofox.fileviewer.ui.task.TaskManagerFragment;

import javax.inject.Inject;

/**
 * A utility class that handles navigation in {@link MainActivity}.
 */
public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToFunction() {
        FunctionFragment functionFragment = FunctionFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(containerId, functionFragment)
                .commitAllowingStateLoss();
    }

    public void navigateTask(){
        if (SettingHelper.isAutoQuery()){
            navigateToDownload();
        } else {
            navigateToSearch();
        }
    }

    public void navigateToSearch() {
        TaskFragment searchFragment = new TaskFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToSettings() {
        SettingsFragment fragment = SettingsFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    void navigateToDownload() {
        DownloadFragment fragment = DownloadFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss();
    }

    void navigateToTaskManager() {
        TaskManagerFragment fragment = TaskManagerFragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

}
