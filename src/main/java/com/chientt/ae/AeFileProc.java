package com.chientt.ae;

public interface AeFileProc {
    void aeFileProc(AeEventLoop eventLoop, int fd, Object clientData, int mask);
}
