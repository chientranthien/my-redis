package com.chientt.myredis;

import java.util.List;

public class RedisClient {
    int fd;
    RedisDb redisDb;
    int dictId;
    String queryBuf;
    int argc;
    RedisObject[] argv;
    RedisCommand cmd;
    int reqType;
    int multiBulkLen;
    long bulkLen;
    List<Object> reply;
    int sentLen;
    long lastInteraction;
    int flags;
    int slaveSelDb;
    int authenticated;
    int replState;
    int replDbFd;
    long replDbOff;
}
