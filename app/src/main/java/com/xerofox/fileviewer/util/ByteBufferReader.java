package com.xerofox.fileviewer.util;
import java.io.*;
import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ByteBufferReader {
	private byte[] buf1=new byte[1];
	private byte[] buf2=new byte[2];
	private byte[] buf4=new byte[4];
	private byte[] buf8=new byte[8];
    private ByteArrayInputStream input = null;
    public ByteBufferReader(byte[] buffer) {
    	this.input = new ByteArrayInputStream(buffer);
    }

    public byte[] readBytes(int byteNum) {
		try{
		    byte[] buffer = new byte[byteNum];
		    input.read(buffer);
		    return buffer;
		} catch(IOException e) {
		    System.err.println(e.toString());
		    return null;
		}
    }
    
    public char readChar() {
    	short v = readShort();
    	return (char)v;
    }

    public short readShort() {
		try {
		    input.read(buf2);
		    short v = 0;
		    if (buf2[0] >= 0)
		    	v = buf2[0];
		    else
		    	v+= 256 + buf2[0];
		    v *= 256;
		    if (buf2[1] >= 0)
		    	v += buf2[1];
		    else
		    	v += 256+buf2[1];
		    return v;
		} catch (IOException e) {
		    System.err.println(e.toString());
		    return 0;
		}
    }

    public int readInt() {
		try {
		    input.read(buf4);
		    int v = 0;
		    if(buf4[0] >= 0)
		    	v = buf4[0];
		    else
		    	v+= 256+buf4[0];
		    for(int i=1;i<4;i++){
				v *= 256;
				if(buf4[i] >= 0)
				    v += buf4[i];
				else
				    v += buf4[i] + 256;
		    }
		    return v;
		} catch (IOException e) {
		    System.err.println(e.toString());
		    return 0;
		}
    }

    public long readLong() {
		try {
		    input.read(buf8);
		    long v = 0;
		    if(buf8[0] >= 0)
		    	v = buf8[0];
		    else
		    	v+= 256+buf8[0];
		    for(int i=1;i<8;i++){
		    	v *= 256;
				if(buf8[i] >= 0)
				    v += buf8[i];
				else
				    v += buf8[i] + 256;
		    }
		    return v;
		} catch (IOException e) {
		    System.err.println(e.toString());
		    return 0;
		}
    }

    public float readFloat() {
    	int v = readInt();
    	return Float.intBitsToFloat(v);
    }

    public double readDouble() {
    	long v = readLong();
    	return Double.longBitsToDouble(v);
    }

    public boolean readBoolean() {
		try {
		    input.read(buf1);
		    if(buf1[0] != 0)
			return true;
		    else
			return false;
		} catch (IOException e) {
		    System.err.println(e.toString());
		    return false;
		}
    }

    public byte readByte() {
		try {
		    input.read(buf1);
		    return buf1[0];
		} catch (IOException e) {
		    System.err.print(e.toString());
		    return 0;
		}
    }
    public String readString() {
		try {
		    int strLen = readInt();
		    if(strLen == -1)
		    	return null;
		    else {
				byte[] stringByteArr = new byte[strLen];
				input.read(stringByteArr);
				return new String(stringByteArr,"UTF-8");
		    }
		} catch (IOException e) {
		    System.err.print(e.toString());
		    return null;
		}
    }
    public Date readDate() {
		int year,month,day,hour,minute,second;
		year = readShort();
		if(year>=0) {
		    month = readByte();
		    day = readByte();
		    hour = readByte();
		    minute = readByte();
		    second = readByte();
		    Calendar calendar = Calendar.getInstance();
		    calendar.set(Calendar.YEAR, year);
		    //Java�·ݴ�0��ʼ,C#��1��ʼ��Ϊ��֤��C#һ�� �����·�ʱ��1����ȡʱ��1��
		    calendar.set(Calendar.MONTH, month-1);
		    calendar.set(Calendar.DAY_OF_MONTH, day);
		    calendar.set(Calendar.HOUR_OF_DAY, hour);
		    calendar.set(Calendar.MINUTE, minute);
		    calendar.set(Calendar.SECOND, second);
		    calendar.set(Calendar.MILLISECOND, 0);
		    return calendar.getTime();
		} else
		    return null;
    }
}
