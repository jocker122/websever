package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
            InputStream in = socket.getInputStream();
            //测试读取客户端发送过来的请求内容
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
            String line = builder.toString().trim();
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
}
