package com.xerofox.fileviewer.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class XmlUtil {
    private ArrayList<String> queryPropList = new ArrayList<String>();

    private Document document = null;
    private Hashtable<String,Element> hashNodeByName = new Hashtable<String,Element>();
    private Hashtable<String,String> hashPropByName = new Hashtable<String,String>();

    public String AppendValue(String valueName, Object value){
        String str = "<"+ valueName + " value=\" " + value.toString() + "\" />";
        queryPropList.add(str);
        return str;
    }
    public String AppendValue(String nodeName,String sonNdeName,int[] idArr){
        if(idArr==null)
            return "";
        String[] strArr = new String[idArr.length];
        for(int i=0;i<idArr.length;i++){
            strArr[i] = String.valueOf(idArr[i]);
        }
        return AppendValue(sonNdeName,strArr);
    }
    public String AppendValue(String nodeName,String sonNdeName,String[] valueArr){
        if(valueArr==null||valueArr.length<=0)
            return "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("     <"+nodeName+" count=\""+valueArr.length+"\">");
        for(int i=0;i<valueArr.length;i++)
            strBuilder.append("     <"+ sonNdeName+" count=\""+valueArr[i]+"\" />");
        strBuilder.append("     </"+ nodeName +">");
        String str = strBuilder.toString();
        queryPropList.add(str);
        return str;
    }
    public String ToXml(){
        if(queryPropList.size()<=0)
            return "";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        strBuilder.append(" <query>");
        for(int i=0;i<queryPropList.size();i++){
            strBuilder.append(queryPropList.get(i));
        }
        strBuilder.append(" </query>");
        return strBuilder.toString();
    }

    public XmlUtil(){
        document = null;
    }

    public XmlUtil(String xmlScope) throws DocumentException {
        document = DocumentHelper.parseText(xmlScope);
        Element element = document.getRootElement();
        listNodes(element);
    }
    //遍历当前节点下的所有节点
    public void listNodes(Element node){
        //首先获取当前节点的所有属性节点
        List<Attribute> list = node.attributes();
        for(Attribute attribute : list){
            if(hashPropByName.get(attribute.getName())!=null)
                hashPropByName.put(attribute.getName(),attribute.getStringValue());
        }
        //如果当前节点内容不为空，则输出
        if(!(node.getTextTrim().equals(""))){
            if(hashPropByName.get(node.getName())!=null)
                hashPropByName.put(node.getName(),node.getTextTrim());
            if(hashNodeByName.get(node.getName())!=null)
                hashNodeByName.put(node.getName(),node);
        }
        //同时迭代当前节点下面的所有子节点
        Iterator<Element> iterator = node.elementIterator();
        while(iterator.hasNext()){
            Element e = iterator.next();
            listNodes(e);
        }
    }
    public String GetValue(String nodeName,String propName) {
        Element node = hashNodeByName.get(nodeName);
        List<Attribute> list = node.attributes();
        for(Attribute attribute : list){
            if(attribute.getName().equals(propName)) {
                return attribute.getStringValue();
            }
        }
        return "";
    }
    public String GetValue(String propName){
        String value = hashPropByName.get(propName);
        if(value!=null)
            return value;
        else
            return "";
    }
}