package dev.panini.actions.state

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritValue
import dev.panini.actions.missingKaraka

/** Variable Assignment & Value Binding (dā / मूल्यदानम्). */
object VariableAssignAction : DhatuAction("मूल्यदानम्", "मूल्यस्य संविभाजनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return missingKaraka(operation, Karaka.KARMAN)
        val values = context.resolveValues(expression)
        if (values.isEmpty()) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Variable assignment requires a value operand in KARMAN.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val resultValue = if (values.size == 1) values.first() else SanskritValue.Suchi(values)
        val textValue = resultValue.toDisplayText()
        return ExecutionResult.Success(
            textValue,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Assigned value '$textValue'.",
                "Produced $textValue.",
            ),
            resultValue
        )
    }
}

/** Variable Inspection & Querying (dṛś / मूल्यदर्शनम्). */
object VariableInspectAction : DhatuAction("मूल्यदर्शनम्", "मूल्यस्य निरीक्षणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return missingKaraka(operation, Karaka.KARMAN)
        val operands = context.resolve(expression)
        val target = operands.firstOrNull() ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Variable inspection requires an operand in KARMAN.",
            listOf("Selected operation ${operation.name}."),
        )
        return ExecutionResult.Success(
            target,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Inspected target '$target'.",
                "Produced $target.",
            ),
        )
    }
}

/** State Persistence Save Action (smṛ / स्मृतिरक्षणम्). */
object SmritiSaveAction : DhatuAction("स्मृतिरक्षणम्", "स्थितेः स्थायि-सङ्ग्रहे रक्षणम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Persistence save requires a key in KARMAN.")
        val operands = context.resolve(expression)
        val key = operands.firstOrNull() ?: "default_session"

        val store = context.stateStore ?: return ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "Persistence save requires a StateStore supplied by the host.",
        )
        val activeContext = SambhashanaContext(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            mentionedEntities = context.variables.mapValues { (_, v) ->
                when (v) {
                    is SanskritValue.Sankhya -> v.word
                    is SanskritValue.Rational -> v.toDisplayText()
                    is SanskritValue.Shabda -> v.text
                    is SanskritValue.Satya -> if (v.boolean) "सत्यम्" else "असत्यम्"
                    is SanskritValue.Gana -> v.toDisplayText()
                    is SanskritValue.Suchi -> v.toDisplayText()
                    is SanskritValue.Lopa -> v.toDisplayText()
                    is SanskritValue.Range -> v.toDisplayText()
                }
            },
        )
        store.save(key, activeContext)

        return ExecutionResult.Success(
            key,
            operation.name,
            listOf("Selected operation ${operation.name}.", "Saved context under session key '$key'."),
        )
    }
}

/** State Persistence Load Action (smṛ / स्मृतिपुनर्प्राप्तिः). */
object SmritiLoadAction : DhatuAction("स्मृतिपुनर्प्राप्तिः", "स्थायि-सङ्ग्रहात् स्थितेः पुनर्प्राप्तिः") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN]
            ?: return ExecutionResult.Failure(ExecutionError.INVALID_VALUE, "Persistence load requires a key in KARMAN.")
        val operands = context.resolve(expression)
        val key = operands.firstOrNull() ?: "default_session"

        val store = context.stateStore ?: return ExecutionResult.Failure(
            ExecutionError.ACTION_FAILED,
            "Persistence load requires a StateStore supplied by the host.",
        )
        val loaded = store.load(key) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "No saved context state found for session key '$key'.",
            listOf("Selected operation ${operation.name}."),
        )

        val restoredValues = loaded.mentionedEntities.map { (name, value) ->
            SanskritValue.Shabda("$name=$value")
        }
        val restored = SanskritValue.Suchi(restoredValues)
        return ExecutionResult.Success(
            restored.toDisplayText(),
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Loaded session context from '$key'.",
                "Restored ${restoredValues.size} persisted values.",
            ),
            restored,
        )
    }
}
