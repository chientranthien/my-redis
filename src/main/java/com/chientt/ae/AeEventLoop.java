package com.chientt.ae;

import io.netty.channel.kqueue.KQueue;
import io.netty.channel.kqueue.KQueueEventLoopGroup;

public class AeEventLoop {//Ae stands for antirez event or async event, I;m not sure
    public static final int AE_SETSIZE = 1024 * 10;
    public static final int AE_OK = 0;
    public static final int AE_ERR = -1;

    public static final int AE_NONE = 0;
    public static final int AE_READABLE = 1;
    public static final int AE_WRITABLE = 2;

    public static final int AE_FILE_EVENTS = 1;
    public static final int AE_TIME_EVENTS = 2;
    public static final int AE_ALL_EVENTS = (AE_FILE_EVENTS | AE_TIME_EVENTS);
    public static final int AE_DONT_WAIT = 4;

    public static final int AE_NOMORE = -1;

    public int maxfd;
    public long timeEventNextId;
    public AeFileEvent[] events = new AeFileEvent[AE_SETSIZE];
    public AeFiredEvent[] fired = new AeFiredEvent[AE_SETSIZE];
    AeTimeEvent timeEventHead;
    int stop;
    Object apidata; /* This is used for polling API specific data */
    AeBeforeSleepProc beforesleep;
    KQueue kQueue;
     int aeApiAddEvent( int fd, int mask) {
         AeApiState state = (AeApiState) apidata;
         struct kevent ke;
         boolean KQueue.isAvailable();
         if (mask & AE_READABLE) {
            EV_SET(&ke, fd, EVFILT_READ, EV_ADD, 0, 0, NULL);
            if (kevent(state->kqfd, &ke, 1, NULL, 0, NULL) == -1) return -1;
        }
        if (mask & AE_WRITABLE) {
            EV_SET(&ke, fd, EVFILT_WRITE, EV_ADD, 0, 0, NULL);
            if (kevent(state->kqfd, &ke, 1, NULL, 0, NULL) == -1) return -1;
        }
        return 0;
    }

}
