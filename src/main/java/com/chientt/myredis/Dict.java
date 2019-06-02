package com.chientt.myredis;

public class Dict {
    public static int DICT_OK = 0;
    public static int DICT_ERROR = 1;
    public static int DICT_HT_INITIAL_SIZE = 4;
    DictType dictType;
    //String privateDate;
    DictHashTable hashTables[] = new DictHashTable[2];
    int rehashIndex;/* rehashing not in progress if rehashidx == -1 */
    int iterators; /* number of iterators currently running */

    int dictIntHashFunction(int key) {
        key += ~(key << 15);
        key ^= (key >> 10);
        key += (key << 3);
        key ^= (key >> 6);
        key += ~(key << 11);
        key ^= (key >> 16);
        return key;
    }

    int dictIdentityHashFunction(int key) {
        return key;
    }

    int dictGenHashFunction(char[] buf, int len) {
        int hash = 5381;

        int i = 0;
        while (len-- != 0)
            hash = ((hash << 5) + hash) + (buf[i++]); /* hash * 33 + c */
        return hash;
    }

    int dictGenCaseHashFunction(char[] buf, int len) {
        int hash = 5381;
        int i = 0;
        while (len-- != 0)
            hash = ((hash << 5) + hash) + (Character.toLowerCase(buf[i++])); /* hash * 33 + c */
        return hash;
    }

    static void resetDict(DictHashTable dictHashTable) {
        dictHashTable.table = null;
        dictHashTable.size = dictHashTable.sizeMask = dictHashTable.used = 0;

    }

    public Dict createDict(DictType dictType, String privateDataPointer) {
        Dict dict = new Dict();
        dict.dictType = dictType;
        dict.rehashIndex = -1;
        return dict;
    }

    boolean isRehashing() {
        return rehashIndex != -1;
    }

    int resize() {
        if (isRehashing()) {
            return DICT_ERROR;
        }
        int minimal = Math.max(hashTables[0].used, DICT_HT_INITIAL_SIZE);
        return expand(minimal);

    }

    int expand(int size) {
        int realSize = nextPower(size);
        if (isRehashing() || hashTables[0].used > size) {
            return DICT_ERROR;
        }
        DictHashTable newHashTable = new DictHashTable( realSize);
        if(hashTables[0].table == null) {
            hashTables[0] = newHashTable;
            return DICT_OK;
        }
        hashTables[1] = newHashTable;
        rehashIndex = 0;
        return DICT_OK;
    }

    static int nextPower(int size) {
        int i = DICT_HT_INITIAL_SIZE;
        if (size > Integer.MAX_VALUE / 2)
            size = Integer.MAX_VALUE;
        while (true) {
            if (i >= size)
                return i;
            i *= 2;
        }
    }

    public static class Entry<K, V> {
        Entry next;
        K key;
        V value;
    }

    public static class DictHashTable {

        Entry[] table;
        int size;
        int sizeMask;
        int used;

        public DictHashTable() {

        }

        public DictHashTable(int size) {
            this.table = new Entry[size];
            this.size = size;
            this.sizeMask = size - 1;
        }
    }

    public static class DictIterator {
        Dict dict;
        int table, index, safe;
        Entry nextEntry;
    }
}
