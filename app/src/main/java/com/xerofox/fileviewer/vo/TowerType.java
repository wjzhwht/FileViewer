package com.xerofox.fileviewer.vo;

import java.util.List;

public class TowerType {
    public static final int ATTACH_FILE_FLAG_CARD = 0x01;
    public static final int ATTACH_FILE_FLAG_NC = 0x02;
    public static final int ATTACH_FILE_FLAG_MODEL = 0x04;

    private int id;
    private String name;
    /**
     * @{ATTACH_FILE_FLAG_CARD} 关联工艺卡文件
     * @{ATTACH_FILE_FLAG_NC} 关联NC数据文件
     * @{ATTACH_FILE_FLAG_MODEL} 关联实体数据文件
     */
    private int attachFileFlag;
    private List<TowerPart> partArr;

    private int projectId;
    private String projectName;

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

    public int getAttachFileFlag() {
        return attachFileFlag;
    }

    public void setAttachFileFlag(int attachFileFlag) {
        this.attachFileFlag = attachFileFlag;
    }

    public List<TowerPart> getPartArr() {
        return partArr;
    }

    public void setPartArr(List<TowerPart> partArr) {
        this.partArr = partArr;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
