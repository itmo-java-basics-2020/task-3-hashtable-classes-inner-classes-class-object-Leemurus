package ru.itmo.java;

public class HashTable {
    private int size;
    private Entry[] arr;
    private int capacity;
    private double loadFactor;

    HashTable(int initialCapacity) {
        this(initialCapacity, 0.5);
    }

    HashTable(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.capacity = initialCapacity;
        this.arr = new Entry[initialCapacity];
        this.size = 0;
    }

    Object put(Object key, Object value) {
        optimizeTable();

        int index = getIndex(key);
        if (arr[index] == null) {
            index = getFirstCleanPairIndex(key);
        }

        Object prevValue = getValueUsingIndex(index);
        size += (getKeyUsingIndex(index) == null ? 1 : 0);
        arr[index] = new Entry(key, value);
        return prevValue;
    }

    Object get(Object key) {
        return getValueUsingIndex(getIndex(key));
    }

    Object remove(Object key) {
        optimizeTable();

        int index = getIndex(key);
        if (arr[index] == null) {
            return null;
        }

        Object prevValue = getValueUsingIndex(index);
        size -= (arr[index].getKey() == null ? 0 : 1);
        arr[index] = new Entry(null, null);
        return prevValue;
    }

    int size() {
        return size;
    }

    private int getHashIndex(Object object) {
        return (object.hashCode() % capacity + capacity) % capacity;
    }

    private int getNextHashIndex(int index) {
        return (index + 12347) % capacity;
    }

    private void optimizeTable() {
        if (size != (int) (loadFactor * capacity)) {
            return;
        }

        Entry[] oldArr = this.arr;
        this.arr = new Entry[capacity * 2];
        this.capacity = capacity * 2;
        this.size = 0;

        for (Entry entry : oldArr) {
            if (entry != null && entry.getKey() != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private int getIndex(Object key) {
        int index = getHashIndex(key);
        while (arr[index] != null && (arr[index].getKey() == null || !arr[index].getKey().equals(key))) {
            index = getNextHashIndex(index);
        }
        return index;
    }

    private int getFirstCleanPairIndex(Object key) {
        int index = getHashIndex(key);
        while (arr[index] != null && arr[index].getKey() != null) {
            index = getNextHashIndex(index);
        }
        return index;
    }

    private Object getKeyUsingIndex(int index) {
        return (arr[index] == null ? null : arr[index].getKey());
    }

    private Object getValueUsingIndex(int index) {
        return (arr[index] == null ? null : arr[index].getValue());
    }

    private static class Entry {
        private Object key, value;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        Object getKey() {
            return key;
        }

        Object getValue() {
            return value;
        }
    }

}
