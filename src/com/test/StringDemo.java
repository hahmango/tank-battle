package com.test;

import java.util.Random;

public class StringDemo {
    public static void main(String[] args) {
        Thread Move = new Thread(){
            @Override
            public void run() {
                while(true){
                    System.out.println("thread");
//                            fighter.moveDown();
                }

            }
        };

        while(true){
            Move.run();
        }

    }
}

