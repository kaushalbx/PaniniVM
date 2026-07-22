package dev.panini.execution

import dev.panini.core.Karaka

/** Variable Assignment & Value Binding (dā / मूल्यदानम्). */
object SanskritVariableAssignAction : DhatuAction {
    const val ID = "value.assign"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val value = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Variable assignment requires a value operand in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        return ExecutionResult.Success(
            value,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Assigned value '$value'.",
                "Produced $value.",
            ),
        )
    }
}

/** Variable Inspection & Querying (dṛś / मूल्यदर्शनम्). */
object SanskritVariableInspectAction : DhatuAction {
    const val ID = "value.inspect"

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        val target = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Variable inspection requires an operand in KARMAN.",
            listOf("Selected operation ${operation.id}."),
        )
        return ExecutionResult.Success(
            target,
            operation.id,
            listOf(
                "Selected operation ${operation.id}.",
                "Inspected target '$target'.",
                "Produced $target.",
            ),
        )
    }
}

/** State Persistence Save Action (smṛ / स्मृतिरक्षणम्). */
object SmritiSaveAction : DhatuAction {
    const val ID = "स्मृतिरक्षणम्"

    var globalStore: dev.panini.execution.persistence.StateStore? = null

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Persistence save requires a key in KARMAN.")
        val operands = context.resolve(expression)
        val key = operands.firstOrNull() ?: "default_session"

        val store = globalStore ?: dev.panini.execution.persistence.FileStateStore(java.io.File(System.getProperty("user.home"), ".paninivm/sessions"))
        val activeContext = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            mentionedEntities = context.variables.mapValues { (_, v) ->
                when (v) {
                    is SanskritValue.Sankhya -> v.word
                    is SanskritValue.Shabda -> v.text
                    is SanskritValue.Satya -> if (v.boolean) "सत्यम्" else "असत्यम्"
                    is SanskritValue.Gana -> v.toDisplayText()
                }
            },
        )
        store.save(key, activeContext)

        return ExecutionResult.Success(
            key,
            operation.id,
            listOf("Selected operation ${operation.id}.", "Saved context under session key '$key'."),
        )
    }
}

/** State Persistence Load Action (smṛ / स्मृतिपुनर्प्राप्तिः). */
object SmritiLoadAction : DhatuAction {
    const val ID = "स्मृतिपुनर्प्राप्तिः"

    var globalStore: dev.panini.execution.persistence.StateStore? = null

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Persistence load requires a key in KARMAN.")
        val operands = context.resolve(expression)
        val key = operands.firstOrNull() ?: "default_session"

        val store = globalStore ?: dev.panini.execution.persistence.FileStateStore(java.io.File(System.getProperty("user.home"), ".paninivm/sessions"))
        val loaded = store.load(key) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "No saved context state found for session key '$key'.",
            listOf("Selected operation ${operation.id}."),
        )

        return ExecutionResult.Success(
            key,
            operation.id,
            listOf("Selected operation ${operation.id}.", "Loaded session context from '$key'."),
        )
    }
}

