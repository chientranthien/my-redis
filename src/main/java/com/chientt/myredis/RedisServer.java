package com.chientt.myredis;

import java.util.List;

public class RedisServer {
    final static int ANET_ERR_LEN = 256;
    Thread mainThread;
    int port;
    String bindAddr;
    String unixSocket;
    int ipfd;
    int sofd;
    DB db;
    long dirty;
    long dirtyBeforeBackgroundSave;
    List<Object> clients;
    Dict commands;
    int loading;
    long loadingTotalBytes;
    long loadingLoadedBytes;
    long loadingStartTime;
    RedisCommand delCommand;
    RedisCommand multiCommand;
    List<Object> slaves;
    List<Object> monitors;
    char[] netError = new char[ANET_ERR_LEN];

}
