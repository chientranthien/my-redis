package com.chientt.util;

import com.chientt.myredis.RedisObject;
import com.chientt.myredis.RedisServer;
import com.chientt.type.EncodingType;
import com.chientt.type.ObjectType;
import com.chientt.type.StorageType;

public class Util {
    public static RedisObject createObject(RedisServer server, ObjectType type, Object key) {
        RedisObject redisObject = new RedisObject();
        redisObject.objectType = type;
        redisObject.encodingType = EncodingType.raw;
        redisObject.ptr = key;
        redisObject.refCount = 1;
        redisObject.lru = server.lruclock;
        redisObject.storageType = StorageType.vmMemory;
        return redisObject;
    }

    public static RedisObject createStringObject(RedisServer server, Object key) {
        return createObject(server, ObjectType.string, key);
    }

    public static long time(Integer tmp){
        return System.currentTimeMillis() / 1000;
    }
}
