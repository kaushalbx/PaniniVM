package dev.panini.execution

import dev.panini.core.Karaka

internal object ExternalOperationRegistrations {
    val all = listOf(
        operation("10.0509", "बाह्यप्रेषणम्", "बाह्यतन्त्राय सन्देशप्रेषणम्", BahyaSendAction) {
            requires(Karaka.KARMAN)
            effects(ExecutionEffect.NETWORK, ExecutionEffect.EXECUTE_PROCESS, ExecutionEffect.SEND_MESSAGE)
            returns(ExecutionSamjna.SHABDA)
        },
    )
}
