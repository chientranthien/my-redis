package com.chientt.test;

import com.chientt.myredis.Dict;

public class Test {
    public static void main(String[] args) {
        Dict<String, String> dict = new Dict();
        dict.add("a","a1");
        dict.add("a","a2");
        dict.replace("b","b1");
        dict.replace("b1","b1");
        dict.replace("b2","b1");
        dict.replace("b3","b1");
        dict.replace("b4","b1");
        dict.replace("b5","b1");
        dict.replace("b6","b1");
        dict.replace("b7","b1");
        dict.replace("b11","b1");
        dict.replace("b12","b1");
        dict.replace("b13","b1");
        dict.replace("b14","b1");
        dict.replace("b15","b1");
        dict.replace("b16","b1");
        dict.replace("b17","b1");
        dict.replace("b18","b1");
        dict.replace("b19","b1");
        dict.replace("b22","b1");
        dict.replace("b21","b1");
        dict.replace("b23","b1");
        dict.replace("b24","b1");
        dict.replace("b25","b25");
        System.out.println(dict.find("a").value);
        System.out.println(dict.find("b").value);
        System.out.println(dict.find("b25").value);
        System.out.println(dict.hashTables[0].size);
    }
}
