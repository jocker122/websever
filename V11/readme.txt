对响应进行重构.
上一个版本我们定义了一个Map保存所有的Content-Type的值在ClientHandler中.但是由于每次请求都会创建
一次ClientHandler,这导致每次Map也会被创建一次,由于Map内容是固定的(将来里面存放1000多种类型),因此
这个Map应当被重用.

实现:
1:在com.webserver.http包中新建一个类:HttpContext
  这个类将来用于存放所有和HTTP协议相关的固定不变会被重用的内容.
2:在HttpContext中定义上述的Map并且声明为静态的
3:ClientHandler在设置响应头时从这里获取即可.


另一个问题:在ClientHandler中,每当我们处理请求是将一个文件设置到正文上,都要在添加两个说明该正文
的响应头Content-Type和Content-Length.既然一个响应中有响应正文就一定会包含这两个响应头,那我们
完全可以将添加上述两个响应头的工作放在设置响应正文的方法中.这样每次设置正文就自动添加这两个响应头即可







