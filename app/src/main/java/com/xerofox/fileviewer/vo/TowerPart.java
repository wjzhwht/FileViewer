package com.xerofox.fileviewer.vo;

public class TowerPart {
    /**
     * 构件类型(角钢、钢板、钢管、槽钢、扁铁等)
     */
    private byte partType;

    /**
     * 构件类型名称
     */
    private String materialName;

    /**
     * 段号
     */
    private String segNo;

    /**
     * 件号
     */
    private String partLabel;

    /**
     * 材质
     */
    private String steelMaterial;

    /**
     * 宽度
     */
    private int width;

    /**
     * 厚度
     */
    private int thick;

    /**
     * 长度
     */
    private int len;

    /**
     * 规格
     */
    private String spec;

    /**
     * 工艺信息
     */
    private String processStr;

    /**
     * 关联文件数组
     */
    private byte[] fileArr;

    /**
     * 工程名
     */
    private String prjName;

    /**
     * 塔型名
     */
    private String towerTypeName;

    public byte getPartType() {
        return partType;
    }

    public void setPartType(byte partType) {
        this.partType = partType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSegNo() {
        return segNo;
    }

    public void setSegNo(String segNo) {
        this.segNo = segNo;
    }

    public String getPartLabel() {
        return partLabel;
    }

    public void setPartLabel(String partLabel) {
        this.partLabel = partLabel;
    }

    public String getSteelMaterial() {
        return steelMaterial;
    }

    public void setSteelMaterial(String steelMaterial) {
        this.steelMaterial = steelMaterial;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getThick() {
        return thick;
    }

    public void setThick(int thick) {
        this.thick = thick;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getProcessStr() {
        return processStr;
    }

    public void setProcessStr(String processStr) {
        this.processStr = processStr;
    }

    public byte[] getFileArr() {
        return fileArr;
    }

    public void setFileArr(byte[] fileArr) {
        this.fileArr = fileArr;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getTowerTypeName() {
        return towerTypeName;
    }

    public void setTowerTypeName(String towerTypeName) {
        this.towerTypeName = towerTypeName;
    }
}
