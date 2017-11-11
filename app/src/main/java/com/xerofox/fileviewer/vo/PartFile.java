package com.xerofox.fileviewer.vo;

import android.os.Parcel;
import android.os.Parcelable;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class PartFile implements Parcelable {
    private int id;
    private String name;
    private String fileType;
    private int length;
    private byte[] bytes;

    public PartFile(ByteBufferReader br) {
        this.id = br.readInt();
        this.name = br.readString();
        this.fileType = br.readString();
        this.length = br.readInt();
        this.bytes = br.readBytes(length);
    }

    public void saveByteArray(ByteBufferWriter br) {
        br.write(this.id);
        br.write(this.name);
        br.write(this.fileType);
        br.write(this.length);
        br.write(this.bytes);
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

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public void save(String name, byte[] data) {
        File file = new File(name);
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "rw");
            ByteArrayOutputStream output = new ByteArrayOutputStream(data.length);
            output.write(data);
//            ByteBufferWriter writer = new ByteBufferWriter(output);
//            writer.write(data);
            byte[] bytes = output.toByteArray();
            raf.write(bytes);
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.fileType);
        dest.writeInt(this.length);
        dest.writeByteArray(this.bytes);
    }

    protected PartFile(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.fileType = in.readString();
        this.length = in.readInt();
        this.bytes = in.createByteArray();
    }

    public static final Parcelable.Creator<PartFile> CREATOR = new Parcelable.Creator<PartFile>() {
        @Override
        public PartFile createFromParcel(Parcel source) {
            return new PartFile(source);
        }

        @Override
        public PartFile[] newArray(int size) {
            return new PartFile[size];
        }
    };
}
