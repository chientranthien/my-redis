package com.chientt.ae;

public class AeEventLoop {//Ae stands for antirez event or async event, I;m nor sure
    public static final int AE_SETSIZE = 1024 * 10;
    int maxfd;
    long timeEventNextId;
    AeFiredEvent[] events = new AeFiredEvent[AE_SETSIZE];
    AeFiredEvent[] fired = new AeFiredEvent[AE_SETSIZE];



}
