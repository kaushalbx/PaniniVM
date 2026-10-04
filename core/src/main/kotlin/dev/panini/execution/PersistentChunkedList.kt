package dev.panini.execution

import java.util.RandomAccess

/**
 * Immutable append-oriented list used for chronological discourse state.
 *
 * Appending copies at most one bounded tail chunk plus the short chunk index;
 * older snapshots continue to observe exactly their original elements.
 */
class PersistentChunkedList<out T> private constructor(
    private val chunks: List<List<T>>,
    override val size: Int,
) : AbstractList<T>(), RandomAccess {

    override fun get(index: Int): T {
        checkElementIndex(index, size)
        return chunks[index / CHUNK_SIZE][index % CHUNK_SIZE]
    }

    fun appended(values: Collection<@UnsafeVariance T>): PersistentChunkedList<T> {
        if (values.isEmpty()) return this
        val updated = chunks.toMutableList()
        var remaining = values.toList()
        if (updated.isNotEmpty() && updated.last().size < CHUNK_SIZE) {
            val available = CHUNK_SIZE - updated.last().size
            val tail = remaining.take(available)
            updated[updated.lastIndex] = updated.last() + tail
            remaining = remaining.drop(tail.size)
        }
        remaining.chunked(CHUNK_SIZE).forEach(updated::add)
        return PersistentChunkedList(updated, size + values.size)
    }

    companion object {
        private const val CHUNK_SIZE = 128

        fun <T> from(values: Collection<T>): PersistentChunkedList<T> =
            PersistentChunkedList(values.chunked(CHUNK_SIZE), values.size)

        private fun checkElementIndex(index: Int, size: Int) {
            if (index < 0 || index >= size) {
                throw IndexOutOfBoundsException("Index $index is outside list size $size.")
            }
        }
    }
}

/** Appends without repeatedly copying an already accumulated chronological list. */
fun <T> List<T>.appendedPersistently(values: Collection<T>): List<T> = when {
    values.isEmpty() -> this
    this is PersistentChunkedList<T> -> appended(values)
    else -> PersistentChunkedList.from(this).appended(values)
}
