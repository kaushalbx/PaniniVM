package dev.panini.actions.collection

import dev.panini.execution.*

/** Join the coordinated collection objects of sam-yuj. */
object ListConcatAction : DhatuAction("सूचीसंयोगः", "सूचीनां परस्पर-संयोजनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val frame = NaturalOperationResolver.resolve(operation, context) as? NaturalOperation.CollectionConcatenation
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                "सूचीसंयोगे द्वे वा अधिकाः समुच्चिताः सूच्यः कर्मरूपेण अपेक्षिताः।")
        val collections = frame.collections.map { expression ->
            val values = context.resolveCompleteValues(expression)
                ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Every collection object must be resolved.")
            values.singleOrNull()?.takeIf { it is SanskritValue.Suchi || it is SanskritValue.Gana }
                ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
                    "Each coordinated object must denote exactly one collection.")
        }
        val declaredTypes = collections.filterIsInstance<SanskritValue.Suchi>().mapNotNull { it.memberType }.distinct()
        if (declaredTypes.size > 1) return ExecutionResult.Failure(ExecutionError.INVALID_VALUE,
            "Cannot concatenate lists with incompatible declared member types.")
        val items = collections.flatMap { value -> when (value) {
            is SanskritValue.Suchi -> value.items
            is SanskritValue.Gana -> value.elements
            else -> error("Validated collection required")
        } }
        val memberType = declaredTypes.singleOrNull()
        if (memberType != null && items.any { !memberType.accepts(it) })
            return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "List members must satisfy the declared type.")
        val combined = SanskritValue.Suchi(items, memberType)
        return ExecutionResult.Success(combined.toDisplayText(), operation.name,
            listOf("Joined ${collections.size} coordinated collections."), combined)
    }
}
