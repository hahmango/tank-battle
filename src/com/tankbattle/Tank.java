package com.tankbattle;

import java.awt.*;
import java.util.List;

/**
 * 战斗机类，定义了战斗机移动、速度和火力的控制和发射的动作
 */
class Tank extends MovingObject {
    // 坦克最大速度
    public static final int MAX_SPEED = 100;
    public static final int INIT_SPEED = 3;
    // 坦克默认加速度
    public static final int ACCELERATION = 10;
    // 坦克火力等级
    private int fireLevel;
    public static final int INIT_FIRE_LEVEL = 8;
    // 坦克剩余生命值
    private int hp;
    // 坦克的id
    private int id;
    public static final int INIT_HP = 3;

    public Tank(int x, int y, int speed, int direction, Image img, int fireLevel, int hp, int id) {
        super(x, y, speed, direction, img);
        this.fireLevel = fireLevel;
        this.hp = hp;
        this.id = id;
    }

    /**
     * 发射子弹方法，根据战斗机火力等级生成新子弹
     *
     * @param bulletList 新子弹要加入的列表
     */
    void fire(List<Bullet> bulletList) {
        int bulletX = 0, bulletY = 0;

        switch (direction) {
            case DIRECTION_UP:
                bulletX = x + width / 2 - ImageUtil.bulletImg.getWidth(null) / 2;
                bulletY = y - ImageUtil.bulletImg.getHeight(null);
                break;
            case DIRECTION_DOWN:
                bulletX = x + width / 2 - ImageUtil.bulletImg.getWidth(null) / 2;
                bulletY = y + height;
                break;
            case DIRECTION_LEFT:
                bulletX = x - ImageUtil.bulletImg.getWidth(null);
                bulletY = y + height / 2 - ImageUtil.bulletImg.getHeight(null) / 2;
                break;
            case DIRECTION_RIGHT:
                bulletX = x + width;
                bulletY = y + height / 2 - ImageUtil.bulletImg.getHeight(null) / 2;
        }
        bulletList.add(
                new Bullet(
                        bulletX,bulletY,
                        fireLevel == 0 ? Bullet.SPEED1 : Bullet.SPEED2,
                        direction,
                        ImageUtil.bulletImg,
                        id
                )
        );
    }


    /**
     * 切换战斗机的火力等级0-1
     */
    void switchLevel() {
        fireLevel = (fireLevel + 1) % 2;
    }

    /**
     * 得到战斗机当前生命值
     *
     * @return 当前战斗机生命值
     */
    int getHp() {
        return hp;
    }

    /**
     * 设置战斗机当前生命值
     *
     * @param hp 要设置的生命值
     */
    void setHp(int hp) {
        this.hp = hp;
    }

    void decreaseHP(int decre) {
        hp -= decre;
        if(hp < 0){
            hp = 0;
        }
    }

    boolean isAlive() {
        return hp > 0;
    }

    public int getFireLevel() {
        return fireLevel;
    }

    public void setFireLevel(int fireLevel) {
        this.fireLevel = fireLevel;
    }

    public int getId() {
        return id;
    }

    @Override
    public void setDirection(int direction) {
        if(direction != this.direction){
            super.setDirection(direction);
            switch (direction){
                case MovingObject.DIRECTION_UP:
                    setImg(ImageUtil.tankUP);
                    break;
                case MovingObject.DIRECTION_DOWN:
                    setImg(ImageUtil.tankDown);
                    break;
                case MovingObject.DIRECTION_LEFT:
                    setImg(ImageUtil.tankLeft);
                    break;
                case MovingObject.DIRECTION_RIGHT:
                    setImg(ImageUtil.tankRight);
                    break;
            }
        }
    }


    /**
     * 坦克的移动
     */
    protected void move() {
        super.move(direction);
        if(isOutOfScene()){
            super.move(-direction);
        }
    }
}
