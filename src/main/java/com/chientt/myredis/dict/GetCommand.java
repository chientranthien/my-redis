package com.chientt.myredis.dict;

import com.chientt.myredis.RedisClient;
import com.chientt.myredis.RedisDb;

public class GetCommand {
    robj *lookupKeyReadOrReply(redisClient *c, robj *key, robj *reply) {
        robj *o = lookupKeyRead(c->db, key);
        if (!o) addReply(c,reply);
        return o;
    }

    int getGenericCommand(RedisClient redisClient) {
        RedisDb redisDb;
        if ((o = lookupKeyReadOrReply(c,c->argv[1],shared.nullbulk)) == NULL)
            return REDIS_OK;

        if (o->type != REDIS_STRING) {
            addReply(c,shared.wrongtypeerr);
            return REDIS_ERR;
        } else {
            addReplyBulk(c,o);
            return REDIS_OK;
        }
    }
}
