package com.chientt.myredis;

import com.chientt.type.EncodingType;
import com.chientt.type.ObjectType;

import java.util.List;

import static com.chientt.util.Util.createObject;
import static com.chientt.util.Util.createStringObject;

public class RedisDb {

    public static final int REDIS_STATIC_ARGS = 8;
    Dict<RedisObject, RedisObject> dict;
    Dict<RedisObject, Long> expires;
    Dict<String, RedisObject> blockingKeys;
    Dict<String, RedisObject> ioKeys;
    Dict<String, RedisObject> watchedKeys;
    int id;
    public static RedisServer server;
    SharedObjects shared;

    RedisObject lookupKey(RedisObject key) {
        Dict.Entry<RedisObject, RedisObject> entry = dict.find(key);
        if (entry != null) {
            RedisObject val = entry.getValue();

            if (server.bgSaveChildPid == -1 && server.bgRewriteChildPid == -1)
                val.lru = server.lruclock;

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

    RedisObject lookupKeyRead(RedisObject key) {
        expireIfNeeded(key);
        return lookupKey(key);
    }

    private int expireIfNeeded(RedisObject key) {
        long when = getExpire(key);
        if (when < 0)
            return 0;
        if (server.loading)
            return 0;
        if (server.masterHost != null) {
            return System.currentTimeMillis() > when ? 1 : 0;
        }
        if (System.currentTimeMillis() <= when)
            return 0;
        server.expiredKeysStat++;
        return 1;
    }

    private void propogateExpire(RedisObject key) {
        RedisObject argv[] = new RedisObject[2];
        argv[0] = createStringObject(server, "DEL");
        argv[1] = key;
        if (server.appendOnly) {

        }
        if (server.slaves.size() > 0) {

        }
    }

    void replicationFeedSlaves(List slaves, int dictId, RedisObject[] objects) {
        RedisObject staticOutv[] = new RedisObject[REDIS_STATIC_ARGS * 3 + 1];
        RedisObject[] outv = null;
        if (objects.length <= staticOutv.length) {
            outv = staticOutv;
        } else {
            outv = new RedisObject[objects.length * 3 + 1];
        }

        RedisObject lenObject = createObject(server, ObjectType.string, String.format("*%d\r\n", objects.length));

        lenObject.refCount = 0;
        int outc = 0;
        outv[outc++] = lenObject;
        for (int j = 0; j < objects.length; j++) {
            lenObject = createObject(server, ObjectType.string, String.format("s%d\r\n", stringObjectLen(objects[j])));
            lenObject.refCount = 0;
            outv[outc++] = lenObject;
            outv[outc++] = objects[j];
            outv[outc++] = shared.crlf;
        }
    }

    int stringObjectLen(RedisObject object) {
        if (object.encodingType == EncodingType.raw)
            return ((String) object.ptr).length();
        else {
            return String.valueOf(object.ptr).length();
        }

    }

    void incrRefCount(RedisObject redisObject) {
        redisObject.refCount++;
    }

    private long getExpire(RedisObject key) {
        Dict.Entry<String, Long> entry = expires.find(key);
        if (entry == null)
            return -1;

        return entry.getValue();

    }


}
