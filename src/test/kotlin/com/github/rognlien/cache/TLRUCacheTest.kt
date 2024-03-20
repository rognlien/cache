package com.github.rognlien.cache


import java.time.Duration
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class TLRUCacheTest {

    @Test
    fun `Least recently used should be unavailable if capacity is exceeded`() {
        val cache = TLRUCache<String, String>(2)
        cache.put("1", "One")
        cache.put("2", "Two")

        cache.get("1")

        cache.put("3", "Three")

        assertEquals("One", cache.get("1"))
        assertEquals("Three", cache.get("3"))
        assertNull(cache.get("2"))
    }

    @Test
    fun `Items should be updated`() {
        val cache = TLRUCache<String, String>(2)
        cache.put("1", "One")
        assertEquals("One", cache.get("1"))

        cache.put("1", "One updated")
        assertEquals("One updated", cache.get("1"))
    }

    @Test
    fun `Fill beyond capacity`() {
        val cache = TLRUCache<String, String>(3)

        (1..4).forEach {
            cache.put(it.toString(), it.toString())
        }

        assertEquals(3, cache.size())
        assertEquals("2", cache.get("2"))
        assertEquals("3", cache.get("3"))
        assertEquals("4", cache.get("4"))
    }

    @Test
    fun `Expired items should not be available`() {
        val cache = TLRUCache<String, String>(3, Duration.ZERO)

        cache.put("1", "One")
        assertNull(cache.get("1"))
    }
}
