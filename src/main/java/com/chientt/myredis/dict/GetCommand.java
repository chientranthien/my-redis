package com.chientt.myredis.dict;

import com.chientt.myredis.RedisClient;
import com.chientt.myredis.RedisDb;
import com.chientt.myredis.RedisObject;
import com.chientt.type.ObjectType;

import static com.chientt.myredis.RedisClient.REDIS_OK;
import static com.chientt.myredis.RedisDb.shared;

public class GetCommand {
    robj *lookupKeyReadOrReply(redisClient *c, robj *key, robj *reply) {
        robj *o = lookupKeyRead(c->db, key);
        if (!o) addReply(c,reply);
        return o;
    }

    int getGenericCommand(RedisClient redisClient) {
        RedisDb redisDb;
        RedisObject o = redisClient.lookupKeyReadOrReply(redisClient.argv[1], shared.nullbulk);
        if (o == null)
            return REDIS_OK;

        if (o.objectType != ObjectType.string) {
            addReply(c,shared.wrongtypeerr);
            return REDIS_ERR;
        } else {
            addReplyBulk(c,o);
            return REDIS_OK;
        }
    }
    void addReply(RedisClient c,  RedisObject obj) {
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

}
