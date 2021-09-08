package com.company;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;
        if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
        return val != null ? val.equals(pair.val) : pair.val == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, val);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", val=" + val +
                '}';
    }
}
