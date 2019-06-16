package com.chientt.myredis;

public class RedisDb {
    Dict<String, RedisObject> dict;
    Dict<String, RedisObject> expires;
    Dict<String, RedisObject> blockingKeys;
    Dict<String, RedisObject> ioKeys;
    Dict<String, RedisObject> watchedKeys;
    int id;
    RedisServer redisServer;

    RedisObject lookupKey(String key) {
        Dict.Entry<String,RedisObject> entry = dict.find(key);
        if (entry != null) {
            RedisObject value = entry.getValue();

        }
    }

}
