package com.xerofox.fileviewer.vo;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;

import java.util.ArrayList;
import java.util.List;

public class TowerType {
    public static final int ATTACH_FILE_FLAG_CARD = 0x01;
    public static final int ATTACH_FILE_FLAG_NC = 0x02;
    public static final int ATTACH_FILE_FLAG_MODEL = 0x04;

    private int id;
    private String name;
    private double version;
    private int projectId;
    private String projectName;
    private String aliasCode;
    private boolean designFinished;
    private String viceType;
    private String voltGrade;
    private int partCount;
    private List<TowerPart> partArr;

    public TowerType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TowerType(ByteBufferReader br) {
        this.version = br.readDouble();
        this.id = br.readInt();
        this.projectId = br.readInt();
        this.projectName = br.readString();
        this.name = br.readString();
        this.aliasCode = br.readString();
        this.designFinished = br.readBoolean();
        this.viceType = br.readString();
        this.voltGrade = br.readString();
        this.partCount = br.readInt();
        this.partArr = new ArrayList<>(partCount);
        for (int i = 0; i < partCount; i++) {
            partArr.add(new TowerPart(br));
        }
    }

    public void saveByteArray(ByteBufferWriter br) {
        br.write(this.version);
        br.write(this.id);
        br.write(this.projectId);
        br.write(this.projectName);
        br.write(this.name);
        br.write(this.aliasCode);
        br.write(this.designFinished);
        br.write(this.viceType);
        br.write(this.voltGrade);
        br.write(this.partCount);
        if (partArr != null && !partArr.isEmpty()) {
            for (TowerPart part : partArr) {
                part.saveByteArray(br);
            }
        }
    }

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

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
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

    public String getAliasCode() {
        return aliasCode;
    }

    public void setAliasCode(String aliasCode) {
        this.aliasCode = aliasCode;
    }

    public boolean isDesignFinished() {
        return designFinished;
    }

    public void setDesignFinished(boolean designFinished) {
        this.designFinished = designFinished;
    }

    public String getViceType() {
        return viceType;
    }

    public void setViceType(String viceType) {
        this.viceType = viceType;
    }

    public String getVoltGrade() {
        return voltGrade;
    }

    public void setVoltGrade(String voltGrade) {
        this.voltGrade = voltGrade;
    }

    public int getPartCount() {
        return partCount;
    }

    public void setPartCount(int partCount) {
        this.partCount = partCount;
    }

    public List<TowerPart> getPartArr() {
        return partArr;
    }

    public void setPartArr(List<TowerPart> partArr) {
        this.partArr = partArr;
    }
}
