package com.chientt.ae;

public class AeTimeEvent {
    long id;
    long whenSec;
    long whenMs;
    AeTimeProc timeProc;
    AeEventFinalizerProc finalizerProc;
    Object clientData;
    AeTimeEvent next;
}
