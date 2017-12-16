package com.xerofox.fileviewer.api;

import android.arch.lifecycle.LiveData;
import android.os.Environment;

import com.xerofox.fileviewer.AppExecutors;
import com.xerofox.fileviewer.util.ByteBufferReader;
import com.xerofox.fileviewer.util.FileUtil;
import com.xerofox.fileviewer.util.Logger;
import com.xerofox.fileviewer.util.XmlUtil;
import com.xerofox.fileviewer.vo.Resource;
import com.xerofox.fileviewer.vo.Task;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class XeroApiImpl implements XeroApi {
    public static String IP = "192.168.2.217";
    public static final String NAMESPACE = "http://xerofox.com/TMSService/";
    public static String URL = "http://" + IP + "/TMSServ/TMSService.asmx";
    public static HttpTransportSE ht = null;
    public static int sessionId = 0;

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
}
