package com.chientt.myredis;

public class RedisDb {
    Dict<String, RedisObject> dict;
    Dict<String, Long> expires;
    Dict<String, RedisObject> blockingKeys;
    Dict<String, RedisObject> ioKeys;
    Dict<String, RedisObject> watchedKeys;
    int id;
    RedisServer server;

    RedisObject lookupKey(String key) {
        Dict.Entry<String, RedisObject> entry = dict.find(key);
        if (entry != null) {
            RedisObject val = entry.getValue();

            if (server.bgSaveChildPid == -1 && server.bgRewriteChildPid == -1)
                val.lru = server.lrulock;

            if (server.vmEnabled) {
                //TODO store into disk
            }
            server.keyspaceHitsStat++;
            return val;
        } else {
            server.keyspaceMissesStat++;
            return null;
        }
    }

    RedisObject lookupKeyRead(String key) {
        expireIfNeeded(key);
        return lookupKey(key);
    }

    private int expireIfNeeded(String key) {
        long when = getExpire(key);
        if (when < 0)
            return 0;
        if (server.loading)
            return 0;
        if (server.masterHost != null) {
            return System.currentTimeMillis() > when ? 1 : 0;
        }
        if(System.currentTimeMillis() <= when)
            return 0;
        server.expiredKeysStat++;
        return
    }

    private void propogateExpire(String key){
        
    }

    private long getExpire(String key) {
        Dict.Entry<String, Long> entry = expires.find(key);
        if (entry == null)
            return -1;

        return entry.getValue();

    }

}
