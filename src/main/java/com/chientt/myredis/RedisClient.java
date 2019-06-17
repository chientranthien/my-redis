package com.chientt.myredis;

import java.util.List;

public class RedisClient {
    public static final int REDIS_REPLY_CHUNK_BYTES = (5 * 1500);
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
    long replDbSize;
    MultiState mState;
    BlockingState bpop;
    List ioKeys;
    List watchedKeys;
    Dict pubsubChannels;
    List pubsubPattern;
    int bufPos;
    char[] buf = new char[REDIS_REPLY_CHUNK_BYTES];
}
