package dev.panini.execution

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/** Weak token ownership: equal data objects are not the same execution token. */
internal class WeakIdentityMap<K : Any, V : Any>(private val maxEntries: Int? = null) {
    private class Key<K : Any>(value: K, queue: ReferenceQueue<K>? = null) : WeakReference<K>(value, queue) {
        private val hash = System.identityHashCode(value)
        override fun hashCode() = hash
        override fun equals(other: Any?): Boolean = this === other ||
            other is Key<*> && get()?.let { it === other.get() } == true
    }

    private val queue = ReferenceQueue<K>()
    private val values = LinkedHashMap<Key<K>, V>()
    init { require(maxEntries == null || maxEntries > 0) { "A bounded weak identity map needs a positive capacity." } }
    private fun expunge() {
        while (true) values.remove(queue.poll() ?: return)
    }

    @Synchronized operator fun set(key: K, value: V) {
        expunge()
        values[Key(key, queue)] = value
        trimToCapacity()
    }

    @Synchronized operator fun get(key: K): V? {
        expunge()
        return values[Key(key)]
    }

    @Synchronized fun putIfAbsent(key: K, value: V): V? {
        expunge()
        val previous = values.putIfAbsent(Key(key, queue), value)
        trimToCapacity()
        return previous
    }

    /** Computes at most once while preserving reference identity rather than data-class equality. */
    @Synchronized fun getOrPut(key: K, defaultValue: () -> V): V {
        expunge()
        values[Key(key)]?.let { return it }
        return defaultValue().also {
            values[Key(key, queue)] = it
            trimToCapacity()
        }
    }

    @Synchronized fun remove(key: K): V? {
        expunge()
        return values.remove(Key(key))
    }

    @Synchronized internal fun size(): Int {
        expunge()
        return values.size
    }

    private fun trimToCapacity() {
        val capacity = maxEntries ?: return
        while (values.size > capacity) values.remove(values.keys.first())
    }
}
