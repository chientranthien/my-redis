package com.chientt.myredis.dict;

import com.chientt.myredis.dict.DictType;

public class StringDictType<K, V> implements DictType<String, V> {

    public int hashFunction(String key) {
        return key != null ? key.toLowerCase().hashCode() : 0;
    }

    public boolean equal(String key1, String key2) {
        return key1.equalsIgnoreCase(key2);
    }

}
