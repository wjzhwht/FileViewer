package com.xero.dropdownmenu;

import android.view.View;
import android.widget.FrameLayout;

public interface MenuAdapter {
    int getMenuCount();

    String getMenuTitle(int position);

    View getView(int position, FrameLayout parentContainer);
}
