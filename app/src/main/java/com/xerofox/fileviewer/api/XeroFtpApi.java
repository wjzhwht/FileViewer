package com.xerofox.fileviewer.api;

import java.io.ByteArrayOutputStream;
import java.net.SocketTimeoutException;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.Logger;
import com.xerofox.fileviewer.util.XmlUtil;

public class XeroFtpApi {

    private static String OpenServerObjectDataProvider(int sessionId, int idObject, String cls_name, boolean compressed){
        try{
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "OpenServerObjectDataProvider");
            //设置参数
            rpc.addProperty("sessionId",sessionId);
            rpc.addProperty("idObject",idObject);
            rpc.addProperty("cls_name",cls_name);
            rpc.addProperty("compressed",compressed);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(XeroNetApi.ht==null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL,100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return null;
            }
            return String.valueOf(ret);
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return null;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return null;
        }
    }
    /*
    private static String OpenFileObjectDataProvider(int sessionId, int idObject, String cls_name, boolean compressed) {
        try{
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "OpenFileObjectDataProvider");
            //设置参数
            rpc.addProperty("sessionId",sessionId);
            rpc.addProperty("idObject",idObject);
            rpc.addProperty("cls_name",cls_name);
            rpc.addProperty("compressed",compressed);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(XeroNetApi.ht==null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL,100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return null;
            }
            String retString = String.valueOf(ret);
            return retString;
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return null;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return null;
        }
    }*/
    private static byte[] DownloadFileObject(int sessionId, int idFileObj, int startposition, int download_size, boolean compressed){
        try{
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "DownloadFileObject");
            //设置参数
            rpc.addProperty("sessionId",sessionId);
            rpc.addProperty("idObject",idFileObj);
            rpc.addProperty("startposition",startposition);
            rpc.addProperty("download_size",download_size);
            rpc.addProperty("compressed",compressed);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(XeroNetApi.ht==null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL,100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return null;
            }
            return Base64.decode(String.valueOf(ret));
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return null;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return null;
        }
    }
    private static boolean CloseFileObjectDataProvider(int sessionId,int fileObjId){
        try{
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "CloseFileObjectDataProvider");
            //设置参数
            rpc.addProperty("sessionId",sessionId);
            rpc.addProperty("fileObjId",fileObjId);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(XeroNetApi.ht==null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL,100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return false;
            }
            String retString = String.valueOf(ret);
            return Boolean.valueOf(retString);
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return false;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return false;
        }
    }

    public static byte[] DownloadServerObject(int objId,String cls_name) {
        int fileObjId = 0;
        int uiFileDataLength = 0;
        int sessionId = XeroNetApi.GetSessionId();
        try{
            String retXML = OpenServerObjectDataProvider(sessionId,objId,cls_name,false);
            if (retXML != null && retXML.length() > 0){
                XmlUtil xmlQuery = new XmlUtil(retXML);
                String idStr = xmlQuery.GetValue("ServerObjectDataProvider", "fileObjId");
                if(idStr!=null&&idStr.length()>0)
                    fileObjId = Integer.valueOf(idStr);
                String sizeStr = xmlQuery.GetValue("ServerObjectDataProvider", "size");
                if(sizeStr!=null&&sizeStr.length()>0)
                    uiFileDataLength = Integer.valueOf(sizeStr);
            }
            if (uiFileDataLength == 0 || fileObjId <= 0)
                return null;
            int indexpos=0;
            int uiLeastSize=uiFileDataLength;
            ByteBufferWriter bw = new ByteBufferWriter(new ByteArrayOutputStream(1000));
            while(uiLeastSize>0){
                int uiDownloadSize=Math.min(uiLeastSize,0x500000);
                byte[] byteArr = DownloadFileObject(sessionId,fileObjId, indexpos, uiDownloadSize, false);
                if(byteArr==null || byteArr.length<uiDownloadSize) {
                    break;    //下载失败
                }
                bw.write(byteArr);
                indexpos+=uiDownloadSize;
                uiLeastSize-=uiDownloadSize;
            }
            CloseFileObjectDataProvider(sessionId,fileObjId);
            if (uiLeastSize == 0)
                return bw.toByteArray();
            else
            {
                //AfxMessageBox("数据文件下载失败!");
                return null;
            }
        }catch (Exception ex){
            return null;
        }
    }
}

