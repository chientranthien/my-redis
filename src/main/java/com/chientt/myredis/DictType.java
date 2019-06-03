package com.chientt.myredis;

public interface DictType<K,V> {
    int hashFunction(K key);

}
