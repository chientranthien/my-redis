package com.chientt.util;

import com.chientt.myredis.RedisObject;
import com.chientt.myredis.RedisServer;
import com.chientt.type.EncodingType;
import com.chientt.type.ObjectType;
import com.chientt.type.StorageType;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;

public class Util {
    public static RedisObject createObject(RedisServer server,ObjectType type, Object key) {
        RedisObject redisObject = new RedisObject();
        redisObject.objectType = type;
        redisObject.encodingType = EncodingType.raw;
        redisObject.ptr = key;
        redisObject.refCount = 1;
        redisObject.lru = server.lrulock;
        redisObject.storageType = StorageType.vmMemory;
        return redisObject;
    }
}
