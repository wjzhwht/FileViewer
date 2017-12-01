package com.xerofox.fileviewer.vo;

public interface MenuFilter {
    String CLEAR = "重置";

    String getText();

    boolean match(TowerPart part);

}
