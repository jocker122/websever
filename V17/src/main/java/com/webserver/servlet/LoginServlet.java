package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        System.out.println("LoginServlet:开始处理登录...");
        //获取登录信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username==null||password==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            return;
        }

        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","r");
                ){
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);//移动到每条记录的开始位置(用户名位置)
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    //找到该用户
                    raf.read(data);//读取密码
                    String pwd = new String(data,"UTF-8").trim();
                    if(pwd.equals(password)){
                        //登录成功
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file);
                        return;
                    }
                    break;
                }
            }
            //统一处理登录失败
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);


        }catch(IOException e){
            e.printStackTrace();
        }




        System.out.println("LoginServlet:登录处理完毕!");
    }
}
