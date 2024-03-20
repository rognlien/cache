package com.github.rognlien.cache

import java.time.Duration

interface Cache<K, T> {
    fun put(key: K, value: T)
    fun put(key: K, value: T, ttl: Duration)

    fun get(key: K): T?

    fun size(): Int
}
