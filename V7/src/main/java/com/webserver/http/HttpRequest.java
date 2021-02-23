package com.webserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类的每一个实例用于表示客户端发送过来的一个HTTP请求内容
 * 每个请求由三部分构成:
 * 请求行，消息头，消息正文
 */
public class HttpRequest {
    //请求行相关信息
    private String method;//请求方式
    private String uri;//抽象路径
    private String protocol;//协议版本

    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();

    //消息正文相关信息

    private Socket socket;
    /**
     * HttpRequest的实例化过程就是解析请求的过程
     * @param socket
     */
    public HttpRequest(Socket socket){
        this.socket = socket;
        //1解析请求行
        parseRequestLine();
        //2解析消息头
        parseHeaders();
        //3解析消息正文
        parseContent();

    }
    //解析一个请求的三步骤:
    //1:解析请求行
    private void parseRequestLine(){
        System.out.println("HttpRequest:开始解析请求行...");
        try {
            String line = readLine();
            System.out.println("请求行:"+line);
            //http://localhost:8088/index.html
            //将请求行按照空格拆分为三部分，并分别赋值给上述变量
            String[] data = line.split("\\s");
            method = data[0];
            /*
                下面的代码可能在运行后浏览器发送请求拆分时，在这里赋值给uri时出现
                字符串下标越界异常，这是由于浏览器发送了空请求，原因与常见错误5一样。
             */
            uri = data[1];
            protocol = data[2];
            System.out.println("method:"+method);//method:GET
            System.out.println("uri:"+uri);//uri:/index.html
            System.out.println("protocol:"+protocol);//protocol:HTTP/1.1
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求行解析完毕!");
    }
    //2:解析消息头
    private void parseHeaders(){
        System.out.println("HttpRequest:开始解析消息头...");
        try {
            //下面读取每一个消息头后，将消息头的名字作为key，消息头的值作为value保存到headers中
            while(true) {
                String line = readLine();
                //读取消息头时，如果只读取到了回车加换行符就应当停止读取
                if(line.isEmpty()){//readLine单独读取CRLF返回值应当是空字符串
                    break;
                }
                System.out.println("消息头:" + line);
                //将消息头按照冒号空格拆分并存入到headers这个Map中保存
                String[] data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("HttpRequest:消息头解析完毕!");
    }
    //3:解析消息正文
    private void parseContent(){
        System.out.println("HttpRequest:开始解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");
    }


    private String readLine() throws IOException {
        /*
            当socket对象相同时，无论调用多少次getInputStream方法，获取回来的输入流
            总是同一个流。输出流也是一样的。
         */
        InputStream in = socket.getInputStream();
        int d;
        char cur=' ';//表示本次读取到的字符
        char pre=' ';//表示上次读取到的字符
        StringBuilder builder = new StringBuilder();//保存读取到的所有字符
        while ((d = in.read())!=-1){
            cur = (char)d;//本次读取到的字符
            //如果上次读取的是回车符，本次读取的是换行符则停止读取
            if(pre==13 && cur==10){
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * 根据消息头的名字获取对应消息头的值
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }
}







