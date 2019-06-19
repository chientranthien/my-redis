package com.chientt.myredis.dict;

public class CommandTableDictType<K,V> implements DictType<String, V> {
    public int hashFunction(String key) {
        return key != null ? key.hashCode() : 0;
    }

    public boolean equal(String key1, String key2) {
        return true;
    }
}
