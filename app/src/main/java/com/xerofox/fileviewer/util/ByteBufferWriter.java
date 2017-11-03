package com.xerofox.fileviewer.util;

import java.util.*;
import java.io.*;

/**
 * <p>Title: </p>
 * <p>
 * <p>Description: </p>
 * <p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ByteBufferWriter {
    private ByteArrayOutputStream output = null;
    private byte[] buf1 = new byte[1];
    private byte[] buf2 = new byte[2];
    private byte[] buf4 = new byte[4];
    private byte[] buf8 = new byte[8];

    public ByteBufferWriter(ByteArrayOutputStream output) {
        this.output = output;
    }

    public byte[] toByteArray() {
        return output.toByteArray();
    }

    public void write(char v) {
        write((short) v);
    }

    public void write(short v) {
        buf2[1] = (byte) (v & 0xff);
        buf2[0] = (byte) (v >> 8 & 0xff);
        try {
            output.write(buf2);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void write(int v) {
        buf4[3] = (byte) (v & 0xff);
        buf4[2] = (byte) (v >> 8 & 0xff);
        buf4[1] = (byte) (v >> 16 & 0xff);
        buf4[0] = (byte) (v >> 24 & 0xff);
        try {
            output.write(buf4);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void write(long v) {
        for (int i = 0; i < 8; i++) {
            if (i > 0)
                buf8[7 - i] = (byte) (v >> (8 * i) & 0xff);
            else
                buf8[7 - i] = (byte) (v & 0xff);
        }
        try {
            output.write(buf8);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void write(float v) {
        write(Float.floatToRawIntBits(v));
    }

    public void write(double v) {
        write(Double.doubleToRawLongBits(v));
    }

    public void write(boolean v) {
        if (v) buf1[0] = 1;
        else buf1[0] = 0;
        try {
            output.write(buf1);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void write(byte b) {
        buf1[0] = b;
        try {
            output.write(buf1);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void write(byte[] byteArr) {
        try {
            if (byteArr != null)
                output.write(byteArr);
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public void write(String v) {
        try {
            if (v == null)
                write((int) -1);
            else {
                byte[] stringByteArr = v.getBytes("UTF-8");
                write(stringByteArr.length);
                output.write(stringByteArr);
            }
        } catch (IOException e) {
            System.err.print(e.toString());
        }
    }

    public void write(Date v) {
        if (v == null)
            write((short) -1);
        else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(v);
            write((short) calendar.get(Calendar.YEAR));
            //Java�·ݴ�0��ʼ,C#��1��ʼ��Ϊ��֤��C#һ�� �����·�ʱ��1����ȡʱ��1��
            write((byte) (calendar.get(Calendar.MONTH) + 1));
            write((byte) calendar.get(Calendar.DAY_OF_MONTH));
            write((byte) calendar.get(Calendar.HOUR_OF_DAY));
            write((byte) calendar.get(Calendar.MINUTE));
            write((byte) calendar.get(Calendar.SECOND));
        }
    }

    //��ȡ��ǰbyteArr�ĳ���
    public int getByteArrLen() {
        return output.size();
    }

    //���������
    public void reset() {
        output.reset();
    }

    public static byte[] ToUniversalBytes(int v) {
        byte[] byteArr = new byte[4];
        byteArr[3] = (byte) (v & 0xff);
        byteArr[2] = (byte) (v >> 8 & 0xff);
        byteArr[1] = (byte) (v >> 16 & 0xff);
        byteArr[0] = (byte) (v >> 24 & 0xff);
        return byteArr;
    }
}
