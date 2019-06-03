package com.chientt.myredis;

public class Main {
    public static void main(String[] args) {
        double a = 0.3;
        float b = 0.3f;
        System.out.println(a == b);
        System.out.println(b);
        for (int i = 0; i < 10; i++) {
            a += 0.1;
            System.out.println( a);
        }
    }
}
