package com.xerofox.fileviewer.vo;

public interface MenuFilter {

    String getText();

    boolean match(TowerPart part);

}
