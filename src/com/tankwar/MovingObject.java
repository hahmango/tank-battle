package com.tankwar;

import java.awt.*;

public class MovingObject {
    // 横纵坐标
    protected int x, y;
    // 移速
    protected int speed;
    // 移动方向
    protected int direction;
    protected static final int DIRECTION_UP = 1;
    protected static final int DIRECTION_DOWN = -1;
    protected static final int DIRECTION_LEFT = 2;
    protected static final int DIRECTION_RIGHT = -2;
    // 图片宽高
    protected int width;
    protected int height;
    // 图片文件
    protected Image img;


    /**
     * 构造方法
     * @param x x
     * @param y y
     * @param speed 速度
     * @param direction 方向
     * @param img 图片源
     */
    public MovingObject(int x, int y, int speed, int direction, Image img) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
        this.img = img;
    }


    /**
     * 判断两个坦克是否相撞
     * @param tank 与之比较的坦克
     * @return true如果两个坦克相撞，反之为false
     */
    protected boolean isCracked(MovingObject tank){
        return Util.isOverlap(
                tank.getX(),
                tank.getY(),
                tank.getX() + tank.getWidth(),
                tank.getY() + tank.getHeight(),
                x, y,
                x + img.getWidth(null),
                y + img.getHeight(null)
        );
    }


    /**
     * 根据当前状态进行移动
     */
    protected void move(int direction) {
        switch (direction) {
            case DIRECTION_UP:
                y -= speed;
                break;
            case DIRECTION_DOWN:
                y += speed;
                break;
            case DIRECTION_LEFT:
                x -= speed;
                break;
            case DIRECTION_RIGHT:
                x += speed;
                break;
        }
    }

    /**
     * 判断是否出界
     * @return true如果出界，反之为false
     */
    public boolean isOutOfScene() {
        int blockSize = ImageUtil.blockImg.getWidth(null);
        Image background = ImageUtil.backgroundImg;
        if(x < blockSize || y < blockSize){
            return true;
        }
        return x > background.getWidth(null) - blockSize - img.getWidth(null)
                || y > background.getHeight(null) - blockSize - img.getHeight(null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Image getImg() {
        return img;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setImg(Image img) {
        this.img = img;
    }
}