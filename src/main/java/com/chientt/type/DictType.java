package com.chientt.type;

public interface DictType<K,V> {
    int hashFunction(K key);

}
