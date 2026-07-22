package dev.panini.execution

import dev.panini.core.Karaka

internal object StateOperationRegistrations {
    val all = listOf(
        operation("01.1143", SanskritVariableInspectAction) {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.SHABDA)
        },
        operation("03.0010", SanskritVariableAssignAction) {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.SHABDA)
        },
        operation("01.0601", SmritiSaveAction) {
            requires(Karaka.KARMAN); triggeredBy(forbiddenAvyayas = setOf("पुनः"))
            effects(ExecutionEffect.WRITE_RESOURCE); returns(ExecutionSamjna.SHABDA)
        },
        operation("01.0601", SmritiLoadAction) {
            requires(Karaka.KARMAN); triggeredBy(requiredAvyayas = setOf("पुनः"))
            effects(ExecutionEffect.READ_RESOURCE); returns(ExecutionSamjna.SHABDA)
        },
    )
}
