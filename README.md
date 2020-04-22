# 联机坦克大战
这是一个可在线联机的坦克大战游戏。

文件分为服务器端和客户端也就是游戏两部分。

服务器端是用 c 写的，socket 编程，然后用多线程处理并发~~(毕竟多线程好写)~~，预先创建一个线程池，用多个线程来 accept 客户端。

客户端是用 java 写的。

## 运行
1. 首先要确保先运行服务器程序。
可以用终端定位到`TankBattle`文件夹中，然后在命令行输入
> `cc server.c && ./a.out 8080`

8080是端口号，运行服务器的时候一定要指定端口号，并且要保证客户端的端口号要和服务器的端口号一致。*（客户端的端口号默认是8080）*

**Note:** 服务器端只需要有一台电脑运行了就行。

2. 客户端直接用 IDE 运行`Game.java`即可。

**Note:** `Game.java`类里的第一行有一个`HOST`常量，表示 IP 地址，如果服务器端在本地运行，那`127.0.0.1`即可，否则需要改成运行服务器端的主机的 IP 地址。

**Note:** 建议使用 IntelliJ IDEA 来运行客户端，否则需要自己导入 jar 包。

## 操作
* `W, S, A, D`分别对应**上下左右**
* **鼠标点击**是发射子弹
* 等待状态时按**空格**即可开始游戏
* 游戏结束时只有胜利的玩家才能按**回车**结束这一盘，然后进入等待开始状态

## 截图

<div align=center><img src=https://tva1.sinaimg.cn/large/007S8ZIlgy1ge2ktycw7hj30pk0hk7wi.jpg height="50%" width="50%"></div>
<br>
<div align=center><img src=https://tva1.sinaimg.cn/large/007S8ZIlgy1ge2kyyrbgnj30kg0e2e81.jpg height="50%" width="50%"></div>
