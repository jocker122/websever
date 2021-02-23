package com.webserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 小鸟WebServer
 * 实现Tomcat的基础功能的一个Web容器
 * Web容器的作用:
 * 1:Web容器是一个Web服务端程序,负责与客户端(通常是浏览器)进行交互
 * 2:完成与客户端的TCP连接及数据交互.
 * 3:基于HTTP协议与客户端进行应用交互,使得浏览器可以访问Web容器中部署的不同网络应用(Webapp)
 *   的页面,资源,功能等.
 * 4:可以管理部署多个不同的网络应用.
 */
public class WebServer {
    private ServerSocket serverSocket;
    public WebServer(){
        try {
            System.out.println("正在启动服务端...");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            /*
                http://localhost:8088
                http://localhost:8088/index.html
             */
            System.out.println("等待客户端连接...");
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端连接了!");
            //启动一个线程与该客户端交互
            ClientHandler handler = new ClientHandler(socket);
            Thread t = new Thread(handler);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
