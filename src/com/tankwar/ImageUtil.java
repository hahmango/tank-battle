package com.tankwar;

import org.loon.framework.game.simple.utils.GraphicsUtils;

import java.awt.*;

/**
 * 封装预载入图片资源的类，用于预载入所有需要用到的图片资源，可以提升游戏运行过程中的流畅感。
 */
class ImageUtil {
    static final Image backgroundImg = GraphicsUtils.loadFileImage("image/background.jpg");
    static final Image tankUP = GraphicsUtils.loadFileImage("image/tankUp.png");
    static final Image tankDown = GraphicsUtils.loadFileImage("image/tankDown.png");
    static final Image tankLeft = GraphicsUtils.loadFileImage("image/tankLeft.png");
    static final Image tankRight = GraphicsUtils.loadFileImage("image/tankRight.png");
    static final Image pauseImg = GraphicsUtils.loadFileImage("image/gamePause.png");
    static final Image overImg = GraphicsUtils.loadFileImage("image/gameOver.png");
    static final Image winImg = GraphicsUtils.loadFileImage("image/gameWin.png");
    static final Image bulletImg = GraphicsUtils.loadFileImage("image/bullet.png");
    static final Image blowImg = GraphicsUtils.loadFileImage("image/blow.png");
    static final Image blockImg = GraphicsUtils.loadFileImage("image/block.gif");
}
