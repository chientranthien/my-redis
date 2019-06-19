package com.chientt.myredis;

import com.chientt.myredis.dict.DictType;
import com.chientt.myredis.dict.StringDictType;

public class Dict<K, V> {
    public static int DICT_OK = 0;
    public static int DICT_ERROR = 1;
    public static int DICT_HT_INITIAL_SIZE = 4;
    DictType  dictType;
    //String privateDate;
    public DictHashTable<K, V> hashTables[];
    int rehashIndex;/* rehashing not in progress if rehashidx == -1 */
    int iterators; /* number of iterators currently running */

    public Dict() {
        rehashIndex = -1;
        hashTables = new DictHashTable[2];
        hashTables[0] = new DictHashTable<K, V>(DICT_HT_INITIAL_SIZE);
        hashTables[1] = new DictHashTable<K, V>(DICT_HT_INITIAL_SIZE);

        dictType =   new StringDictType<K, V>();
    }

    public Dict(DictType dictType) {
        rehashIndex = -1;
        hashTables = new DictHashTable[2];
        hashTables[0] = new DictHashTable<K, V>(DICT_HT_INITIAL_SIZE);
        hashTables[1] = new DictHashTable<K, V>(DICT_HT_INITIAL_SIZE);

        this.dictType = dictType;
    }
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
//        dictHashTable.table = null;
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
        DictHashTable newHashTable = new DictHashTable(realSize);
        if (hashTables[0].table == null) {
            hashTables[0] = newHashTable;
            return DICT_OK;
        }
        hashTables[1] = newHashTable;
        rehashIndex = 0;
        return DICT_OK;
    }

    void copy() {
        hashTables[0].size = hashTables[1].size;
        hashTables[0].sizeMask = hashTables[1].sizeMask;
        hashTables[0].used = hashTables[1].used;
        hashTables[0].table = hashTables[1].table;
    }

    /**
     * @param n steps of incremental rehashing
     * @return Returns 1 if there are still keys to move from the old to the new hash table, otherwise 0 is returned.
     */
    int rehash(int n) {
        if (!isRehashing())
            return 0;
        while (n-- != 0) {
            /* Check if we already rehashed the whole table... */
            if (hashTables[0].used == 0) {
                copy();
                resetDict(hashTables[1]);
                rehashIndex = -1;
                return 0;
            }

            while (hashTables[0].table[rehashIndex] == null)
                rehashIndex++;
            Entry<K, V> entry = hashTables[0].table[rehashIndex];
            while (entry != null) {
                Entry nextEntry = entry.next;
                //TODO String to generic
                int hash = dictHashKey(entry.key) & hashTables[1].sizeMask;

                entry.next = hashTables[1].table[hash];
                hashTables[1].table[hash] = entry;
                hashTables[0].used--;
                hashTables[1].used++;
                entry = nextEntry;
            }
            hashTables[0].table[rehashIndex] = null;
            rehashIndex++;
        }
        return 1;
    }

    int dictRehashMilliseconds(long ms) {
        long start = System.currentTimeMillis();
        int rehashes = 0;
        while (rehash(100) != 0) {
            rehashes += 100;
            if (System.currentTimeMillis() - start > ms)
                break;
        }
        return rehashes;
    }

    void rehashStep() {
        if (iterators == 0)
            rehash(1);
    }

    public int add(K key, V value) {
        if (isRehashing())
            rehashStep();
        int index = keyIndex(key);
        if (index == -1)
            return DICT_ERROR;
        DictHashTable<K, V> hashTable = isRehashing() ? hashTables[1] : hashTables[0];
        Entry entry = new Entry();
        entry.key = key;
        //TODO check dup?
        entry.value = value;
        entry.next = hashTable.table[index];
        hashTable.table[index] = entry;
        hashTable.used++;
        return DICT_OK;
    }

    /* Add an element, discarding the old if the key already exists.
     * Return 1 if the key was added from scratch, 0 if there was already an
     * element with such key and dictReplace() just performed a value update
     * operation. */
    public int replace(K key, V value) {
        if (add(key, value) == DICT_OK)
            return 1;
        Entry entry = find(key);
        entry.value = value;
        return 0;
    }

    public Entry find(K key) {
        if (hashTables[0].size == 0)
            return null;
        if (isRehashing())
            rehashStep();
        int hash = dictHashKey(key);
        for (int table = 0; table <= 1; table++) {
            int index = hash & hashTables[table].sizeMask;
            Entry<K, V> entry = hashTables[table].table[index];
            while (entry != null) {
                if (key == entry.key || entry.key.equals(key))
                    return entry;
                entry = entry.next;
            }
            if (!isRehashing())
                return null;
        }
        return null;
    }

    public int delete(K key) {
        if (hashTables[0].size == 0)
            return DICT_ERROR;
        if (isRehashing())
            rehashStep();
        int hash = dictHashKey(key);
        for (int table = 0; table <= 1; table++) {
            int index = hash & hashTables[table].sizeMask;
            Entry entry = hashTables[table].table[index];
            Entry prev = null;
            while (entry != null) {
                if (key == entry.key || entry.key.equals(key)) {
                    if (prev != null) {
                        prev.next = entry.next;

                    } else {
                        hashTables[table].table[index].next = entry.next;
                    }
                    entry.next = null;
                    hashTables[table].used--;
                    return DICT_OK;
                }
                prev = entry;
                entry = entry.next;
            }
            if (!isRehashing()) break;
        }
        return DICT_ERROR;
    }

    /* Returns the index of a free slot that can be populated with
     * an hash entry for the given 'key'.
     * If the key already exists, -1 is returned.
     *
     * Note that if we are in the process of rehashing the hash table, the
     * index is always returned in the context of the second (new) hash table. */
    int keyIndex(K key) {
        if (expandIfNeeded() == DICT_ERROR)
            return -1;
        int hash = dictHashKey(key);
        int index = 0;
        for (int table = 0; table <= 1; table++) {
            index = hash & hashTables[table].sizeMask;
            Entry entry = hashTables[table].table[index];
            while (entry != null) {
                if (key == entry.key || key.equals(entry.key))
                    return -1;
                entry = entry.next;
            }
        }

        return index;
    }

    int expandIfNeeded() {
        if (isRehashing())
            return DICT_OK;
        if (hashTables[0].size == 0)
            return expand(DICT_HT_INITIAL_SIZE);
        if (hashTables[0].used >= hashTables[1].size)
            return expand(hashTables[0].size > hashTables[0].used ?
                    hashTables[0].size : hashTables[0].used * 2);

        return DICT_OK;
    }

    //TODO String to generic
    int dictHashKey(K key) {
        return dictType.hashFunction(key);
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
        public Entry next;
        public K key;
        public V value;

        public Entry getNext() {
            return next;
        }

        public void setNext(Entry next) {
            this.next = next;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    public static class DictHashTable<K, V> {

        Entry<K, V>[] table;
        public int size;
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
