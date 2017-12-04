package com.xerofox.fileviewer.vo;

public interface MenuFilter {
    String CLEAR = "不限";

    String getText();

    boolean match(TowerPart part);

}
