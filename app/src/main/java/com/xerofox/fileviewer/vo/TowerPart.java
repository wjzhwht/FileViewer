package com.xerofox.fileviewer.vo;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;

public class TowerPart {
    private int id;

    private int segI;

    private String segPrefix;

    private String segSuffix;

    /**
     * 段号
     */
    private String segNo;

    private String materialMark;

    /**
     * 宽度
     */
    private double wide;

    /**
     * 厚度
     */
    private double thick;

    /**
     * 长度
     */
    private double length;

    /**
     *
     */
    private double wingAngle;

    /**
     *
     */
    private double realWeight;

    /**
     *
     */
    private int num;

    /**
     *
     */
    private String notes;

    /**
     *
     */
    private int fileCount;

    private PartFile partFile;

    public TowerPart() {
    }

    public TowerPart(ByteBufferReader br) {
        this.id = br.readInt();
        this.segI = br.readInt();
        this.segPrefix = br.readString();
        this.segSuffix = br.readString();
        this.segNo = br.readString();
        this.materialMark = br.readString();
        this.wide = br.readDouble();
        this.thick = br.readDouble();
        this.length = br.readDouble();
        this.wingAngle = br.readDouble();
        this.realWeight = br.readDouble();
        this.num = br.readInt();
        this.notes = br.readString();
        this.fileCount = br.readInt();
        this.partFile = new PartFile(br);
    }

    public void saveByteArray(ByteBufferWriter br) {
        br.write(this.id);
        br.write(this.segI);
        br.write(this.segPrefix);
        br.write(this.segSuffix);
        br.write(this.segNo);
        br.write(this.materialMark);
        br.write(this.wide);
        br.write(this.thick);
        br.write(this.length);
        br.write(this.wingAngle);
        br.write(this.realWeight);
        br.write(this.num);
        br.write(this.notes);
        br.write(this.fileCount);
        this.partFile.saveByteArray(br);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSegI() {
        return segI;
    }

    public void setSegI(int segI) {
        this.segI = segI;
    }

    public String getSegPrefix() {
        return segPrefix;
    }

    public void setSegPrefix(String segPrefix) {
        this.segPrefix = segPrefix;
    }

    public String getSegSuffix() {
        return segSuffix;
    }

    public void setSegSuffix(String segSuffix) {
        this.segSuffix = segSuffix;
    }

    public String getSegNo() {
        return segNo;
    }

    public void setSegNo(String segNo) {
        this.segNo = segNo;
    }

    public String getMaterialMark() {
        return materialMark;
    }

    public void setMaterialMark(String materialMark) {
        this.materialMark = materialMark;
    }

    public double getWide() {
        return wide;
    }

    public void setWide(double wide) {
        this.wide = wide;
    }

    public double getThick() {
        return thick;
    }

    public void setThick(double thick) {
        this.thick = thick;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWingAngle() {
        return wingAngle;
    }

    public void setWingAngle(double wingAngle) {
        this.wingAngle = wingAngle;
    }

    public double getRealWeight() {
        return realWeight;
    }

    public void setRealWeight(double realWeight) {
        this.realWeight = realWeight;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public PartFile getPartFile() {
        return partFile;
    }

    public void setPartFile(PartFile partFile) {
        this.partFile = partFile;
    }

}
