package com.chientt.ae;

import com.chientt.myredis.RedisClient;
import com.chientt.myredis.RedisObject;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.chientt.myredis.RedisClient.REDIS_CLOSE_AFTER_REPLY;
import static com.chientt.myredis.RedisClient.REDIS_MASTER;
import static com.chientt.myredis.RedisServer.REDIS_MAX_WRITE_PER_EVENT;
import static com.chientt.util.Util.time;

public class AeFileProcSendReply implements  AeFileProc {
    int write(int fd, char[] buf, int from, int len){
        FileDescriptor fdOb = new FileDescriptor();
        sun.misc.SharedSecrets.getJavaIOFileDescriptorAccess().set(fdOb,fd);
        FileOutputStream out = new FileOutputStream(fdOb);
        byte[] bytes = new String(buf).getBytes();
        try {
            out.write(bytes,from,len);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return len;
    }
    public void aeFileProc(AeEventLoop el, int fd, Object privData, int mask) {
        RedisClient client = (RedisClient) privData;
        int nwritten = 0, totwritten = 0, objlen;
RedisObject o;
        while(client.bufPos > 0 || client.reply.size() > 0) {
            if (client.bufPos > 0) {
                if ((client.flags & REDIS_MASTER )!= 0) {
                    /* Don't reply to a master */
                    nwritten = client.bufPos - client.sentLen;
                } else {
                    nwritten = write(fd,client.buf,client.sentLen,client.bufPos-client.sentLen);
                    if (nwritten <= 0) break;
                }
                client.sentLen += nwritten;
                totwritten += nwritten;

                /* If the buffer was sent, set bufpos to zero to continue with
                 * the remainder of the reply. */
                if (client.sentLen == client.bufPos) {
                    client.bufPos = 0;
                    client.sentLen = 0;
                }
            } else {
                o = (RedisObject) client.reply.get(0);
                objlen = ((String)o.ptr).length();
                String ptrValue = ((String)o.ptr);
                if (objlen == 0) {
                    client.reply.remove(0);
                    continue;
                }

                if ((client.flags & REDIS_MASTER) !=0) {
                    /* Don't reply to a master */
                    nwritten = objlen - client.sentLen;
                } else {
                    nwritten = write(fd, ptrValue.toCharArray(),client.sentLen,objlen-client.sentLen);
                    if (nwritten <= 0) break;
                }
                client.sentLen  += nwritten;
                totwritten += nwritten;

                /* If we fully sent the object on head go to the next one */
                if (client.sentLen == objlen) {
                    client.reply.remove(0);
                    client.sentLen = 0;
                }
            }
            /* Note that we avoid to send more thank REDIS_MAX_WRITE_PER_EVENT
             * bytes, in a single threaded server it's a good idea to serve
             * other clients as well, even if a very large request comes from
             * super fast link that is always able to accept data (in real world
             * scenario think about 'KEYS *' against the loopback interfae) */
            if (totwritten > REDIS_MAX_WRITE_PER_EVENT) break;
        }//end while loop
        if (nwritten == -1) {
            if (errno == EAGAIN) {
                nwritten = 0;
            } else {
                redisLog(REDIS_VERBOSE,
                        "Error writing to client: %s", strerror(errno));
                freeClient(c);
                return;
            }
        }
        if (totwritten > 0) client.lastInteraction = time(null);
        if (client.bufPos == 0 && client.reply.size() == 0) {
            client.sentLen = 0;
            aeDeleteFileEvent(server.el,client.fd,AE_WRITABLE);

            /* Close connection after entire reply has been sent. */
            if (client.flags & REDIS_CLOSE_AFTER_REPLY) freeClient(c);
        }
    }
}
