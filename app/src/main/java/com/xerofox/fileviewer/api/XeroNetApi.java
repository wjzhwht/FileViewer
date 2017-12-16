package com.xerofox.fileviewer.api;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.Logger;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;
import com.xerofox.fileviewer.util.XmlUtil;

public class XeroNetApi {
    public static String IP = "192.168.2.6";
    public static final String NAMESPACE = "http://xerofox.com/TMSService/";
    public static String URL = "http://"+IP+"/TMSServ/TMSService.asmx";
    public static HttpTransportSE ht = null;
    public static int sessionId = 0;
    ////////////////////////////////////
    // service的调用函数
    ////////////////////////////////////
    // 从服务器下载任务列表
    private ArrayList<Task> downloadSimpleTaskListFromServer(int[] localTaskIdArr,boolean queryState){
        GetSessionId();
        ArrayList<Task> taskList = new ArrayList<Task>();
        try
        {
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "QueryObjects");
            //设置参数
            rpc.addProperty("sessionId",sessionId);
            rpc.addProperty("clsName","ManuElemTask");
            XmlUtil xmlHelper = new XmlUtil();
            xmlHelper.AppendValue("IdArr","id",localTaskIdArr);
            if(queryState)  //查询指定任务状态
                xmlHelper.AppendValue("QueryType",1);
            rpc.addProperty("xmlScope",xmlHelper.ToXml());
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(ht==null)
                ht = new HttpTransportSE(URL,100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return taskList;
            }
            String retString = String.valueOf(ret);
            byte[] retByteArr = Base64.decode(retString);
            //解析byte[]
            ByteBufferReader br = new ByteBufferReader(retByteArr);
            int taskArrLen = br.readInt();
            Logger.d("!!!!!!!!!!!!!!!!!!!!!!!! new task arr len:"+String.valueOf(taskArrLen));
            for(int i=0;i<taskArrLen;i++)
            {
                int id=br.readInt();
                String name=br.readString();
                Task task = new Task(id,name);
                task.setDate(br.readDate());
                if(queryState)
                    task.setNeedUpdate(br.readInt()>0);
                else
                    task.setState(br.readInt());
                taskList.add(task);
                Logger.d("initTask Time:" + String.valueOf(System.currentTimeMillis()-startTime));
                startTime = System.currentTimeMillis();
            }
            //Logger.d("parse Time:" + String.valueOf(System.currentTimeMillis()-startTime));
            return taskList;
        } catch (SocketTimeoutException timeoutException) {
           // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return taskList;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return taskList;
        }
    }

    private static int loginUser(String userName,String password){
        try{
            sessionId = 0;
            //
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "loginUser");
            //设置参数
            rpc.addProperty("userName",userName);
            rpc.addProperty("password",password);
            rpc.addProperty("fingerprint",null);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(ht==null)
                ht = new HttpTransportSE(URL,100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return 0;
            }
            sessionId = Integer.valueOf(String.valueOf(ret));
            return sessionId;
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return sessionId;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return sessionId;
        }
    }
    public static int GetSessionId(){
        if(sessionId>0)
            return sessionId;
        else
            return loginUser("wjh","");
    }

    private byte[] DownloadTaskById(int taskId){
        return XeroFtpApi.DownloadServerObject(taskId,"ManuElemTask");
    }

    public ArrayList<Task> DownloadTaskArr(){
        //1.获取本地任务id数组
        int[] localTaskIdArr = null;
        //2.查询服务器新任务idArr
        ArrayList<Task> taskList = downloadSimpleTaskListFromServer(localTaskIdArr,false);
        for(int i=0;i<taskList.size();i++){
            Task task = taskList.get(i);
            byte[] byteArr = DownloadTaskById(task.getId());
            if(byteArr==null)
                continue;
            ByteBufferReader br = new ByteBufferReader(byteArr);
            task = new Task(br);
        }
        //3.保存任务为本地文件
        return taskList;
    }
    // 查询任务更新状态
    public List<Task> QueryTaskUpdateState(int[] taskIdArr){
        return downloadSimpleTaskListFromServer(taskIdArr,false);
    }
    // 查询任务构件更新状态
    public boolean QueryTaskPartsUpdateState(Task task){
        if(task==null||task.getPartList().size()==0)
            return false;
        GetSessionId();
        try{
            Hashtable<Integer,TowerPart>  hashPartById = new Hashtable<Integer,TowerPart>();
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "QueryObjects");
            //设置参数
            rpc.addProperty("sessionId",sessionId);
            rpc.addProperty("clsName","ManuElemTaskPartId");
            XmlUtil xmlHelper = new XmlUtil();
            xmlHelper.AppendValue("ManuElemTaskId",task.getId());
            ArrayList<String> md5List = new ArrayList<String>();
            for(int i=0;i<task.getPartList().size();i++)
            {
                TowerPart part = task.getPartList().get(i);
                md5List.add(part.getMd5());
                hashPartById.put(part.getId(),part);
                part.setNeedUpdated(false); //初始化构件更新状态
            }
            xmlHelper.AppendValue("PartLabelArr","md5",(String[])md5List.toArray());
            rpc.addProperty("xmlScope",xmlHelper.ToXml());
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if(ht==null)
                ht = new HttpTransportSE(URL,100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis()-startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if(ret==null){
                //MessageBox.show(context, "没有需要下载的新任务!");
                return false;
            }
            String retString = String.valueOf(ret);
            byte[] retByteArr = Base64.decode(retString);
            //解析byte[]
            ByteBufferReader br = new ByteBufferReader(retByteArr);
            int taskArrLen = br.readInt();
            Logger.d("!!!!!!!!!!!!!!!!!!!!!!!! new task arr len:"+String.valueOf(taskArrLen));
            for(int i=0;i<taskArrLen;i++)
            {
                int id=br.readInt();
                TowerPart part = hashPartById.get(id);
                if(part!=null)
                    part.setNeedUpdated(true);
                Logger.d("initTask Time:" + String.valueOf(System.currentTimeMillis()-startTime));
                startTime = System.currentTimeMillis();
            }
            //Logger.d("parse Time:" + String.valueOf(System.currentTimeMillis()-startTime));
            return true;
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return false;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return false;
        }
    }
    // 根据构件id更新构件
    public TowerPart[] QueryTaskParts(int[] partIdArr){
        return null;
    }
}

