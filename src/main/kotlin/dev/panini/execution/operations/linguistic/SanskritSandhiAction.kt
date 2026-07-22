package dev.panini.execution.operations.linguistic

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult

import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind

/** Sandhi joining (saṃhitā) over text operands using the Panini Ashtadhyayi DerivationEngine. */
object SanskritSandhiAction : DhatuAction("संहिताकरणम्", "पदानां सन्धियोगः") {
    private val engine by lazy { DerivationEngine() }

    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = requireNotNull(context.bindings[Karaka.KARMAN])
        val operands = context.resolve(expression)
        if (operands.size < 2) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Sandhi joining requires at least 2 text operands.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val result = operands.drop(1).fold(operands.first()) { acc, next -> applySandhi(acc, next) }
        return ExecutionResult.Success(
            result,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Joined ${operands.joinToString(" + ")}.",
                "Produced $result.",
            ),
        )
    }

    private fun applySandhi(left: String, right: String): String {
        val l = left.trim()
        val r = right.trim()
        if (l.isEmpty()) return r
        if (r.isEmpty()) return l

        val initialTerms = listOf(
            DerivationTerm("term-left", l, TermKind.PRATIPADIKA),
            DerivationTerm("term-right", r, TermKind.PRATIPADIKA),
        )
        val initialState = DerivationState(terms = initialTerms)
        val derivationResult = engine.derive(initialState)
        return derivationResult.final.surface
    }
}

