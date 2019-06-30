package com.chientt.myredis;

import com.chientt.ae.AeEventLoop;
import com.chientt.ae.AeFileEvent;
import com.chientt.ae.AeFileProc;

import java.util.List;

import static com.chientt.ae.AeEventLoop.*;
import static com.chientt.myredis.RedisServer.REDIS_REPL_NONE;
import static com.chientt.myredis.RedisServer.REDIS_REPL_ONLINE;

public class RedisClient {
    public static final int REDIS_SLAVE = 1;       /* This client is a slave server */
    public static final int REDIS_MASTER = 2;      /* This client is a master server */
    public static final int REDIS_MONITOR = 4;     /* This client is a slave monitor, see MONITOR */
    public static final int REDIS_MULTI = 8;     /* This client is in a MULTI context */
    public static final int REDIS_BLOCKED = 16;/* The client is waiting in a blocking operation */
    public static final int REDIS_IO_WAIT = 32;/* The client is waiting for Virtual Memory I/O */
    public static final int REDIS_DIRTY_CAS = 64;/* Watched keys modified. EXEC will fail. */
    public static final int REDIS_CLOSE_AFTER_REPLY = 128;/* Close after writing entire reply. */
    public static final int REDIS_UNBLOCKED = 256;

    public static final int REDIS_REPLY_CHUNK_BYTES = (5 * 1500);
    public int fd;
    public RedisDb redisDb;
    public int dictId;
    public String queryBuf;
    public int argc;
    public RedisObject[] argv;
    public RedisCommand cmd;
    public int reqType;
    public int multiBulkLen;
    public long bulkLen;
    public List<Object> reply;
    public int sentLen;
    public long lastInteraction;
    public int flags;
    public int slaveSelDb;
    public int authenticated;
    public int replState;
    public int replDbFd;
    public long replDbOff;
    public long replDbSize;
    public MultiState mState;
    public BlockingState bpop;
    public List ioKeys;
    public List watchedKeys;
    public Dict pubsubChannels;
    public List pubsubPattern;
    public int bufPos;
    public char[] buf = new char[REDIS_REPLY_CHUNK_BYTES];


    public RedisObject lookupKeyReadOrReply( RedisObject key, RedisObject reply) {
        RedisObject value = redisDb.lookupKeyRead( key);
        if (value!= null)
            addReply(reply);
        return value;
    }
    void addReply(  RedisObject redisObject) {
        if (_installWriteEvent(c) != REDIS_OK) return;
        redisAssert(!server.vm_enabled || obj->storage == REDIS_VM_MEMORY);

        /* This is an important place where we can avoid copy-on-write
         * when there is a saving child running, avoiding touching the
         * refcount field of the object if it's not needed.
         *
         * If the encoding is RAW and there is room in the static buffer
         * we'll be able to send the object to the client without
         * messing with its page. */
        if(obj->encoding ==REDIS_ENCODING_RAW)

    {
        if (_addReplyToBuffer(c, obj -> ptr, sdslen(obj -> ptr)) != REDIS_OK)
            _addReplyObjectToList(c, obj);
    } else

    {
        /* FIXME: convert the long into string and use _addReplyToBuffer()
         * instead of calling getDecodedObject. As this place in the
         * code is too performance critical. */
        obj = getDecodedObject(obj);
        if (_addReplyToBuffer(c, obj -> ptr, sdslen(obj -> ptr)) != REDIS_OK)
            _addReplyObjectToList(c, obj);
        decrRefCount(obj);
    }

}
    public final static int REDIS_OK = 0;
    final static int REDIS_ERR = -1;


    int _installWriteEvent(RedisClient client) {
        if (client.fd <= 0) return REDIS_ERR;
        if (client.bufPos == 0 && client.reply.size() == 0 &&
                (client.replState == REDIS_REPL_NONE ||
                        client.replState == REDIS_REPL_ONLINE) &&
                aeCreateFileEvent(server.el, c -> fd, AE_WRITABLE,
                        sendReplyToClient, c) == AE_ERR) return REDIS_ERR;
        return REDIS_OK;
    }


    int aeCreateFileEvent(AeEventLoop eventLoop, int fd, int mask,
                          AeFileProc proc, Object clientData) {
        if (fd >= AE_SETSIZE)
            return AE_ERR;
        AeFileEvent fe = eventLoop.events[fd];

        if (aeApiAddEvent(eventLoop, fd, mask) == -1)
            return AE_ERR;
        fe -> mask |= mask;
        if (mask & AE_READABLE) fe -> rfileProc = proc;
        if (mask & AE_WRITABLE) fe -> wfileProc = proc;
        fe -> clientData = clientData;
        if (fd > eventLoop -> maxfd)
            eventLoop -> maxfd = fd;
        return AE_OK;
    }

}
