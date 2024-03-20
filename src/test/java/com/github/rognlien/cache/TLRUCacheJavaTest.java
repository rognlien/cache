package com.github.rognlien.cache;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TLRUCacheJavaTest {

    @Test
    public void  least_recently_used_should_be_unavailable_if_capacity_is_exceeded() {
        final Cache<String, String> cache = new TLRUCache(2);
        cache.put("1", "One");
        cache.put("2", "Two");

        cache.get("1");

        cache.put("3", "Three");

        assertEquals("One", cache.get("1"));
        assertEquals("Three", cache.get("3"));
        assertNull(cache.get("2"));
    }

    @Test
    public void items_should_be_updated() {
        final Cache<String, String> cache = new TLRUCache(2);
        cache.put("1", "One");
        assertEquals("One", cache.get("1"));

        cache.put("1", "One updated");
        assertEquals("One updated", cache.get("1"));
    }

    @Test
    public void fill_beyond_capacity() {
        final Cache<String, String> cache = new TLRUCache(3);
        IntStream.range(1, 5)
                .forEach(it -> cache.put(Integer.toString(it), Integer.toString(it)));

        assertEquals(3, cache.size());
        assertEquals("2", cache.get("2"));
        assertEquals("3", cache.get("3"));
        assertEquals("4", cache.get("4"));
    }

    @Test
    public void expired_items_should_not_be_available() {
        final Cache<String, String> cache = new TLRUCache(3, Duration.ZERO);

        cache.put("1", "One");
        assertNull(cache.get("1"));
    }

    @Test
    public void explicitly_expired_items_should_not_be_available() {
        final Cache<String, String> cache = new TLRUCache(3);

        cache.put("1", "One", Duration.ZERO);
        assertNull(cache.get("1"));
    }
}
