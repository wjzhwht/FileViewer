package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.os.Environment;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.ByteBufferWriter;
import com.xerofox.fileviewer.util.FileUtil;
import com.xerofox.fileviewer.util.Logger;
import com.xerofox.fileviewer.util.XmlUtil;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;
import com.xerofox.fileviewer.vo.TowerPart;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

public class XeroApiImpl implements XeroApi {
    private static String IP = "192.168.2.6";
    private static final String NAMESPACE = "http://xerofox.com/TMSService/";
    private static String URL = "http://" + IP + "/TMSServ/TMSService.asmx";
    private static HttpTransportSE ht = null;
    private static int sessionId = 0;

    @Inject
    public XeroApiImpl() {
    }

    @Override
    public LiveData<ApiResponse<List<Task>>> loadAllTasks() {
        // FIXME: 2017/11/3  mock server api
        return new LiveData<ApiResponse<List<Task>>>() {
            @Override
            protected void onActive() {
                super.onActive();
                try {
                    List<Task> tasks = new ArrayList<>();
                    String directory = Environment.getExternalStorageDirectory().getPath() + File.separator + FileHelper.PATH_DATA_ROOT;
                    List<File> files = FileUtil.listFilesInDir(directory);
                    for (File file : files) {
                        RandomAccessFile raf;
                        raf = new RandomAccessFile(file, "r");
                        byte[] byteArr = new byte[(int) raf.length()];
                        raf.read(byteArr);
                        raf.close();
                        Task task = new Task(new ByteBufferReader(byteArr));
                        tasks.add(task);
                    }
                    ApiResponse<List<Task>> response = new ApiResponse<>(200, tasks, null);
                    postValue(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public LiveData<Resource<List<Task>>> getServerTasks(AppExecutors appExecutors, int[] localTaskIds) {
        return new LiveData<Resource<List<Task>>>() {
            @Override
            protected void onActive() {
                super.onActive();
                appExecutors.networkIO().execute(() -> {
                    Resource<List<Task>> response = downloadSimpleTaskListFromServer(localTaskIds, false);
                    postValue(response);
                });
            }
        };
    }

    private Resource<List<Task>> downloadSimpleTaskListFromServer(int[] localTaskIdArr, boolean queryState) {
        GetSessionId();
        List<Task> taskList = new ArrayList<>();
        try {
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "QueryObjects");
            //设置参数
            rpc.addProperty("sessionId", sessionId);
            rpc.addProperty("clsName", "ManuElemTask");
            XmlUtil xmlHelper = new XmlUtil();
            xmlHelper.AppendValue("IdArr", "id", localTaskIdArr);
            if (queryState)  //查询指定任务状态
                xmlHelper.AppendValue("QueryType", 1);
            rpc.addProperty("xmlScope", xmlHelper.ToXml());
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (ht == null)
                ht = new HttpTransportSE(URL, 100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
                return Resource.success(taskList);
            }
            String retString = String.valueOf(ret);
            byte[] retByteArr = Base64.decode(retString);
            //解析byte[]
            ByteBufferReader br = new ByteBufferReader(retByteArr);
            int taskArrLen = br.readInt();
            Logger.d("!!!!!!!!!!!!!!!!!!!!!!!! new task arr len:" + String.valueOf(taskArrLen));
            for (int i = 0; i < taskArrLen; i++) {
                int id = br.readInt();
                String name = br.readString();
                Task task = new Task(id, name);
                task.setDate(br.readDate());
                if (queryState) {
                    task.setNeedUpdate(br.readInt() > 0);
                } else {
                    task.setState(br.readInt());
                }
                taskList.add(task);
                Logger.d("initTask Time:" + String.valueOf(System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();
            }
            return Resource.success(taskList);
        } catch (SocketTimeoutException timeoutException) {
            timeoutException.printStackTrace();
            return Resource.error("服务器连接超时", taskList);
        } catch (Exception e) {
            e.printStackTrace();
            return Resource.error("服务器地址错误", taskList);
        }
    }

    private static int GetSessionId() {
        if (sessionId > 0) {
            return sessionId;
        } else {
            return loginUser("wjh", "");
        }
    }

    private static int loginUser(String userName, String password) {
        try {
            sessionId = 0;
            //
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "loginUser");
            //设置参数
            rpc.addProperty("userName", userName);
            rpc.addProperty("password", password);
            rpc.addProperty("fingerprint", null);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (ht == null)
                ht = new HttpTransportSE(URL, 100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
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

    @Override
    public LiveData<Resource<Boolean>> downloadTasks(AppExecutors appExecutors, FileHelper fileHelper, List<Task> data) {
        return new LiveData<Resource<Boolean>>() {
            @Override
            protected void onActive() {
                super.onActive();
                postValue(Resource.loading(true));
                appExecutors.networkIO().execute(() -> {
                    List<Task> downloadedTask = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        Task task = data.get(i);
                        byte[] byteArr = downloadServerObject(task.getId(), "ManuElemTask");
                        if (byteArr == null)
                            continue;
                        ByteBufferReader br = new ByteBufferReader(byteArr);
                        task = new Task(br);
                        downloadedTask.add(task);
                    }
                    appExecutors.diskIO().execute(() -> {
                        fileHelper.saveTasks(downloadedTask);
                        postValue(Resource.success(true));
                    });
                });
            }
        };
    }

    private byte[] downloadServerObject(int objId, String cls_name) {
        int fileObjId = 0;
        int uiFileDataLength = 0;
        int sessionId = GetSessionId();
        try {
            String retXML = openServerObjectDataProvider(sessionId, objId, cls_name, false);
            if (retXML != null && retXML.length() > 0) {
                XmlUtil xmlQuery = new XmlUtil(retXML);
                String idStr = xmlQuery.GetValue("fileObjId");
                if (idStr != null && idStr.length() > 0)
                    fileObjId = Integer.valueOf(idStr);
                String sizeStr = xmlQuery.GetValue("size");
                if (sizeStr != null && sizeStr.length() > 0)
                    uiFileDataLength = Integer.valueOf(sizeStr);
            }
            if (uiFileDataLength == 0 || fileObjId <= 0)
                return null;
            int indexpos = 0;
            int uiLeastSize = uiFileDataLength;
            ByteBufferWriter bw = new ByteBufferWriter(new ByteArrayOutputStream(1000));
            while (uiLeastSize > 0) {
                int uiDownloadSize = Math.min(uiLeastSize, 0x500000);
                byte[] byteArr = downloadFileObject(sessionId, fileObjId, indexpos, uiDownloadSize, false);
                if (byteArr == null || byteArr.length < uiDownloadSize) {
                    break;    //下载失败
                }
                bw.write(byteArr);
                indexpos += uiDownloadSize;
                uiLeastSize -= uiDownloadSize;
            }
            closeFileObjectDataProvider(sessionId, fileObjId);
            if (uiLeastSize == 0)
                return bw.toByteArray();
            else {
                //AfxMessageBox("数据文件下载失败!");
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] downloadFileObject(int sessionId, int idFileObj, int startposition, int download_size, boolean compressed) {
        try {
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "DownloadFileObject");
            //设置参数
            rpc.addProperty("sessionId", sessionId);
            rpc.addProperty("idFileObj", idFileObj);
            rpc.addProperty("startposition", startposition);
            rpc.addProperty("download_size", download_size);
            rpc.addProperty("compressed", compressed);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (XeroNetApi.ht == null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL, 100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
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


    private String openServerObjectDataProvider(int sessionId, int idObject, String cls_name, boolean compressed) {
        try {
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "OpenServerObjectDataProvider");
            //设置参数
            rpc.addProperty("sessionId", sessionId);
            rpc.addProperty("idObject", idObject);
            rpc.addProperty("cls_name", cls_name);
            rpc.addProperty("compressed", compressed);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (XeroNetApi.ht == null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL, 100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
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

    private boolean closeFileObjectDataProvider(int sessionId, int fileObjId) {
        try {
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(XeroNetApi.NAMESPACE, "CloseFileObjectDataProvider");
            //设置参数
            rpc.addProperty("sessionId", sessionId);
            rpc.addProperty("idFileObj", fileObjId);
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (XeroNetApi.ht == null)
                XeroNetApi.ht = new HttpTransportSE(XeroNetApi.URL, 100000);
            //发送请求
            XeroNetApi.ht.call(null, envelope);
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
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

    public static boolean queryTaskPartsUpdateState(int id, List<TowerPart> parts) {
        if (parts == null || parts.isEmpty()) {
            return false;
        }
        GetSessionId();
        try {
            Hashtable<Integer, TowerPart> hashPartById = new Hashtable<>();
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "QueryObjects");
            //设置参数
            rpc.addProperty("sessionId", sessionId);
            rpc.addProperty("clsName", "ManuElemTaskPartId");
            XmlUtil xmlHelper = new XmlUtil();
            xmlHelper.AppendValue("ManuElemTaskId", id);
            for (int i = 0; i < parts.size(); i++) {
                TowerPart part = parts.get(i);
                hashPartById.put(part.getId(), part);
                part.setNeedUpdated(false); //初始化构件更新状态
            }
            xmlHelper.AppendValue("PartMd5Arr", parts);
            rpc.addProperty("xmlScope", xmlHelper.ToXml());
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (ht == null)
                ht = new HttpTransportSE(URL, 100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
                //MessageBox.show(context, "没有需要下载的新任务!");
                return false;
            }
            String retString = String.valueOf(ret);
            byte[] retByteArr = Base64.decode(retString);
            //解析byte[]
            ByteBufferReader br = new ByteBufferReader(retByteArr);
            int taskArrLen = br.readInt();
            Logger.d("!!!!!!!!!!!!!!!!!!!!!!!! new task arr len:" + String.valueOf(taskArrLen));
            for (int i = 0; i < taskArrLen; i++) {
                int partId = br.readInt();
                TowerPart part = hashPartById.get(partId);
                if (part != null)
                    part.setNeedUpdated(true);
                Logger.d("initTask Time:" + String.valueOf(System.currentTimeMillis() - startTime));
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

    public TowerPart[] queryTaskParts(int taskId, int[] partIdArr) {
        if (partIdArr == null || partIdArr.length == 0)
            return null;
        GetSessionId();
        try {
            long startTime = System.currentTimeMillis();
            Logger.d(String.valueOf(startTime));
            SoapObject rpc = new SoapObject(NAMESPACE, "QueryObjects");
            //设置参数
            rpc.addProperty("sessionId", sessionId);
            rpc.addProperty("clsName", "ManuElemTaskPart");
            XmlUtil xmlHelper = new XmlUtil();
            xmlHelper.AppendValue("ManuElemTaskId", taskId);
            xmlHelper.AppendValue("IdArr", "id", partIdArr);
            rpc.addProperty("xmlScope", xmlHelper.ToXml());
            //设置版本
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
            envelope.bodyOut = rpc;
            envelope.dotNet = true;
            //envelope.setOutputSoapObject(rpc);
            //传递byte[]时需要事先注册
            new MarshalBase64().register(envelope);
            //建立连接
            if (ht == null)
                ht = new HttpTransportSE(URL, 100000);
            //发送请求
            ht.call(null, envelope);
            Logger.d("download Time:" + String.valueOf(System.currentTimeMillis() - startTime));
            startTime = System.currentTimeMillis();
            //
            Object ret = envelope.getResponse();
            if (ret == null) {
                //MessageBox.show(context, "没有需要下载的新任务!");
                return null;
            }
            String retString = String.valueOf(ret);
            byte[] retByteArr = Base64.decode(retString);
            //解析byte[]
            ArrayList<TowerPart> partList = new ArrayList<>();
            ByteBufferReader br = new ByteBufferReader(retByteArr);
            int taskArrLen = br.readInt();
            Logger.d("!!!!!!!!!!!!!!!!!!!!!!!! new task arr len:" + String.valueOf(taskArrLen));
            for (int i = 0; i < taskArrLen; i++) {
                partList.add(new TowerPart(br));
                Logger.d("initTask Time:" + String.valueOf(System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();
            }
            //Logger.d("parse Time:" + String.valueOf(System.currentTimeMillis()-startTime));
            return (TowerPart[]) partList.toArray();
        } catch (SocketTimeoutException timeoutException) {
            // MessageBox.show(context, "服务器地址:"+URL+"!"+"连接服务器超时,请重试!");
            return null;
        } catch (Exception e) {
            //CrashHandler.saveExceptionInfo2File(e);
            //MessageBox.show(context, "服务器地址:"+URL+"!"+e.getMessage());
            return null;
        }
    }
}
