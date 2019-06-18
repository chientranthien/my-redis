package com.chientt.myredis;

import com.chientt.type.ObjectType;
import sun.jvm.hotspot.oops.ShortField;

import static com.chientt.util.Util.createObject;

public class SharedObjects {
    public static final int REDIS_SHARED_INTEGERS = 10000;

    RedisObject crlf, ok, err, emptybulk, czero, cone, cnegone, pong, space,
            colon, nullbulk, nullmultibulk, queued,
            emptymultibulk, wrongtypeerr, nokeyerr, syntaxerr, sameobjecterr,
            outofrangeerr, loadingerr, plus,
            select0, select1, select2, select3, select4,
            select5, select6, select7, select8, select9,
            messagebulk, pmessagebulk, subscribebulk, unsubscribebulk, mbulk3,
            mbulk4, psubscribebulk, punsubscribebulk;

    RedisObject[] integers = new RedisObject[REDIS_SHARED_INTEGERS];

    public SharedObjects() {
        int j;

        crlf = createObject(RedisDb.server,ObjectType.string, "\r\n");
        ok = createObject(RedisDb.server,ObjectType.string, "+OK\r\n");
        err = createObject(REDIS_STRING, sdsnew("-ERR\r\n"));
        emptybulk = createObject(REDIS_STRING, sdsnew("$0\r\n\r\n"));
        czero = createObject(REDIS_STRING, sdsnew(":0\r\n"));
        cone = createObject(REDIS_STRING, sdsnew(":1\r\n"));
        cnegone = createObject(REDIS_STRING, sdsnew(":-1\r\n"));
        nullbulk = createObject(REDIS_STRING, sdsnew("$-1\r\n"));
        nullmultibulk = createObject(REDIS_STRING, sdsnew("*-1\r\n"));
        emptymultibulk = createObject(REDIS_STRING, sdsnew("*0\r\n"));
        pong = createObject(REDIS_STRING, sdsnew("+PONG\r\n"));
        queued = createObject(REDIS_STRING, sdsnew("+QUEUED\r\n"));
        wrongtypeerr = createObject(REDIS_STRING, sdsnew(
                "-ERR Operation against a key holding the wrong kind of value\r\n"));
        nokeyerr = createObject(REDIS_STRING, sdsnew(
                "-ERR no such key\r\n"));
        syntaxerr = createObject(REDIS_STRING, sdsnew(
                "-ERR syntax error\r\n"));
        sameobjecterr = createObject(REDIS_STRING, sdsnew(
                "-ERR source and destination objects are the same\r\n"));
        outofrangeerr = createObject(REDIS_STRING, sdsnew(
                "-ERR index out of range\r\n"));
        loadingerr = createObject(REDIS_STRING, sdsnew(
                "-LOADING Redis is loading the dataset in memory\r\n"));
        space = createObject(REDIS_STRING, sdsnew(" "));
        colon = createObject(REDIS_STRING, sdsnew(":"));
        plus = createObject(REDIS_STRING, sdsnew("+"));
        select0 = createStringObject("select 0\r\n", 10);
        select1 = createStringObject("select 1\r\n", 10);
        select2 = createStringObject("select 2\r\n", 10);
        select3 = createStringObject("select 3\r\n", 10);
        select4 = createStringObject("select 4\r\n", 10);
        select5 = createStringObject("select 5\r\n", 10);
        select6 = createStringObject("select 6\r\n", 10);
        select7 = createStringObject("select 7\r\n", 10);
        select8 = createStringObject("select 8\r\n", 10);
        select9 = createStringObject("select 9\r\n", 10);
        messagebulk = createStringObject("$7\r\nmessage\r\n", 13);
        pmessagebulk = createStringObject("$8\r\npmessage\r\n", 14);
        subscribebulk = createStringObject("$9\r\nsubscribe\r\n", 15);
        unsubscribebulk = createStringObject("$11\r\nunsubscribe\r\n", 18);
        psubscribebulk = createStringObject("$10\r\npsubscribe\r\n", 17);
        punsubscribebulk = createStringObject("$12\r\npunsubscribe\r\n", 19);
        mbulk3 = createStringObject("*3\r\n", 4);
        mbulk4 = createStringObject("*4\r\n", 4);
        for (j = 0; j < REDIS_SHARED_INTEGERS; j++) {
            integers[j] = createObject(REDIS_STRING, ( void*)(long) j);
            integers[j]->encoding = REDIS_ENCODING_INT;
        }

    }
}
