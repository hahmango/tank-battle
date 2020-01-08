package com.tankwar;

public class Util {
    /**
     * 判断两个矩形是否重叠
     * @param axU 第一个矩形的左上角x坐标
     * @param ayU 第一个矩形的左上角y坐标
     * @param axD 第一个矩形的右下角x坐标
     * @param ayD 第一个矩形的右下角y坐标
     * @param bxU 第二个矩形的左上角x坐标
     * @param byU 第二个矩形的左上角y坐标
     * @param bxD 第二个矩形的右下角x坐标
     * @param byD 第二个矩形的右下角y坐标
     * @return true如果两个飞行物相撞，反之false
     */
     static Boolean isOverlap(int axU, int ayU, int axD, int ayD, int bxU, int byU, int bxD, int byD){
         return ayD >= byU && ayU <= byD && axD >= bxU && axU <= bxD;
    }
}
