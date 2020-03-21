package ru.itmo.java;

public class HashTable {
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

    private int capacity, size;
    private Entry[] arr;
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

    private int getHashIndex(Object object) {
        return (object.hashCode() % capacity + capacity) % capacity;
    }

    private int getNextHashIndex(int index) {
        return (index + 12347) % capacity;
    }

    private void changeTableSize(int size) {
        Entry[] oldArr = arr;
        this.arr = new Entry[size];
        this.capacity = size;
        this.size = 0;

        for (Entry entry : oldArr) {
            if (entry != null && entry.getKey() != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private void optimizeTable() {
        if (size == (int) (loadFactor * capacity)) {
            changeTableSize(capacity * 2);
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

    private Object putInIndex(int index, Object key, Object value) {
        if (arr[index] == null) {
            index = getFirstCleanPairIndex(key);
        }

        Object prevValue = getValueUsingIndex(index);
        size += (getKeyUsingIndex(index) == null ? 1 : 0);
        arr[index] = new Entry(key, value);
        return prevValue;
    }

    private Object removeInIndex(int index) {
        if (arr[index] == null) {
            return null;
        }

        Object prevValue = getValueUsingIndex(index);
        size -= (arr[index].getKey() == null ? 0 : 1);
        arr[index] = new Entry(null, null);
        return prevValue;
    }

    private Object getKeyUsingIndex(int index) {
        return (arr[index] == null ? null : arr[index].getKey());
    }

    private Object getValueUsingIndex(int index) {
        return (arr[index] == null ? null : arr[index].getValue());
    }

    Object put(Object key, Object value) {
        optimizeTable();
        return putInIndex(getIndex(key), key, value);
    }

    Object get(Object key) {
        return getValueUsingIndex(getIndex(key));
    }

    Object remove(Object key) {
        optimizeTable();
        return removeInIndex(getIndex(key));
    }

    int size() {
        return size;
    }
}
