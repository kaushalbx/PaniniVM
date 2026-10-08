package dev.panini.actions.collection

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.NaturalOperation
import dev.panini.execution.NaturalOperationResolver
import dev.panini.execution.SanskritValue

/** Concatenate two lists (triggered by सृज् / संयोजन / संयोग). */
object ListConcatAction : DhatuAction("सूचीसंयोगः", "सूच्योः परस्पर-संयोजनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val naturalFrame = NaturalOperationResolver.resolve(operation, context)
            as? NaturalOperation.CollectionConcatenation

        // The semantic frame is primary. Direct kāraka lookup retains the old
        // सृज् construction during its compatibility window.
        val karmanExpr = naturalFrame?.collection ?: context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "सूचीसंयोगे संयोज्या सूची कर्मरूपेण अपेक्षिता।"
            )

        val karmanValues = context.resolveCompleteValues(karmanExpr)
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "Collection joining requires a resolved value for every object.")

        // In the natural युज् frame, the second collection is the instrument/
        // co-participant of joining. SAMPRADANA remains a legacy सृज् frame.
        val companionExpr = naturalFrame?.companion
            ?: context.bindings[Karaka.KARTR]
            ?: context.bindings[Karaka.KARANA]
            ?: context.bindings[Karaka.SAMPRADANA]
        val (list1, list2) = if (companionExpr != null) {
            val companionValues = context.resolveCompleteValues(companionExpr)
                ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                    "Collection joining requires a resolved value for every companion.")
            if (naturalFrame != null && listOf(karmanValues, companionValues).any { values ->
                values.size != 1 || values.single().let { it !is SanskritValue.Suchi && it !is SanskritValue.Gana }
            }) return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Natural collection joining requires exactly one collection in each participant role.",
            )
            karmanValues to companionValues
        } else {
            // If SAMPRADANA is absent, check if KARMAN is a Coordination of multiple lists
            if (karmanValues.size >= 2) {
                val first = karmanValues.first()
                val second = karmanValues.drop(1)
                listOf(first) to second
            } else if (karmanValues.size == 1) {
                karmanValues to emptyList()
            } else {
                return ExecutionResult.Failure(
                    ExecutionError.INVALID_VALUE,
                    "सूचीसंयोगे द्वितीया सूची करणरूपेण अपेक्षिता।"
                )
            }
        }

        // Unpack list items (Suchi, Gana, or simple list)
        fun unpack(values: List<SanskritValue>): List<SanskritValue> = values.flatMap { value ->
            when (value) {
                is SanskritValue.Suchi -> value.items
                is SanskritValue.Gana -> value.elements
                else -> listOf(value)
            }
        }

        val items1 = unpack(list1)
        val items2 = unpack(list2)
        val declaredTypes = (list1 + list2).filterIsInstance<SanskritValue.Suchi>()
            .mapNotNull { it.memberType }.distinct()
        if (declaredTypes.size > 1) return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "Cannot concatenate lists with incompatible declared member types.",
        )
        val memberType = declaredTypes.singleOrNull()
        if (memberType != null && (items1 + items2).any { !memberType.accepts(it) }) return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE, "List members must satisfy the declared $memberType type.",
        )
        val combined = SanskritValue.Suchi(items1 + items2, memberType)

        return ExecutionResult.Success(
            combined.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Concatenated list of size ${items1.size} with list of size ${items2.size} -> total size ${items1.size + items2.size}.",
            ),
            combined
        )
    }
}
