package dev.panini.execution

import dev.panini.core.Karaka

internal object StateOperationRegistrations {
    val all = listOf(
        operation("01.1143", "मूल्यदर्शनम्", "मूल्यस्य निरीक्षणम्", SanskritVariableInspectAction) {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.SHABDA)
        },
        operation("03.0010", "मूल्यदानम्", "मूल्यस्य संविभाजनम्", SanskritVariableAssignAction) {
            requires(Karaka.KARMAN); returns(ExecutionSamjna.SHABDA)
        },
        operation("01.0601", "स्मृतिरक्षणम्", "स्थितेः स्थायि-सङ्ग्रहे रक्षणम्", SmritiSaveAction) {
            requires(Karaka.KARMAN); triggeredBy(forbiddenAvyayas = setOf("पुनः"))
            effects(ExecutionEffect.WRITE_RESOURCE); returns(ExecutionSamjna.SHABDA)
        },
        operation("01.0601", "स्मृतिपुनर्प्राप्तिः", "स्थायि-सङ्ग्रहात् स्थितेः पुनर्प्राप्तिः", SmritiLoadAction) {
            requires(Karaka.KARMAN); triggeredBy(requiredAvyayas = setOf("पुनः"))
            effects(ExecutionEffect.READ_RESOURCE); returns(ExecutionSamjna.SHABDA)
        },
    )
}
