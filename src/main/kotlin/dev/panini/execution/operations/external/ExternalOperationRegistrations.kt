package dev.panini.execution

import dev.panini.core.Karaka

internal object ExternalOperationRegistrations {
    val all = listOf(
        operation("10.0509", BahyaSendAction) {
            requires(Karaka.KARMAN)
            effects(ExecutionEffect.NETWORK, ExecutionEffect.EXECUTE_PROCESS, ExecutionEffect.SEND_MESSAGE)
            returns(ExecutionSamjna.SHABDA)
        },
    )
}
