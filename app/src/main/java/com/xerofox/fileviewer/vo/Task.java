package com.xerofox.fileviewer.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;

import java.util.ArrayList;
import java.util.Date;

public class Task implements Parcelable {
    private double version;
    private int id;
    private String name;
    private Date date;
    private int state;
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
        this.count = br.readInt();
        partList = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            partList.add(new TowerPart(br, readBytes));
        }
    }

    public void saveByteArray(ByteBufferWriter br) {
        br.write(this.version);
        br.write(this.id);
        br.write(this.name);
        br.write(this.date);
        br.write(this.state);
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
        this.partList = in.createTypedArrayList(TowerPart.CREATOR);
        this.count = in.readInt();
    }

    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
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
