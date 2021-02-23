package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *  负责与指定客户端进行HTTP交互
 *  HTTP协议要求与客户端的交互规则采取一问一答的方式。因此，处理客户端交互以3步形式完成:
 *  1:解析请求(一问)
 *  2:处理请求
 *  3:发送响应(一答)
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    public void run() {
        try{
            //1解析请求
            //读取请求行
            String line = readLine();
            System.out.println("请求行:"+line);
            String method;//请求方式
            String uri;//抽象路径
            String protocol;//协议版本

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

            //读取所有消息头
            Map<String,String> headers = new HashMap<>();
            //下面读取每一个消息头后，将消息头的名字作为key，消息头的值作为value保存到headers中
            while(true) {
                line = readLine();
                //读取消息头时，如果只读取到了回车加换行符就应当停止读取
                if(line.isEmpty()){//readLine单独读取CRLF返回值应当是空字符串
                    break;
                }
                System.out.println("消息头:" + line);
                //将消息头按照冒号空格拆分并存入到headers这个Map中保存
                data = line.split(":\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);

            //2处理请求

            //3发送响应

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //处理完毕后与客户端断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

}
