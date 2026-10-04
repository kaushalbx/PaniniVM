package dev.panini.execution

import dev.panini.core.Karaka

/**
 * Backend-neutral meaning of natural PVM argument frames after kāraka
 * resolution. These values contain no spelling or word-order assumptions.
 */
sealed interface NaturalOperation {
    data class StatePlacement(
        val value: ExecutionExpression,
        val destination: ExecutionExpression,
    ) : NaturalOperation

    data class CollectionInsertion(
        val item: ExecutionExpression,
        val collection: ExecutionExpression,
    ) : NaturalOperation

    /** Objects expressed as karman that are gathered into one ordered collection. */
    data class CollectionFormation(
        val items: ExecutionExpression,
    ) : NaturalOperation

    data class IndexedRetrieval(
        val collection: ExecutionExpression,
        val index: ExecutionExpression,
        val result: ExecutionExpression,
    ) : NaturalOperation

    /** A nominative entity exists in a collection expressed in the locative. */
    data class CollectionMembership(
        val item: ExecutionExpression,
        val collection: ExecutionExpression,
    ) : NaturalOperation

    /** One collection is joined with another expressed in the instrumental. */
    data class CollectionConcatenation(
        val collection: ExecutionExpression,
        val companion: ExecutionExpression,
    ) : NaturalOperation

    /** Counts the members of the collection expressed as the object of गण्. */
    data class CollectionCardinality(
        val collection: ExecutionExpression,
    ) : NaturalOperation

    /** A portion of a collection delimited by ordinal source and end positions. */
    data class CollectionSlice(
        val collection: ExecutionExpression,
        val start: ExecutionExpression,
        val endInclusive: ExecutionExpression,
        val result: ExecutionExpression,
    ) : NaturalOperation
}

/** Maps a selected semantic operation and its resolved kārakas to natural meaning. */
object NaturalOperationResolver {
    fun resolve(resolved: ResolvedOperation): NaturalOperation? =
        resolve(resolved.operation, resolved.context)

    fun resolve(operation: DhatuOperation, context: ExecutionContext): NaturalOperation? {
        val bindings = context.bindings
        return when (operation.name) {
            "मूल्यदानम्" -> {
                val value = bindings[Karaka.KARMAN] ?: return null
                val destination = bindings[Karaka.ADHIKARANA] ?: return null
                NaturalOperation.StatePlacement(value, destination)
            }
            "सूचीनिक्षेपणम्" -> {
                val item = bindings[Karaka.KARMAN] ?: return null
                val collection = bindings[Karaka.ADHIKARANA] ?: return null
                NaturalOperation.CollectionInsertion(item, collection)
            }
            "सूचीसङ्ग्रहः" -> {
                val items = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.CollectionFormation(items)
            }
            "सूचीस्थानम्" -> {
                val collection = bindings[Karaka.APADANA] ?: return null
                val index = bindings[Karaka.ADHIKARANA] ?: return null
                val result = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.IndexedRetrieval(collection, index, result)
            }
            "सूच्यस्तित्वम्" -> {
                val item = bindings[Karaka.KARTR] ?: return null
                val collection = bindings[Karaka.ADHIKARANA] ?: return null
                NaturalOperation.CollectionMembership(item, collection)
            }
            "सूचीसंयोगः" -> {
                val collection = bindings[Karaka.KARMAN] ?: return null
                val companion = bindings[Karaka.KARTR] ?: return null
                NaturalOperation.CollectionConcatenation(collection, companion)
            }
            "सूच्याकारः" -> {
                val collection = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.CollectionCardinality(collection)
            }
            "सूचीविभागः" -> {
                val collection = bindings[Karaka.SAMBANDHA] ?: return null
                val start = bindings[Karaka.APADANA] ?: return null
                val end = bindings[Karaka.ADHIKARANA] ?: return null
                val result = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.CollectionSlice(collection, start, end, result)
            }
            else -> null
        }
    }
}
