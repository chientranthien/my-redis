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
    RedisDb redisDb;
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
    AeEventLoop eventLoop;
    int cronLoops;
    long lastSave;
    long startTimeStat;
    long numCommandsStat;
    long numConnectionsStat;
    long expiredKeysStat;
    long evictedKeysStat;
    long keyspaceHitsStat;
    long keyspaceMissesStat;
    List<Object> slowLog;
    long slowLogEntryId;
    long slowLogLogSlowerThan;
    long slowLogMaxLen;

    //configuration
    int verbosity;
    int maxIdLeTime;
    int dbNum;
    int daemonSize;
    int appendOnly;
    int appendFsync;
    int noAppendFsyncOnRewrite;
    int shutdownAsap;
    long lastFSync;
    int appendFd;
    int appendSelDb;
    String pidFile;
    int bgSaveChildPid;
    int bgRewriteChildPid;
    String bgRewriteBuf;
    String aofBuf;
    SaveParam saveParam;
    int saveParamLen;
    String logFile;
    int syslogEnabled;
    String syslogIdent;
    int syslogFacility;
    String dbFileName;
    String appendFileName;
    String requirePass;
    int rdbCompress;
    int activeReharshing;
    int isSlave;
    String masterAuth;
    String masterHost;
    int masterPort;
    RedisClient master;

}
