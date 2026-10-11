package dev.panini.execution

import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/** Weak token ownership: equal data objects are not the same execution token. */
internal class WeakIdentityMap<K : Any, V : Any> {
    private class Key<K : Any>(value: K, queue: ReferenceQueue<K>? = null) : WeakReference<K>(value, queue) {
        private val hash = System.identityHashCode(value)
        override fun hashCode() = hash
        override fun equals(other: Any?): Boolean = this === other ||
            other is Key<*> && get()?.let { it === other.get() } == true
    }

    private val queue = ReferenceQueue<K>()
    private val values = HashMap<Key<K>, V>()
    private fun expunge() {
        while (true) values.remove(queue.poll() ?: return)
    }

    @Synchronized operator fun set(key: K, value: V) {
        expunge()
        values[Key(key, queue)] = value
    }

    @Synchronized operator fun get(key: K): V? {
        expunge()
        return values[Key(key)]
    }

    @Synchronized fun putIfAbsent(key: K, value: V): V? {
        expunge()
        return values.putIfAbsent(Key(key, queue), value)
    }

    @Synchronized fun remove(key: K): V? {
        expunge()
        return values.remove(Key(key))
    }
}
