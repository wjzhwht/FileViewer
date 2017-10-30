package com.xerofox.fileviewer.vo;

import java.util.List;

public class Project {
    private int id;
    private String name;
    private List<TowerType> towerTypeArr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TowerType> getTowerTypeArr() {
        return towerTypeArr;
    }

    public void setTowerTypeArr(List<TowerType> towerTypeArr) {
        this.towerTypeArr = towerTypeArr;
    }
}
