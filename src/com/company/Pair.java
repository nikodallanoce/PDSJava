package com.company;

public class Pair<T, V> {

    private final T key;

    public T getKey() {
        return key;
    }

    public V getVal() {
        return val;
    }

    private final V val;

    public Pair(T key, V val) {
        this.key = key;
        this.val = val;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", val=" + val +
                '}';
    }
}
