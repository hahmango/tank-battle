package com.tankwar;

import java.awt.*;

public class TankPlace {
    static class Place{
        int x, y, direction;

        public Place(int x, int y, int direction) {
            this.x = x;
            this.y = y;
            this.direction = direction;
        }
    }

    static Image block = ImageUtil.blockImg;
    static Image background = ImageUtil.backgroundImg;
    static Image tank = ImageUtil.tankLeft;

    // 初始位置分配数组
    public static final Place[] placeList = {
            new Place(block.getWidth(null), block.getWidth(null), MovingObject.DIRECTION_DOWN), // 左上
            new Place( // 右上
                    background.getWidth(null) - block.getWidth(null) - ImageUtil.tankDown.getWidth(null),
                    block.getHeight(null),
                    MovingObject.DIRECTION_DOWN
            ),
            new Place( // 左下
                    block.getWidth(null),
                    background.getHeight(null) - block.getWidth(null) - ImageUtil.tankUP.getWidth(null),
                    MovingObject.DIRECTION_UP
            ),
            new Place( // 右下
                    background.getWidth(null) - block.getWidth(null) - ImageUtil.tankUP.getWidth(null),
                    background.getHeight(null) - block.getWidth(null) - ImageUtil.tankUP.getWidth(null),
                    MovingObject.DIRECTION_UP
            ),
            new Place( // 上中
                    background.getWidth(null) / 2,
                    block.getWidth(null),
                    MovingObject.DIRECTION_RIGHT
            ),
            new Place( // 下中
                    background.getWidth(null) / 2,
                    background.getHeight(null) - block.getWidth(null) - tank.getWidth(null),
                    MovingObject.DIRECTION_UP
            ),
            new Place( // 左中
                    block.getWidth(null),
                    background.getHeight(null) / 2,
                    MovingObject.DIRECTION_RIGHT
            ),
            new Place( // 右中
                    background.getWidth(null) - block.getWidth(null) - tank.getWidth(null),
                    block.getWidth(null),
                    MovingObject.DIRECTION_LEFT
            )
    };
}