package com.chientt.myredis;

import com.chientt.myredis.dict.CommandTableDictType;

import java.io.File;
import java.util.List;
import java.util.concurrent.locks.Lock;

import static com.chientt.myredis.Dict.DICT_OK;

public class RedisServer {
    public static final int REDIS_SERVERPORT = 6379;
    final static int ANET_ERR_LEN = 256;
    final static int REDIS_MAXIDLETIME = 60 * 5;
    final static int REDIS_IOBUF_LEN = 1024;
    final static int REDIS_LOADBUF_LEN = 1024;
    final static int REDIS_STATIC_ARGS = 8;
    final static int REDIS_DEFAULT_DBNUM = 16;
    final static int REDIS_CONFIGLINE_MAX = 1024;
    final static int REDIS_MAX_SYNC_TIME = 60;
    final static int REDIS_EXPIRELOOKUPS_PER_CRON = 10;
    final static int REDIS_MAX_WRITE_PER_EVENT = 1024 * 64;
    final static int REDIS_REQUEST_MAX_SIZE = 1024 * 1024 * 256;
    final static int REDIS_SHARED_INTEGERS = 10000;
    final static int REDIS_REPLY_CHUNK_BYTES = 5 * 1500;
    final static int REDIS_MAX_LOGMSG_LEN = 1024;
    final static int REDIS_SLOWLOG_LOG_SLOWER_THAN = 10000;
    final static int REDIS_SLOWLOG_MAX_LEN = 64;
    final static int LOG_LOCAL0 = 16 << 3;
    final static int APPENDFSYNC_NO = 0;
    final static int APPENDFSYNC_ALWAYS = 1;
    final static int APPENDFSYNC_EVERYSEC = 2;

    final static int REDIS_MAXMEMORY_VOLATILE_LRU = 0;
    final static int REDIS_MAXMEMORY_VOLATILE_TTL = 1;
    final static int REDIS_MAXMEMORY_VOLATILE_RANDOM = 2;
    final static int REDIS_MAXMEMORY_ALLKEYS_LRU = 3;
    final static int REDIS_MAXMEMORY_ALLKEYS_RANDOM = 4;
    final static int REDIS_MAXMEMORY_NO_EVICTION = 5;
    final static int REDIS_HASH_MAX_ZIPMAP_ENTRIES = 512;
    final static int REDIS_HASH_MAX_ZIPMAP_VALUE = 64;
    final static int REDIS_LIST_MAX_ZIPLIST_ENTRIES = 512;
    final static int REDIS_LIST_MAX_ZIPLIST_VALUE = 64;
    final static int REDIS_SET_MAX_INTSET_ENTRIES = 512;
    final static int REDIS_LRU_CLOCK_MAX = ((1 << 21) - 1);/* Max value of obj->lru */
    final static int REDIS_LRU_CLOCK_RESOLUTION = 10; /* LRU clock resolution in seconds */
    final static int REDIS_REPL_NONE = 0;   /* No active replication */
    final static int REDIS_REPL_CONNECT = 1;    /* Must connect to master */
    final static int REDIS_REPL_TRANSFER = 2;   /* Receiving .rdb from master */
    final static int REDIS_REPL_CONNECTED = 3; /* Connected to master */

    public static final double R_Zero = 0.0;
    public static final double R_PosInf = 1.0 / R_Zero;
    public static final double R_NegInf = -1.0 / R_Zero;
    public static final double R_Nan = R_Zero / R_Zero;
    public Thread mainThread;
    public int port;
    public String bindAddr;
    public String unixSocket;
    public int ipfd;
    public int sofd;
    public RedisDb redisDb;
    public long dirty;
    public long dirtyBeforeBackgroundSave;
    public List<Object> clients;
    public Dict commands;
    public boolean loading;
    public long loadingTotalBytes;
    public long loadingLoadedBytes;
    public long loadingStartTime;
    public RedisCommand delCommand;
    public RedisCommand multiCommand;
    public List<Object> slaves;
    public List<Object> monitors;
    public char[] netError = new char[ANET_ERR_LEN];
    public AeEventLoop eventLoop;
    public int cronLoops;
    public long lastSave;
    public long startTimeStat;
    public long numCommandsStat;
    public long numConnectionsStat;
    public long expiredKeysStat;
    public long evictedKeysStat;
    public long keyspaceHitsStat;
    public long keyspaceMissesStat;
    public List<Object> slowLog;
    public long slowLogEntryId;
    public long slowLogLogSlowerThan;
    public long slowLogMaxLen;

    //configuration
    public int verbosity;
    public int maxIdLeTime;
    public int dbNum;
    public int daemonize;
    public boolean appendOnly;
    public int appendFsync;
    public int noAppendFsyncOnRewrite;
    public int shutdownAsap;
    public long lastFSync;
    public int appendFd;
    public int appendSelDb;
    public String pidFile;
    public int bgSaveChildPid;
    public int bgRewriteChildPid;
    public String bgRewriteBuf;
    public String aofBuf;
    public List<SaveParam> saveParams;
    public String logFile;
    public boolean syslogEnabled;
    public String syslogIdent;
    public int syslogFacility;
    public String dbFileName;
    public String appendFileName;
    public String requirePass;
    public int rdbCompression;
    public int activeRehashing;
    public boolean isSlave;
    public String masterAuth;
    public String masterHost;
    public int masterPort;
    public RedisClient master;
    public int replState;
    public long replTransferLeft;
    public int replTransferS;
    public int replTransferFd;
    public String replTranferTmpFile;
    public long replTransferLastIo;
    public int replServerStateData;
    public int maxClients;
    public long maxMemory;
    public int maxMemoryPolicy;
    public int maxMemorySamples;
    public int bpopBlockedClients;
    public int vmBlockedClients;
    public List unblockedClients;
    public int sortDesc;
    public int sortAplha;
    public int sortByPattern;
    public boolean vmEnabled;
    public String vmSwapFile;
    public long vmPageSize;
    public long vmPages;
    public long vmMaxMemory;
    public long hashMaxZipMapEntries;
    public long hashMaxZipMapValue;
    public long listMaxZipListEntries;
    public long listMaxZipListValue;
    public long setMaxIntsetEntries;
    public File vmFp;
    public int vmFd;
    public long vmNextpage;
    public long vmNearPages;
    public String vmBitmap;
    public long unixTime;
    public List ioNewJobs;
    public List ioProcessing;
    public List ioProcessed;
    public List ioReadyClients;
    public Lock ioMutex;
    public Lock ioSwapFileMutex;
    public Lock ioThreadsAttr;
    public int ioActiveThreads;
    public int vmMaxThreads;
    public int ioReadyPipeRead;
    public int ioReadyPipeWrite;
    public long vmStatsUsagePage;
    public long vmStatsSwappedObjects;
    public long vmStatsSwapouts;
    public long vmStatsIns;
    public Dict pubsubChannels;
    public List pubsubPattern;
    public long lruclock = 22;
    public long lrulockPadding = 10;




    RedisCommand readonlyCommandTable[] = {
            new RedisCommand("get",getCommand,2,0,null,1,1,1),
            {"set",setCommand,3,REDIS_CMD_DENYOOM,NULL,0,0,0},
            {"setnx",setnxCommand,3,REDIS_CMD_DENYOOM,NULL,0,0,0},
            {"setex",setexCommand,4,REDIS_CMD_DENYOOM,NULL,0,0,0},
            {"append",appendCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"strlen",strlenCommand,2,0,NULL,1,1,1},
            {"del",delCommand,-2,0,NULL,0,0,0},
            {"exists",existsCommand,2,0,NULL,1,1,1},
            {"setbit",setbitCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"getbit",getbitCommand,3,0,NULL,1,1,1},
            {"setrange",setrangeCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"getrange",getrangeCommand,4,0,NULL,1,1,1},
            {"substr",getrangeCommand,4,0,NULL,1,1,1},
            {"incr",incrCommand,2,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"decr",decrCommand,2,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"mget",mgetCommand,-2,0,NULL,1,-1,1},
            {"rpush",rpushCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"lpush",lpushCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"rpushx",rpushxCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"lpushx",lpushxCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"linsert",linsertCommand,5,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"rpop",rpopCommand,2,0,NULL,1,1,1},
            {"lpop",lpopCommand,2,0,NULL,1,1,1},
            {"brpop",brpopCommand,-3,0,NULL,1,1,1},
            {"brpoplpush",brpoplpushCommand,4,REDIS_CMD_DENYOOM,NULL,1,2,1},
            {"blpop",blpopCommand,-3,0,NULL,1,1,1},
            {"llen",llenCommand,2,0,NULL,1,1,1},
            {"lindex",lindexCommand,3,0,NULL,1,1,1},
            {"lset",lsetCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"lrange",lrangeCommand,4,0,NULL,1,1,1},
            {"ltrim",ltrimCommand,4,0,NULL,1,1,1},
            {"lrem",lremCommand,4,0,NULL,1,1,1},
            {"rpoplpush",rpoplpushCommand,3,REDIS_CMD_DENYOOM,NULL,1,2,1},
            {"sadd",saddCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"srem",sremCommand,3,0,NULL,1,1,1},
            {"smove",smoveCommand,4,0,NULL,1,2,1},
            {"sismember",sismemberCommand,3,0,NULL,1,1,1},
            {"scard",scardCommand,2,0,NULL,1,1,1},
            {"spop",spopCommand,2,0,NULL,1,1,1},
            {"srandmember",srandmemberCommand,2,0,NULL,1,1,1},
            {"sinter",sinterCommand,-2,REDIS_CMD_DENYOOM,NULL,1,-1,1},
            {"sinterstore",sinterstoreCommand,-3,REDIS_CMD_DENYOOM,NULL,2,-1,1},
            {"sunion",sunionCommand,-2,REDIS_CMD_DENYOOM,NULL,1,-1,1},
            {"sunionstore",sunionstoreCommand,-3,REDIS_CMD_DENYOOM,NULL,2,-1,1},
            {"sdiff",sdiffCommand,-2,REDIS_CMD_DENYOOM,NULL,1,-1,1},
            {"sdiffstore",sdiffstoreCommand,-3,REDIS_CMD_DENYOOM,NULL,2,-1,1},
            {"smembers",sinterCommand,2,0,NULL,1,1,1},
            {"zadd",zaddCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"zincrby",zincrbyCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"zrem",zremCommand,3,0,NULL,1,1,1},
            {"zremrangebyscore",zremrangebyscoreCommand,4,0,NULL,1,1,1},
            {"zremrangebyrank",zremrangebyrankCommand,4,0,NULL,1,1,1},
            {"zunionstore",zunionstoreCommand,-4,REDIS_CMD_DENYOOM,zunionInterBlockClientOnSwappedKeys,0,0,0},
            {"zinterstore",zinterstoreCommand,-4,REDIS_CMD_DENYOOM,zunionInterBlockClientOnSwappedKeys,0,0,0},
            {"zrange",zrangeCommand,-4,0,NULL,1,1,1},
            {"zrangebyscore",zrangebyscoreCommand,-4,0,NULL,1,1,1},
            {"zrevrangebyscore",zrevrangebyscoreCommand,-4,0,NULL,1,1,1},
            {"zcount",zcountCommand,4,0,NULL,1,1,1},
            {"zrevrange",zrevrangeCommand,-4,0,NULL,1,1,1},
            {"zcard",zcardCommand,2,0,NULL,1,1,1},
            {"zscore",zscoreCommand,3,0,NULL,1,1,1},
            {"zrank",zrankCommand,3,0,NULL,1,1,1},
            {"zrevrank",zrevrankCommand,3,0,NULL,1,1,1},
            {"hset",hsetCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"hsetnx",hsetnxCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"hget",hgetCommand,3,0,NULL,1,1,1},
            {"hmset",hmsetCommand,-4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"hmget",hmgetCommand,-3,0,NULL,1,1,1},
            {"hincrby",hincrbyCommand,4,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"hdel",hdelCommand,3,0,NULL,1,1,1},
            {"hlen",hlenCommand,2,0,NULL,1,1,1},
            {"hkeys",hkeysCommand,2,0,NULL,1,1,1},
            {"hvals",hvalsCommand,2,0,NULL,1,1,1},
            {"hgetall",hgetallCommand,2,0,NULL,1,1,1},
            {"hexists",hexistsCommand,3,0,NULL,1,1,1},
            {"incrby",incrbyCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"decrby",decrbyCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"getset",getsetCommand,3,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"mset",msetCommand,-3,REDIS_CMD_DENYOOM,NULL,1,-1,2},
            {"msetnx",msetnxCommand,-3,REDIS_CMD_DENYOOM,NULL,1,-1,2},
            {"randomkey",randomkeyCommand,1,0,NULL,0,0,0},
            {"select",selectCommand,2,0,NULL,0,0,0},
            {"move",moveCommand,3,0,NULL,1,1,1},
            {"rename",renameCommand,3,0,NULL,1,1,1},
            {"renamenx",renamenxCommand,3,0,NULL,1,1,1},
            {"expire",expireCommand,3,0,NULL,0,0,0},
            {"expireat",expireatCommand,3,0,NULL,0,0,0},
            {"keys",keysCommand,2,0,NULL,0,0,0},
            {"dbsize",dbsizeCommand,1,0,NULL,0,0,0},
            {"auth",authCommand,2,0,NULL,0,0,0},
            {"ping",pingCommand,1,0,NULL,0,0,0},
            {"echo",echoCommand,2,0,NULL,0,0,0},
            {"save",saveCommand,1,0,NULL,0,0,0},
            {"bgsave",bgsaveCommand,1,0,NULL,0,0,0},
            {"bgrewriteaof",bgrewriteaofCommand,1,0,NULL,0,0,0},
            {"shutdown",shutdownCommand,1,0,NULL,0,0,0},
            {"lastsave",lastsaveCommand,1,0,NULL,0,0,0},
            {"type",typeCommand,2,0,NULL,1,1,1},
            {"multi",multiCommand,1,0,NULL,0,0,0},
            {"exec",execCommand,1,REDIS_CMD_DENYOOM,execBlockClientOnSwappedKeys,0,0,0},
            {"discard",discardCommand,1,0,NULL,0,0,0},
            {"sync",syncCommand,1,0,NULL,0,0,0},
            {"flushdb",flushdbCommand,1,0,NULL,0,0,0},
            {"flushall",flushallCommand,1,0,NULL,0,0,0},
            {"sort",sortCommand,-2,REDIS_CMD_DENYOOM,NULL,1,1,1},
            {"info",infoCommand,1,0,NULL,0,0,0},
            {"monitor",monitorCommand,1,0,NULL,0,0,0},
            {"ttl",ttlCommand,2,0,NULL,1,1,1},
            {"persist",persistCommand,2,0,NULL,1,1,1},
            {"slaveof",slaveofCommand,3,0,NULL,0,0,0},
            {"debug",debugCommand,-2,0,NULL,0,0,0},
            {"config",configCommand,-2,0,NULL,0,0,0},
            {"subscribe",subscribeCommand,-2,0,NULL,0,0,0},
            {"unsubscribe",unsubscribeCommand,-1,0,NULL,0,0,0},
            {"psubscribe",psubscribeCommand,-2,0,NULL,0,0,0},
            {"punsubscribe",punsubscribeCommand,-1,0,NULL,0,0,0},
            {"publish",publishCommand,3,REDIS_CMD_FORCE_REPLICATION,NULL,0,0,0},
            {"watch",watchCommand,-2,0,NULL,0,0,0},
            {"unwatch",unwatchCommand,1,0,NULL,0,0,0},
            {"object",objectCommand,-2,0,NULL,0,0,0},
            {"slowlog",slowlogCommand,-2,0,NULL,0,0,0}
    };
    public void initServer() {
        port = REDIS_SERVERPORT;
        bindAddr = null;
        unixSocket = null;
        ipfd = -1;
        sofd = -1;
        dbNum = REDIS_DEFAULT_DBNUM;
        verbosity = 1;
        maxIdLeTime = REDIS_MAXIDLETIME;
        saveParams = null;
        loading = false;
        logFile = null; /* NULL = log on standard output */
        syslogEnabled = false;
        syslogIdent = "redis";
        syslogFacility = LOG_LOCAL0;
        daemonize = 0;
        appendOnly = false;
        appendFsync = APPENDFSYNC_EVERYSEC;
        noAppendFsyncOnRewrite = 0;
        lastFSync = System.currentTimeMillis();
        appendFd = -1;
        appendSelDb = -1; /* Make sure the first time will not match */
        pidFile = "/var/run/redis.pid";
        dbFileName = "dump.rdb";
        appendFileName = "appendonly.aof";
        requirePass = null;
        rdbCompression = 1;
        activeRehashing = 1;
        maxClients = 0;
        bpopBlockedClients = 0;
        maxMemory = 0;
        maxMemoryPolicy = REDIS_MAXMEMORY_VOLATILE_LRU;
        maxMemorySamples = 3;
        vmEnabled = false;
        vmSwapFile = "/tmp/redis-%p.vm";
        vmPageSize = 256;          /* 256 bytes per page */
        vmPages = 1024 * 1024 * 100;    /* 104 millions of pages */
        vmMaxMemory = 1024 * 1024 * 1024 * 1; /* 1 GB of RAM */
        vmMaxThreads = 4;
        vmBlockedClients = 0;
        hashMaxZipMapEntries = REDIS_HASH_MAX_ZIPMAP_ENTRIES;
        hashMaxZipMapValue = REDIS_HASH_MAX_ZIPMAP_VALUE;
        listMaxZipListEntries = REDIS_LIST_MAX_ZIPLIST_ENTRIES;
        listMaxZipListValue = REDIS_LIST_MAX_ZIPLIST_VALUE;
        setMaxIntsetEntries = REDIS_SET_MAX_INTSET_ENTRIES;
        shutdownAsap = 0;

        updateLRUClock();
//        resetServerSaveParams();

        appendServerSaveParams(60 * 60, 1);  /* save after 1 hour and 1 change */
        appendServerSaveParams(300, 100);  /* save after 5 minutes and 100 changes */
        appendServerSaveParams(60, 10000); /* save after 1 minute and 10000 changes */
        /* Replication related */
        isSlave = false;
        masterAuth = null;
        masterHost = null;
        masterPort = 6379;
        master = null;
        replState = REDIS_REPL_NONE;
        replServerStateData = 1;


        /* Command table -- we intiialize it here as it is part of the
         * initial configuration, since command names may be changed via
         * redis.conf using the rename-command directive. */
        commands = new Dict(new CommandTableDictType());
        populateCommandTable();
        delCommand = lookupCommandByCString("del");
        multiCommand = lookupCommandByCString("multi");

        /* Slow log */
        slowlog_log_slower_than = REDIS_SLOWLOG_LOG_SLOWER_THAN;
        slowlog_max_len = REDIS_SLOWLOG_MAX_LEN;

    }

    void updateLRUClock() {
        this.lruclock = (System.currentTimeMillis() / (1000 * REDIS_LRU_CLOCK_RESOLUTION)) &
                REDIS_LRU_CLOCK_MAX;
    }

    void appendServerSaveParams(long seconds, int changes) {
        saveParams.add(new SaveParam(seconds, changes));
    }
    void populateCommandTable() {
        int numcommands = sizeof(readonlyCommandTable)/sizeof(struct redisCommand);

        for (int j = 0; j < numcommands; j++) {
            struct redisCommand *c = readonlyCommandTable+j;
            int retval;

            retval = dictAdd(server.commands, sdsnew(c->name), c);
        }
    }

}
