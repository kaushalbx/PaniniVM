package dev.panini.execution

import dev.panini.core.Karaka

/**
 * Backend-neutral meaning of natural PVM argument frames after kāraka
 * resolution. These values contain no spelling or word-order assumptions.
 */
sealed interface NaturalOperation {
    data class CollectionSummation(val collection: ExecutionExpression) : NaturalOperation
    /** The selected member of a collection expressed in the genitive. */
    data class CollectionExtraction(
        val collection: ExecutionExpression,
        val member: ExecutionExpression,
    ) : NaturalOperation
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
        val items: ExecutionExpression?,
    ) : NaturalOperation

    data class IndexedRetrieval(
        val collection: ExecutionExpression,
        val index: ExecutionExpression,
        val result: ExecutionExpression,
    ) : NaturalOperation

    /** Returns a persistent copy with one one-based position replaced. */
    data class IndexedReplacement(
        val collection: ExecutionExpression,
        val index: ExecutionExpression,
        val replacement: ExecutionExpression,
    ) : NaturalOperation

    /** Returns a persistent copy with the members at two one-based positions exchanged. */
    data class IndexedExchange(
        val collection: ExecutionExpression,
        val firstIndex: ExecutionExpression,
        val secondIndex: ExecutionExpression,
    ) : NaturalOperation

    /** Returns a persistent copy with a member inserted at a one-based position. */
    data class IndexedInsertion(
        val collection: ExecutionExpression,
        val index: ExecutionExpression,
        val inserted: ExecutionExpression,
    ) : NaturalOperation

    /** Returns a persistent copy without the member at a one-based position. */
    data class IndexedRemoval(
        val collection: ExecutionExpression,
        val index: ExecutionExpression,
    ) : NaturalOperation

    /** Returns a structured shortened-list/removed-value result. */
    data class FinalRemoval(
        val collection: ExecutionExpression,
    ) : NaturalOperation

    /** Finds the first one-based position of an item in a collection. */
    data class CollectionPosition(
        val collection: ExecutionExpression,
        val item: ExecutionExpression,
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
            "सङ्ख्यायोजनम्" -> {
                val member = bindings[Karaka.KARMAN] as? ExecutionExpression.Pada ?: return null
                if (member.memberSelection != CollectionMemberSelection.NUMBERS) return null
                val whole = bindings[Karaka.SAMBANDHA] ?: return null
                NaturalOperation.CollectionSummation(whole)
            }
            "सूच्युद्धरणम्" -> {
                val collection = bindings[Karaka.SAMBANDHA] ?: return null
                val member = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.CollectionExtraction(collection, member)
            }
            "मूल्यदानम्" -> {
                val value = bindings[Karaka.KARMAN] ?: return null
                val destination = bindings[Karaka.ADHIKARANA] ?: return null
                NaturalOperation.StatePlacement(value, destination)
            }
            "सूचीप्रतिस्थापनम्" -> {
                val collection = bindings[Karaka.SAMBANDHA] ?: return null
                val index = bindings[Karaka.ADHIKARANA] ?: return null
                val replacement = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.IndexedReplacement(collection, index, replacement)
            }
            "सूचीस्थानविनिमयः" -> {
                val collection = bindings[Karaka.SAMBANDHA] ?: return null
                val positions = bindings[Karaka.ADHIKARANA] as? ExecutionExpression.Coordination ?: return null
                if (positions.members.size != 2) return null
                NaturalOperation.IndexedExchange(collection, positions.members[0], positions.members[1])
            }
            "सूचीस्थाननिक्षेपणम्" -> {
                val collection = bindings[Karaka.SAMBANDHA] ?: return null
                val index = bindings[Karaka.ADHIKARANA] ?: return null
                val inserted = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.IndexedInsertion(collection, index, inserted)
            }
            "सूचीस्थाननिष्कासनम्" -> {
                val collection = bindings[Karaka.SAMBANDHA] ?: return null
                val index = bindings[Karaka.ADHIKARANA] ?: return null
                NaturalOperation.IndexedRemoval(collection, index)
            }
            "सूच्यन्त्यनिष्कासनम्" -> {
                val collection = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.FinalRemoval(collection)
            }
            "सूचीनिक्षेपणम्" -> {
                val item = bindings[Karaka.KARMAN] ?: return null
                val collection = bindings[Karaka.ADHIKARANA] ?: return null
                NaturalOperation.CollectionInsertion(item, collection)
            }
            "सूचीसङ्ग्रहः" -> {
                val items = bindings[Karaka.KARMAN]
                NaturalOperation.CollectionFormation(items)
            }
            "सूचीस्थानम्" -> {
                val collection = bindings[Karaka.APADANA] ?: return null
                val index = bindings[Karaka.ADHIKARANA] ?: return null
                val result = bindings[Karaka.KARMAN] ?: return null
                NaturalOperation.IndexedRetrieval(collection, index, result)
            }
            "सूचीस्थानान्वेषणम्" -> {
                val collection = bindings[Karaka.ADHIKARANA] ?: return null
                val item = bindings[Karaka.SAMBANDHA] ?: return null
                NaturalOperation.CollectionPosition(collection, item)
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
