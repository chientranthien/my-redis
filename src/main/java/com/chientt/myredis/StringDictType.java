package com.chientt.myredis;

public class StringDictType<String, T> implements DictType<String, T> {

    public int hashFunction(String key) {
        return key != null ? key.hashCode() : 0;
    }
}
