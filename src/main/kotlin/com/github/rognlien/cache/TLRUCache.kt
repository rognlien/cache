package com.github.rognlien.cache

import java.time.Duration
import java.time.Instant
import java.time.Instant.now
import java.util.Collections.synchronizedMap

class TLRUCache<K, T> @JvmOverloads constructor (val capacity: Int, private val ttl: Duration = Duration.ofMinutes(10)) : Cache<K, T> {

    private data class Entry<T>(val expires: Instant, val value: T) {
        val expired: Boolean get() = expires.isBefore(now())
    }

    private val cache: MutableMap<K, Entry<T>> = synchronizedMap(object : LinkedHashMap<K, Entry<T>>(capacity, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, TLRUCache.Entry<T>>): Boolean = size > capacity
    })

    override fun put(key: K, value: T) {
        put(key, value, ttl)
    }

    override fun put(key: K, value: T, ttl: Duration) {
        cache[key] = Entry(now().plus(ttl), value)
    }

    override fun get(key: K): T? {
        return cache[key]?.takeUnless { it.expired }?.value
    }

    override fun size(): Int {
        return cache.size
    }
}
