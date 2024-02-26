package com.github.rognlien.cache

interface Cache<K, T> {
    fun put(key: K, value: T)

    fun get(key: K): T?

    fun size(): Int
}
