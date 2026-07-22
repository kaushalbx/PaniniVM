package dev.panini.execution.operations.linguistic

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionSamjna
import dev.panini.execution.ExpressionShape
import dev.panini.execution.operation

internal object LinguisticOperationRegistrations {
    val all = listOf(
        operation("08.0010", SanskritSandhiAction) {
            requires(Karaka.KARMAN, minimum = 2, shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredAvyayas = setOf("इति")); returns(ExecutionSamjna.SHABDA)
        },
        operation("08.0010", SanskritSubantaDerivationAction) {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.SHABDA)
        },
    )
}
