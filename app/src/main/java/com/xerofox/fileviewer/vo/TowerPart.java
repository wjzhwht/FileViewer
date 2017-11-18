package com.xerofox.fileviewer.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;

public class TowerPart implements Parcelable {

    /**
     * 工程Id
     */
    private int projectId;

    /**
     * 工程名
     */
    private String projectName;

    /**
     * 塔型Id
     */
    private int towerTypeId;

    /**
     * 塔型名
     */
    private String towerTypeName;

    /**
     * 构件Id
     */
    private int id;

    /**
     * 构件编号
     */
    private String partNo;

    /**
     * 加工数量
     */
    private int num;

    /**
     * 段号
     */
    private String segStr;

    /**
     * 材质
     */
    private String materialMark;

    /**
     * 规格
     */
    private String specification;

    /**
     * 肢宽
     */
    private double wide;

    /**
     * 肢厚
     */
    private double thick;

    /**
     * 长度
     */
    private double length;

    /**
     * 开合角
     */
    private double wingAngle;

    /**
     * 重量
     */
    private double realWeight;

    /**
     * 备注
     */
    private String notes;

    //-------------构件工艺信息

    /**
     * 焊接
     */
    private int manuHourWeld;

    /**
     * 制弯
     */
    private int manuHourZhiWan;

    /**
     * 切角
     */
    private int manuHourCutAngle;

    /**
     * 铲背
     */
    private int manuHourCutBer;

    /**
     * 清根
     */
    private int manuHourCutRoot;

    /**
     * 冲孔
     */
    private int manuHourClashHole;

    /**
     * 钻孔
     */
    private int manuHourBore;

    /**
     * 开合角
     */
    private int manuHourKaiHe;

    /**
     * 坡口
     */
    private int manuHourFillet;

    /**
     * 压扁
     */
    private int manuHourPushFlat;

    //-------------螺栓数量

    /**
     * 直径16螺栓数量
     */
    private int m16LsNum;

    /**
     * 直径20螺栓数量
     */
    private int m20LsNum;

    /**
     * 直径24螺栓数量
     */
    private int m24LsNum;

    /**
     * 其他直径螺栓数量
     */
    private int mxLsNum;

    private int fileCount;

    private PartFile partFile;

    public TowerPart() {
    }

    public TowerPart(ByteBufferReader br) {
        this.projectId = br.readInt();
        this.projectName = br.readString();
        this.towerTypeId = br.readInt();
        this.towerTypeName = br.readString();
        this.id = br.readInt();
        this.partNo = br.readString();
        this.num = br.readInt();
        this.segStr = br.readString();
        this.materialMark = br.readString();
        this.specification = br.readString();
        this.wide = br.readDouble();
        this.thick = br.readDouble();
        this.length = br.readDouble();
        this.wingAngle = br.readDouble();
        this.realWeight = br.readDouble();
        this.notes = br.readString();
        this.manuHourWeld = br.readInt();
        this.manuHourZhiWan = br.readInt();
        this.manuHourCutAngle = br.readInt();
        this.manuHourCutBer = br.readInt();
        this.manuHourCutRoot = br.readInt();
        this.manuHourClashHole = br.readInt();
        this.manuHourBore = br.readInt();
        this.manuHourKaiHe = br.readInt();
        this.manuHourFillet = br.readInt();
        this.manuHourPushFlat = br.readInt();
        this.m16LsNum = br.readInt();
        this.m20LsNum = br.readInt();
        this.m24LsNum = br.readInt();
        this.mxLsNum = br.readInt();
        this.fileCount = br.readInt();
        if (fileCount > 0) {
            this.partFile = new PartFile(br);
        }
    }

    public void saveByteArray(ByteBufferWriter br) {
        br.write(projectId);
        br.write(projectName);
        br.write(towerTypeId);
        br.write(towerTypeName);
        br.write(id);
        br.write(partNo);
        br.write(num);
        br.write(segStr);
        br.write(materialMark);
        br.write(specification);
        br.write(wide);
        br.write(thick);
        br.write(length);
        br.write(wingAngle);
        br.write(realWeight);
        br.write(notes);
        br.write(manuHourWeld);
        br.write(manuHourZhiWan);
        br.write(manuHourCutAngle);
        br.write(manuHourCutBer);
        br.write(manuHourCutRoot);
        br.write(manuHourClashHole);
        br.write(manuHourBore);
        br.write(manuHourKaiHe);
        br.write(manuHourFillet);
        br.write(manuHourPushFlat);
        br.write(m16LsNum);
        br.write(m20LsNum);
        br.write(m24LsNum);
        br.write(mxLsNum);
        this.partFile.saveByteArray(br);
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

    public int getTowerTypeId() {
        return towerTypeId;
    }

    public void setTowerTypeId(int towerTypeId) {
        this.towerTypeId = towerTypeId;
    }

    public String getTowerTypeName() {
        return towerTypeName;
    }

    public void setTowerTypeName(String towerTypeName) {
        this.towerTypeName = towerTypeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getSegStr() {
        return segStr;
    }

    public void setSegStr(String segStr) {
        this.segStr = segStr;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getMaterialMark() {
        return materialMark;
    }

    public void setMaterialMark(String materialMark) {
        this.materialMark = materialMark;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getManuHourWeld() {
        return manuHourWeld;
    }

    public void setManuHourWeld(int manuHourWeld) {
        this.manuHourWeld = manuHourWeld;
    }

    public int getManuHourZhiWan() {
        return manuHourZhiWan;
    }

    public void setManuHourZhiWan(int manuHourZhiWan) {
        this.manuHourZhiWan = manuHourZhiWan;
    }

    public int getManuHourCutAngle() {
        return manuHourCutAngle;
    }

    public void setManuHourCutAngle(int manuHourCutAngle) {
        this.manuHourCutAngle = manuHourCutAngle;
    }

    public int getManuHourCutBer() {
        return manuHourCutBer;
    }

    public void setManuHourCutBer(int manuHourCutBer) {
        this.manuHourCutBer = manuHourCutBer;
    }

    public int getManuHourCutRoot() {
        return manuHourCutRoot;
    }

    public void setManuHourCutRoot(int manuHourCutRoot) {
        this.manuHourCutRoot = manuHourCutRoot;
    }

    public int getManuHourClashHole() {
        return manuHourClashHole;
    }

    public void setManuHourClashHole(int manuHourClashHole) {
        this.manuHourClashHole = manuHourClashHole;
    }

    public int getManuHourBore() {
        return manuHourBore;
    }

    public void setManuHourBore(int manuHourBore) {
        this.manuHourBore = manuHourBore;
    }

    public int getManuHourKaiHe() {
        return manuHourKaiHe;
    }

    public void setManuHourKaiHe(int manuHourKaiHe) {
        this.manuHourKaiHe = manuHourKaiHe;
    }

    public int getManuHourFillet() {
        return manuHourFillet;
    }

    public void setManuHourFillet(int manuHourFillet) {
        this.manuHourFillet = manuHourFillet;
    }

    public int getManuHourPushFlat() {
        return manuHourPushFlat;
    }

    public void setManuHourPushFlat(int manuHourPushFlat) {
        this.manuHourPushFlat = manuHourPushFlat;
    }

    public int getM16LsNum() {
        return m16LsNum;
    }

    public void setM16LsNum(int m16LsNum) {
        this.m16LsNum = m16LsNum;
    }

    public int getM20LsNum() {
        return m20LsNum;
    }

    public void setM20LsNum(int m20LsNum) {
        this.m20LsNum = m20LsNum;
    }

    public int getM24LsNum() {
        return m24LsNum;
    }

    public void setM24LsNum(int m24LsNum) {
        this.m24LsNum = m24LsNum;
    }

    public int getMxLsNum() {
        return mxLsNum;
    }

    public void setMxLsNum(int mxLsNum) {
        this.mxLsNum = mxLsNum;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.projectId);
        dest.writeString(this.projectName);
        dest.writeInt(this.towerTypeId);
        dest.writeString(this.towerTypeName);
        dest.writeInt(this.id);
        dest.writeString(this.partNo);
        dest.writeInt(this.num);
        dest.writeString(this.segStr);
        dest.writeString(this.materialMark);
        dest.writeString(this.specification);
        dest.writeDouble(this.wide);
        dest.writeDouble(this.thick);
        dest.writeDouble(this.length);
        dest.writeDouble(this.wingAngle);
        dest.writeDouble(this.realWeight);
        dest.writeString(this.notes);
        dest.writeInt(this.manuHourWeld);
        dest.writeInt(this.manuHourZhiWan);
        dest.writeInt(this.manuHourCutAngle);
        dest.writeInt(this.manuHourCutBer);
        dest.writeInt(this.manuHourCutRoot);
        dest.writeInt(this.manuHourClashHole);
        dest.writeInt(this.manuHourBore);
        dest.writeInt(this.manuHourKaiHe);
        dest.writeInt(this.manuHourFillet);
        dest.writeInt(this.manuHourPushFlat);
        dest.writeInt(this.m16LsNum);
        dest.writeInt(this.m20LsNum);
        dest.writeInt(this.m24LsNum);
        dest.writeInt(this.mxLsNum);
        dest.writeInt(this.fileCount);
        dest.writeParcelable(this.partFile, flags);
    }

    protected TowerPart(Parcel in) {
        this.projectId = in.readInt();
        this.projectName = in.readString();
        this.towerTypeId = in.readInt();
        this.towerTypeName = in.readString();
        this.id = in.readInt();
        this.partNo = in.readString();
        this.num = in.readInt();
        this.segStr = in.readString();
        this.materialMark = in.readString();
        this.specification = in.readString();
        this.wide = in.readDouble();
        this.thick = in.readDouble();
        this.length = in.readDouble();
        this.wingAngle = in.readDouble();
        this.realWeight = in.readDouble();
        this.notes = in.readString();
        this.manuHourWeld = in.readInt();
        this.manuHourZhiWan = in.readInt();
        this.manuHourCutAngle = in.readInt();
        this.manuHourCutBer = in.readInt();
        this.manuHourCutRoot = in.readInt();
        this.manuHourClashHole = in.readInt();
        this.manuHourBore = in.readInt();
        this.manuHourKaiHe = in.readInt();
        this.manuHourFillet = in.readInt();
        this.manuHourPushFlat = in.readInt();
        this.m16LsNum = in.readInt();
        this.m20LsNum = in.readInt();
        this.m24LsNum = in.readInt();
        this.mxLsNum = in.readInt();
        this.fileCount = in.readInt();
        this.partFile = in.readParcelable(PartFile.class.getClassLoader());
    }

    public static final Creator<TowerPart> CREATOR = new Creator<TowerPart>() {
        @Override
        public TowerPart createFromParcel(Parcel source) {
            return new TowerPart(source);
        }

        @Override
        public TowerPart[] newArray(int size) {
            return new TowerPart[size];
        }
    };
}
