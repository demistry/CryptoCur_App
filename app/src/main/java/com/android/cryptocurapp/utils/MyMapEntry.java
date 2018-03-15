package com.android.cryptocurapp.utils;

import java.util.Map;


/**
 * Created by ILENWABOR DAVID on 12/03/2018.
 */

public final class MyMapEntry<K, V> implements Map.Entry<K, V> {
    private final K key;
    private V value;

    public MyMapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }


    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}
