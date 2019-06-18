package com.chientt.myredis;

import com.chientt.type.EncodingType;
import com.chientt.type.ObjectType;
import com.chientt.type.StorageType;

public class RedisObject {
    public ObjectType objectType;
    public StorageType storageType;
    public EncodingType encodingType;
    public long lru;
    public int refCount;
    public Object ptr;
}
