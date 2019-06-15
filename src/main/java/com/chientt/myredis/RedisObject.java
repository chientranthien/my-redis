package com.chientt.myredis;

import com.chientt.type.EncodingType;
import com.chientt.type.ObjectType;
import com.chientt.type.StorageType;

public class RedisObject {
    ObjectType objectType;
    StorageType storageType;
    EncodingType encodingType;
    long lru;
    int refCount;
    Dict.Entry entry;
}
