package com.chientt.myredis;

public interface AeEventFinalizerProc {
    int doit(AeEventLoop eventLoop,Object clientData);
}
