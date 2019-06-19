package com.chientt.myredis;

import java.util.List;

import static com.chientt.myredis.RedisServer.REDIS_REPL_NONE;

public class RedisClient {
    public static final int REDIS_REPLY_CHUNK_BYTES = (5 * 1500);
    int fd;
    RedisDb redisDb;
    int dictId;
    String queryBuf;
    int argc;
    RedisObject[] argv;
    RedisCommand cmd;
    int reqType;
    int multiBulkLen;
    long bulkLen;
    List<Object> reply;
    int sentLen;
    long lastInteraction;
    int flags;
    int slaveSelDb;
    int authenticated;
    int replState;
    int replDbFd;
    long replDbOff;
    long replDbSize;
    MultiState mState;
    BlockingState bpop;
    List ioKeys;
    List watchedKeys;
    Dict pubsubChannels;
    List pubsubPattern;
    int bufPos;
    char[] buf = new char[REDIS_REPLY_CHUNK_BYTES];


    RedisObject lookupKeyReadOrReply( RedisObject key, RedisObject reply) {
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
        if (obj->encoding == REDIS_ENCODING_RAW) {
            if (_addReplyToBuffer(c,obj->ptr,sdslen(obj->ptr)) != REDIS_OK)
                _addReplyObjectToList(c,obj);
        } else {
            /* FIXME: convert the long into string and use _addReplyToBuffer()
             * instead of calling getDecodedObject. As this place in the
             * code is too performance critical. */
            obj = getDecodedObject(obj);
            if (_addReplyToBuffer(c,obj->ptr,sdslen(obj->ptr)) != REDIS_OK)
                _addReplyObjectToList(c,obj);
            decrRefCount(obj);
        }
    }
    final static int REDIS_OK             =   0;
            final static int REDIS_ERR     =          -1;


    int _installWriteEvent(RedisClient client) {
        if (client.fd <= 0) return REDIS_ERR;
        if (client.bufPos == 0 && client.reply.size() == 0 &&
                (client.replState == REDIS_REPL_NONE ||
                        c->replstate == REDIS_REPL_ONLINE) &&
                aeCreateFileEvent(server.el, c->fd, AE_WRITABLE,
                        sendReplyToClient, c) == AE_ERR) return REDIS_ERR;
        return REDIS_OK;
    }

}
