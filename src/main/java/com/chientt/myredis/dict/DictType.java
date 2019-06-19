package com.chientt.myredis.dict;

public interface DictType<K, V> {
    int hashFunction(K key);

    boolean equal(K key1, K key2);

}
