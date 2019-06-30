package com.chientt.myredis;

import com.chientt.type.EncodingType;
import com.chientt.type.ObjectType;
import sun.jvm.hotspot.oops.ShortField;

import static com.chientt.util.Util.createObject;
import static com.chientt.util.Util.createStringObject;

public class SharedObjects {
    public static final int REDIS_SHARED_INTEGERS = 10000;

    public RedisObject crlf, ok, err, emptybulk, czero, cone, cnegone, pong, space,
            colon, nullbulk, nullmultibulk, queued,
            emptymultibulk, wrongtypeerr, nokeyerr, syntaxerr, sameobjecterr,
            outofrangeerr, loadingerr, plus,
            select0, select1, select2, select3, select4,
            select5, select6, select7, select8, select9,
            messagebulk, pmessagebulk, subscribebulk, unsubscribebulk, mbulk3,
            mbulk4, psubscribebulk, punsubscribebulk;

    RedisObject[] integers = new RedisObject[REDIS_SHARED_INTEGERS];

    public SharedObjects() {

        crlf = createObject(RedisDb.server,ObjectType.string, "\r\n");
        ok = createObject(RedisDb.server,ObjectType.string, "+OK\r\n");
        err = createObject(RedisDb.server,ObjectType.string,  "-ERR\r\n");
        emptybulk = createObject(RedisDb.server,ObjectType.string, "$0\r\n\r\n");
        czero = createObject(RedisDb.server,ObjectType.string, ":0\r\n");
        cone = createObject(RedisDb.server,ObjectType.string, ":1\r\n");
        cnegone = createObject(RedisDb.server,ObjectType.string, ":-1\r\n");
        nullbulk = createObject(RedisDb.server,ObjectType.string, "$-1\r\n");
        nullmultibulk = createObject(RedisDb.server,ObjectType.string, "*-1\r\n");
        emptymultibulk = createObject(RedisDb.server,ObjectType.string, "*0\r\n");
        pong = createObject(RedisDb.server,ObjectType.string, "+PONG\r\n");
        queued = createObject(RedisDb.server,ObjectType.string, "+QUEUED\r\n");
        wrongtypeerr = createObject(RedisDb.server,ObjectType.string,
                "-ERR Operation against a key holding the wrong kind of value\r\n");
        nokeyerr = createObject(RedisDb.server,ObjectType.string,
                "-ERR no such key\r\n");
        syntaxerr = createObject(RedisDb.server,ObjectType.string,
                "-ERR syntax error\r\n");
        sameobjecterr = createObject(RedisDb.server,ObjectType.string,
                "-ERR source and destination objects are the same\r\n");
        outofrangeerr = createObject(RedisDb.server,ObjectType.string,
                "-ERR index out of range\r\n");
        loadingerr = createObject(RedisDb.server,ObjectType.string,
                "-LOADING Redis is loading the dataset in memory\r\n");
        space = createObject(RedisDb.server,ObjectType.string, " ");
        colon = createObject(RedisDb.server,ObjectType.string, ":");
        plus = createObject(RedisDb.server,ObjectType.string, "+");
        select0 = createStringObject(RedisDb.server,"select 0\r\n");
        select1 = createStringObject(RedisDb.server,"select 1\r\n");
        select2 = createStringObject(RedisDb.server,"select 2\r\n");
        select3 = createStringObject(RedisDb.server,"select 3\r\n");
        select4 = createStringObject(RedisDb.server,"select 4\r\n");
        select5 = createStringObject(RedisDb.server,"select 5\r\n");
        select6 = createStringObject(RedisDb.server,"select 6\r\n");
        select7 = createStringObject(RedisDb.server,"select 7\r\n");
        select8 = createStringObject(RedisDb.server,"select 8\r\n");
        select9 = createStringObject(RedisDb.server,"select 9\r\n");
        messagebulk = createStringObject(RedisDb.server,"$7\r\nmessage\r\n");
        pmessagebulk = createStringObject(RedisDb.server,"$8\r\npmessage\r\n");
        subscribebulk = createStringObject(RedisDb.server,"$9\r\nsubscribe\r\n");
        unsubscribebulk = createStringObject(RedisDb.server,"$11\r\nunsubscribe\r\n");
        psubscribebulk = createStringObject(RedisDb.server,"$10\r\npsubscribe\r\n");
        punsubscribebulk = createStringObject(RedisDb.server,"$12\r\npunsubscribe\r\n");
        mbulk3 = createStringObject(RedisDb.server,"*3\r\n");
        mbulk4 = createStringObject(RedisDb.server,"*4\r\n");
        for (Integer j = 0; j < REDIS_SHARED_INTEGERS; j++) {
            integers[j] = createObject(RedisDb.server,ObjectType.string,j);
            integers[j].encodingType= EncodingType.integer;
        }

    }
}
