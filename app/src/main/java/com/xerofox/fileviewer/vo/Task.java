package com.xerofox.fileviewer.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.Util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Task implements Parcelable {
    public static final String FILE_EXTENSION = ".tpp";

    public static final String DIVIER_STATE = "_";
    public static final String DIVIER_ID = "#";
    public static final String DIVIER_NAME = "%";

    public static final String STATE_STRING_NEW = "n";
    public static final String STATE_STRING_ACTIVE = "a";
    public static final String STATE_STRING_FINISH = "f";
    public static final String STATE_STRING_HIDE = "h";

    public static final int STATE_INT_NEW = 0;
    public static final int STATE_INT_ACTIVE = 1;
    public static final int STATE_INT_FINISH = 2;
    public static final int STATE_INT_HIDE = 3;

    private double version;
    private int id;
    private String name;
    private Date date;
    private int state;
    private boolean needUpdate;
    private String md5;
    private ArrayList<TowerPart> partList;
    private int count;

    public Task(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Task(ByteBufferReader br) {
        this.version = br.readDouble();
        this.id = br.readInt();
        this.name = br.readString();
        this.date = br.readDate();
        this.state = br.readInt();
        this.md5 = br.readString();
        this.count = br.readInt();
        partList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            partList.add(new TowerPart(br));
        }
    }

    public Task(ByteBufferReader br, boolean readBytes) {
        this.version = br.readDouble();
        this.id = br.readInt();
        this.name = br.readString();
        this.date = br.readDate();
        this.state = br.readInt();
        this.md5 = br.readString();
        this.count = br.readInt();
        partList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            partList.add(new TowerPart(br, readBytes));
        }
    }

    public Task(String name) {
        if (!TextUtils.isEmpty(name)
                && name.contains(DIVIER_STATE)
                && name.contains(DIVIER_ID)
                && name.contains(DIVIER_NAME)) {
            String[] split = name.split(DIVIER_STATE);
            setStateString(split[0]);
            split = split[1].split(DIVIER_ID);
            setId(Integer.parseInt(split[0]));
            split = split[1].split(DIVIER_NAME);
            setName(split[0]);
            setDate(Util.format2Date(split[1]));
        }
    }

    public String getTaskDirectoryName() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStateString());
        sb.append(DIVIER_STATE);
        sb.append(getId());
        sb.append(DIVIER_ID);
        sb.append(getName());
        sb.append(DIVIER_NAME);
        sb.append(Util.formatTimeStamp(date.getTime()));
        return sb.toString();
    }

    public String getTaskFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStateString());
        sb.append(DIVIER_STATE);
        sb.append(getId());
        sb.append(FILE_EXTENSION);
        return sb.toString();
    }

    public void saveByteArray(ByteBufferWriter br) {
        br.write(this.version);
        br.write(this.id);
        br.write(this.name);
        br.write(this.date);
        br.write(this.state);
        br.write(this.md5);
        br.write(this.count);
        if (partList != null && !partList.isEmpty()) {
            for (TowerPart part : partList) {
                part.saveByteArray(br);
            }
        }
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStateString(String state) {
        switch (state) {
            case STATE_STRING_NEW:
                setState(STATE_INT_NEW);
                break;
            case STATE_STRING_ACTIVE:
                setState(STATE_INT_ACTIVE);
                break;
            case STATE_STRING_FINISH:
                setState(STATE_INT_FINISH);
                break;
            case STATE_STRING_HIDE:
                setState(STATE_INT_HIDE);
                break;
            default:
                break;
        }
    }

    public String getStateString() {
        switch (state) {
            case STATE_INT_NEW:
                return STATE_STRING_NEW;
            case STATE_INT_ACTIVE:
                return STATE_STRING_ACTIVE;
            case STATE_INT_FINISH:
                return STATE_STRING_FINISH;
            case STATE_INT_HIDE:
                return STATE_STRING_HIDE;
            default:
                return "";
        }
    }

    public ArrayList<TowerPart> getPartList() {
        return partList;
    }

    public void setPartList(ArrayList<TowerPart> partList) {
        this.partList = partList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isNeedUpdate() { return needUpdate;}

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public void setNeedUpdate(boolean needUpdate) {this.needUpdate = needUpdate;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.version);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.state);
        dest.writeByte(this.needUpdate ? (byte) 1 : (byte) 0);
        dest.writeString(this.md5);
        dest.writeTypedList(this.partList);
        dest.writeInt(this.count);
    }

    protected Task(Parcel in) {
        this.version = in.readDouble();
        this.id = in.readInt();
        this.name = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.state = in.readInt();
        this.needUpdate = in.readByte() != 0;
        this.md5 = in.readString();
        this.partList = in.createTypedArrayList(TowerPart.CREATOR);
        this.count = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
