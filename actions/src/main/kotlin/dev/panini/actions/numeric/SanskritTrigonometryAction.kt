package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.DhatuAction
import dev.panini.execution.DhatuOperation
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionError
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.renderSankhyaResult
import dev.panini.execution.resolveSankhyaValues

/** Trigonometric functions (ज्या, कोटिज्या, स्पर्शज्या, उत्क्रमज्या) over a degree argument. */
object SanskritTrigonometryAction : DhatuAction("ज्यासाधनम्", "कोणस्य ज्या-कोटिज्या-साधनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: return ExecutionResult.Failure(
            ExecutionError.MISSING_KARAKA,
            "Trigonometric operation requires a degree angle argument under Karman.",
            listOf("Selected operation ${operation.name}."),
        )

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "The argument is not a valid saṅkhyā value.",
            listOf("Selected operation ${operation.name}."),
        )

        val angle = values.firstOrNull() ?: 0L
        val rad = angle.toDouble() * Math.PI / 180.0

        val valDouble = when {
            operation.name.contains("कोटिज्या") -> kotlin.math.cos(rad)
            operation.name.contains("स्पर्शज्या") -> kotlin.math.tan(rad)
            operation.name.contains("उत्क्रमज्या") -> 1.0 - kotlin.math.cos(rad)
            else -> kotlin.math.sin(rad)
        }

        val resultLong = valDouble.toLong()
        val resultStr = renderSankhyaResult(resultLong) ?: "$resultLong"

        return ExecutionResult.Success(
            resultStr,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Evaluated angle $angle degrees.",
                "Produced $resultStr.",
            ),
            SanskritValue.Sankhya(resultLong, resultStr),
        )
    }
}
