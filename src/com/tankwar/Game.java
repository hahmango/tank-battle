package com.tankwar;

import com.Client;
import org.loon.framework.game.simple.GameScene;
import org.loon.framework.game.simple.core.graphics.Deploy;
import org.loon.framework.game.simple.core.graphics.Screen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.util.*;


/**
 * 程序的主要框架和主类，进行程序的所有功能的调用和一些draw方法的具体实现。
 */
public class Game extends Screen {
    // 游戏运行状态
    private static final int GAME_WAITING = 1;
    private static final int GAME_RUNNING = 2;
    private static final int GAME_OVER = 0;
    private static int gameStatus;
    private static Image overImage = ImageUtil.overImg;
    // 游戏得分数据
    private static int score = 0;
    private static int highScore = 0;
    // 坦克列表
    private static List<Tank> tanks = new ArrayList<>();
    // 存活的坦克的id
    private static Set<Integer> aliveId = new HashSet<>();
    // 子弹对象列表
    private static List<Bullet> bullets = new ArrayList<>();
    // 我方坦克
    private static Tank myTank = null;

    /*
    int
    0000 0000, 0000 0000, 0000 0000, 0000 0000
    |< tank>|  #### |<    x    >||<    y    >|

     */
    private static final int DATA_MASK = 0x0000ffff;
    private static final int NEW_CONNECT = 0x00100000;
    private static final int SET_TANK_ID = 0x00200000;
    private static final int KEY_EVENT = 0x00400000;
    private static final int MOUSE_EVENT = 0x00800000;
    private static final int TANK1 = 0x80000000;
    private static final int TANK2 = 0x40000000;
    private static final int TANK3 = 0x20000000;
    private static final int TANK4 = 0x10000000;
    private static final int TANK5 = 0x08000000;
    private static final int TANK6 = 0x04000000;
    private static final int TANK7 = 0x02000000;
    private static final int TANK8 = 0x01000000;

    private static final int TANK_NUM_MAX = 8;
    private static final int tankFlagList[] = {
            TANK1, TANK2, TANK3, TANK4,
            TANK5, TANK6, TANK7, TANK8
    };
    private static Client client;
    private static boolean isFirstConnect = true;

    private static int localTankID; // 本机坦克
    private static boolean hasLocalTank = false;

    // 创建一个线程用于监听服务器发来的数据
    private static Thread readThread;


    /**
     * 程序的入口，最先被JVM调用的方法
     * 由一个线程调用
     * @param args 传入命令参数
     */
    public static void main(String[] args) {
        try {
            Game game = new Game();
            readThread.start();
            // 创建屏幕对象
            GameScene gs = new GameScene(
                    "坦克大战",
                    ImageUtil.backgroundImg.getWidth(null),
                    ImageUtil.backgroundImg.getHeight(null)
            );
            // 由屏幕取得画笔
            Deploy d = gs.getDeploy();
            d.setScreen(game);
            // 重复描绘
            d.mainLoop();
            // 设置窗口可见
            gs.showFrame();
            // 设置游戏图标
            gs.setIconImage("image/icon.png");
        } catch (ConcurrentModificationException ignored) {
        }
    }


    /**
     * 初始化游戏框架
     */
    private Game() {
        super();
        // 设置图片为背景
        setBackground(ImageUtil.backgroundImg);
        // 设置为等待状态
        try {
            client = new Client("127.0.0.1", 8081);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setGameWaiting();
        // 创建一个线程用于监听服务器发来的数据
        readThread = new Thread(() -> {
            int data;
            while (true) {
                try {
                    data = client.recvFromServer();
                    processData(data);
                } catch (SocketException e) {
                    System.exit(1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        playSound("music/超级马里奥主题.mp3");
    }


    /**
     * 框架绘图函数
     *
     * @param graphics2D 框架封装绘图对象
     */
    @Override
    public void draw(Graphics2D graphics2D) {
        try {
            // 画背景
            graphics2D.drawImage(ImageUtil.backgroundImg, 0, 0, null);
            // 画边界
            drawBlock(graphics2D);
            // 画分数
            drawInfo(graphics2D);
            // 画坦克
            drawTanks(graphics2D);
            // 画子弹
            drawBullet(graphics2D);

            if (gameStatus != GAME_RUNNING) {
                drawWhenNotRunning(graphics2D);
            }
        } catch (ConcurrentModificationException ignored) { }
    }

    /**
     * 按下按键监听
     * WASD、上下左右控制方向
     * 按键J控制，按键K加速
     *
     * @param keyEvent 键盘按键事件
     */
    @Override
    public void onKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        // 死亡时不要发送按键
        if (myTank.isAlive()) {
            // 发送键值
            try {
                client.sendToServer(keyCode | tankFlagList[localTankID]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // TODO: onkeywithid放入，把set坐标改成发送数据，处理数据那里直接set坐标，区分鼠标和键盘，计算x y，不创建己方子弹列表，直接遍历所有子弹来区分
        try {
            // 运行状态时
            if (gameStatus == GAME_RUNNING) {
//            Tank thisTank = tanks.get(id);
                // 坦克上移
                if (keyCode == 'w' || keyCode == 'W' || keyCode == 38) {
//                thisTank.setDirection(Tank.DIRECTION_UP);
//                thisTank.move();
                    client.sendToServer(keyCode | tankFlagList[localTankID] | KEY_EVENT);
                }
                // 坦克下移
                if (keyCode == 's' || keyCode == 'S' || keyCode == 40) {
                    client.sendToServer(keyCode | tankFlagList[localTankID] | KEY_EVENT);
                }
                // 坦克左移
                if (keyCode == 'a' || keyCode == 'A' || keyCode == 37) {
                    client.sendToServer(keyCode | tankFlagList[localTankID] | KEY_EVENT);
                }
                // 坦克右移
                if (keyCode == 'd' || keyCode == 'D' || keyCode == 39) {
                    client.sendToServer(keyCode | tankFlagList[localTankID] | KEY_EVENT);
                }
//            if (keyCode == 'j' || keyCode == 'J') {
//                thisTank.fire(bullets);
//            }
//            // 火力切换
//            if (keyCode == 'k' || keyCode == 'K') {
//                thisTank.switchLevel();
//            }
                // 约定鼠标单击发送编码250
//                if (keyCode == 250) {
//                    thisTank.fire(bullets);
//                }
            }
            // 等待状态时空格开始
            else if (gameStatus == GAME_WAITING) {
                if (keyCode == ' ') {
                    // TODO:发送开始编码
                    client.sendToServer(keyCode | tankFlagList[localTankID] | KEY_EVENT);
                    gameStatus = GAME_RUNNING;
                }
            }
            // 结束状态时空格继续
            else if (gameStatus == GAME_OVER) {
                if (keyCode == ' ') {
                    // TODO:发送等待编码
                    client.sendToServer(keyCode | tankFlagList[localTankID] | KEY_EVENT);
                    setGameWaiting();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 用于处理从服务器传过来的一个int数据
     *
     * @param data 数据
     */
    private void processData(int data) {

        if ((data & NEW_CONNECT) != 0) {
            aliveId.add(tanks.size());
            tanks.add(generateTank(tanks.size()));
            System.out.println("There is a new client online, create new tank for it");
            if (!hasLocalTank && localTankID<tanks.size()) {
                hasLocalTank = true;
                myTank = tanks.get(localTankID);
            }
        }
        else if ((data & SET_TANK_ID) != 0) {

            // 第一次上线，先创建其他客户端已有的坦克，然后设置自己的坦克ID
            localTankID = data & DATA_MASK;
            if (isFirstConnect) {
                isFirstConnect = false;
                for (int i=0; i<localTankID; i++) {
                    aliveId.add(i);
                    tanks.add(generateTank(i));
                }
            }
            System.out.println("I am the new client, my tank ID is "+localTankID);
        }
        else if ((data & QUIT) != 0) {
            int tankID = data & DATA_MASK;
            System.out.println("quit\n");
            for (Tank tank : tanks) {
                if (tank.getId() == tankID) {
                    tanks.remove(tank);
                    break;
                }
            }
        }
        else { // 解析数据
            int key = data & DATA_MASK;
            int tankID = 0;
            for (int i=0; i<=TANK_NUM_MAX; i++) {
                if ((data & tankFlagList[i]) != 0) {
                    tankID = i;
                    break;
                }
            }
            System.out.println("The number "+tankID+" client send key : "+key);
            // 根据键值移动
            if (aliveId.contains(tankID)) {
                onKeyWithId(tankID, key);
            }
            // 处理了IndexOutOfBoundsException
        }
    }

    /**
     * 对应坦克按键动作
     * @param id 坦克id
     * @param keyCode 按键键值
     */
    private void onKeyWithId(int id, int keyCode) {


    }

    @Override
    public void onKeyUp(KeyEvent keyEvent) {

    }

    /**
     * 鼠标左键监听
     *
     * @param mouseEvent 鼠标左键事件
     */
    @Override
    public void leftClick(MouseEvent mouseEvent) {
        if (gameStatus == GAME_RUNNING) {
            // 发送单击编码250
            try {
                client.sendToServer(250 | tankFlagList[localTankID]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 鼠标中键监听
     *
     * @param mouseEvent 鼠标中键事件
     */
    @Override
    public void middleClick(MouseEvent mouseEvent) {

    }

    /**
     * 鼠标右键监听
     *
     * @param mouseEvent 鼠标右键事件
     */
    @Override
    public void rightClick(MouseEvent mouseEvent) {

    }

    /**
     * 描绘游戏等待和游戏结束的界面
     *
     * @param graphics2D 封装的绘制类对象
     */
    private void drawWhenNotRunning(Graphics2D graphics2D) {
        if(gameStatus == GAME_OVER){
            graphics2D.drawImage(
                    overImage,
                    (ImageUtil.backgroundImg.getWidth(null) - overImage.getWidth(null)) / 2,
                    (ImageUtil.backgroundImg.getHeight(null) - overImage.getHeight(null)) / 2,
                    null
            );
        }
        else{
            graphics2D.drawImage(
                    ImageUtil.pauseImg,
                    (ImageUtil.backgroundImg.getWidth(null) - ImageUtil.pauseImg.getWidth(null))/2,
                    (ImageUtil.backgroundImg.getHeight(null) - ImageUtil.pauseImg.getHeight(null))/2,
                    null
            );
            graphics2D.setFont(new Font("宋体", Font.BOLD, 30));
            graphics2D.setColor(new Color(0, 0, 0));
            graphics2D.drawString("当前在线人数:" + tanks.size() + "人，请输入空格开始", 220, 400);
        }
    }

    /**
     * 绘制子弹
     *
     * @param graphics2D 封装的绘制类对象
     */
    private void drawBullet(Graphics2D graphics2D) {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            // 画子弹
            Bullet bullet = bulletIterator.next();
            graphics2D.drawImage(
                    bullet.getImg(),
                    bullet.getX(),
                    bullet.getY(),
                    bullet.getWidth(),
                    bullet.getHeight(),
                    null
            );
            // 判断子弹撞击事件
            checkShot(bullet);
            // 移动子弹
            bullet.move(bullet.direction);
            // 判断子弹是否出界并进行回收
            if (bullet.isOutOfScene()) {
                bulletIterator.remove();
            }
        }
    }


    /**
     * 判断子弹是否撞击敌机并产生相应消失和爆炸效果
     *
     * @param bullet 被判断的子弹
     */
    private void checkShot(Bullet bullet) {
        Iterator<Tank> tankIterator = tanks.iterator();

        while (tankIterator.hasNext() && tanks.size() > 0) {
            Tank tank = tankIterator.next();
            // 子弹仅对存活坦克进行伤害
            if (tank.getId() != bullet.getId() && tank.isAlive() && tank.isCracked(bullet)) {
                // 回收子弹
                bullets.remove(bullet);
                // 游戏计分
                if(bullet.getId() == myTank.getId()){
                    score++;
                }
                // 显示爆炸效果并回收死亡坦克
                tank.decreaseHP(1);
                if(!tank.isAlive()){
                    tank.setImg(ImageUtil.blowImg);

//                    Timer timer = new Timer();
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            try {
//                                tanks.remove(tank);
//                            } catch (ConcurrentModificationException ignored) {
//                            }
//                        }
//                    }, 100);
                    aliveId.remove(tank.getId());
                    tanks.remove(tank);
                    // 如果当前坦克死亡
                    if(tank.getId() == localTankID){
                        setGameOver();
                    }
                    else if(tanks.size() == 1){
                        try {
                            client.sendToServer(OVER);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        overImage = ImageUtil.winImg;
                        setGameOver();
                    }
                }
            }
        }
    }


    /**
     * 绘制坦克
     *
     * @param graphics2D 封装的绘制类对象
     */
    private void drawTanks(Graphics2D graphics2D) {
        // 遍历画坦克
        for (Tank tank:tanks) {
            if(tank.isAlive()){
                graphics2D.drawImage(tank.img, tank.x, tank.y, null);
            }
        }
    }


    /**
     * 绘制分数等信息
     *
     * @param graphics2D 封装的绘制类对象
     */
    private void drawInfo(Graphics2D graphics2D) {
        graphics2D.setFont(new Font("黑体", Font.BOLD, 20));
        graphics2D.setColor(new Color(78, 94, 255));
        graphics2D.drawString("Score:" + score, 820, 50);
        graphics2D.drawString("High:" + highScore, 820, 75);
        graphics2D.drawString("HP:" + (myTank != null ? myTank.getHp() : 3), 820, 100);
    }


    /**
     * 绘制地图边界
     * @param graphics2D 封装的绘制类对象
     */
    private void drawBlock(Graphics2D graphics2D) {
        Image background = ImageUtil.backgroundImg;
        Image block = ImageUtil.blockImg;
        // 绘制上下两排
        for(int xx = 0; xx < background.getWidth(null); xx+=block.getWidth(null)){
            graphics2D.drawImage(block, xx, 0, null);
            graphics2D.drawImage(block, xx, background.getHeight(null) - block.getHeight(null), null);
        }
        // 绘制左右两排
        for(int yy = block.getHeight(null); yy < background.getHeight(null); yy+=block.getHeight(null)){
            graphics2D.drawImage(block, 0, yy, null);
            graphics2D.drawImage(block,  background.getWidth(null) - block.getWidth(null), yy, null);
        }
    }


    /**
     * 生成坦克对象
     *
     * @param id 坦克id
     * @return Tank 新生成的坦克
     */
    private static Tank generateTank(int id) {
        TankPlace.Place tankPlace = TankPlace.placeList[id];
        int direction = tankPlace.direction;

        Image image = null;
        switch (direction) {
            case MovingObject.DIRECTION_UP:
                image = ImageUtil.tankUP;
                break;
            case MovingObject.DIRECTION_DOWN:
                image = ImageUtil.tankDown;
                break;
            case MovingObject.DIRECTION_LEFT:
                image = ImageUtil.tankLeft;
                break;
            case MovingObject.DIRECTION_RIGHT:
                image = ImageUtil.tankRight;
                break;
        }
        return new Tank(tankPlace.x, tankPlace.y, Tank.INIT_SPEED, direction, image, Tank.INIT_FIRE_LEVEL, Tank.INIT_HP, id);
    }


    /**
     * 设置游戏等待
     */
    private void setGameWaiting() {
        // 设置初始状态为等待中
        gameStatus = GAME_WAITING;
        try {
            client.sendToServer(NEW_CONNECT); // 告诉其他客户端有新的连接
        } catch (IOException e) {
            e.printStackTrace();
        }
        score = 0;
        overImage = ImageUtil.overImg;
    }


    /**
     * 设置游戏停止
     */
    private void setGameOver() {
        gameStatus = GAME_OVER;
        // 清空子弹和敌机列表
        bullets.clear();
        tanks.clear();
        // 更新最高得分
        highScore = Math.max(score, highScore);
        // 初始化
        hasLocalTank = false;
    }

}
