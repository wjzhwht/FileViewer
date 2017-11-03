package com.xerofox.fileviewer.ui.common;

import android.support.v4.app.FragmentManager;

import com.xerofox.fileviewer.MainActivity;
import com.xerofox.fileviewer.R;
import com.xerofox.fileviewer.ui.tower.TowerPartFragment;
import com.xerofox.fileviewer.ui.search.SearchFragment;

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

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToRepo(String owner, String name) {
        TowerPartFragment fragment = TowerPartFragment.create(owner, name);
        String tag = "repo" + "/" + owner + "/" + name;
        fragmentManager.beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
