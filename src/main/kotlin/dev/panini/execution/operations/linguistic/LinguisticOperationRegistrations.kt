package dev.panini.execution

import dev.panini.core.Karaka

internal object LinguisticOperationRegistrations {
    val all = listOf(
        operation("08.0010", "संहिताकरणम्", "पदानां सन्धियोगः", SanskritSandhiAction) {
            requires(Karaka.KARMAN, minimum = 2, shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredAvyayas = setOf("इति")); returns(ExecutionSamjna.SHABDA)
        },
        operation("08.0010", "पदनिष्पत्तिः", "प्रातिपदिकस्य सुबन्तरूपसिद्धिः", SanskritSubantaDerivationAction) {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.SHABDA)
        },
    )
}
