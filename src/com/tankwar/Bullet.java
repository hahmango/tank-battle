package com.tankwar;

import java.awt.*;

 class Bullet extends MovingObject {
    static final int SPEED1 = 3;
    static final int SPEED2 = 3;
    // 子弹对应的坦克的id
    private int id;

    Bullet(int x, int y, int speed, int direction, Image img, int id) {
        super(x, y, speed, direction, img);
        this.id = id;
    }

    int getId() {
        return id;
    }
}
