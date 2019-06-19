package com.chientt.myredis;

public class RedisCommand {
    String name;
    Object proc;
    int arity;
    int flags;
    Object redisVmPreLoadProc;
    int vmFirstKey;
    int vmLastKey;
    int vmKeyStep;

    public RedisCommand(String name, Object proc, int arity, int flags, Object redisVmPreLoadProc, int vmFirstKey, int vmLastKey, int vmKeyStep) {
        this.name = name;
        this.proc = proc;
        this.arity = arity;
        this.flags = flags;
        this.redisVmPreLoadProc = redisVmPreLoadProc;
        this.vmFirstKey = vmFirstKey;
        this.vmLastKey = vmLastKey;
        this.vmKeyStep = vmKeyStep;
    }
}
