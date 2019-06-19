package com.chientt.myredis.dict;

import com.chientt.myredis.RedisClient;

public interface CommandFunction {
    void doCommand(RedisClient redisClient);
}
