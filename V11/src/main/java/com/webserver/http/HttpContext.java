package com.webserver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 当前类用于保存所有与HTTP协议相关的规定内容以便重用
 */
public class HttpContext {
    /**
     * 资源后缀名与响应头Content-Type值的对应关系
     * key:资源后缀名
     * value:Content-Type对应的值
     */
    private static Map<String,String> mimeMapping = new HashMap<>();

    static{
        initMimeMapping();
    }

    private static void initMimeMapping(){
        mimeMapping.put("html","text/html");
        mimeMapping.put("css","text/css");
        mimeMapping.put("js","application/javascript");
        mimeMapping.put("png","image/png");
        mimeMapping.put("gif","image/gif");
        mimeMapping.put("jpg","image/jpeg");
    }

    /**
     * 根据给定的资源后缀名获取到对应的Content-Type的值
     * @param ext
     * @return
     */
    public static String getMimeType(String ext){
        return mimeMapping.get(ext);
    }
}
