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

/** Circumference calculation (परिधि) for a circle given radius r. */
object SanskritCircumferenceAction : DhatuAction("परिधिसाधनम्", "वृत्तस्य परिधिसाधनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: return ExecutionResult.Failure(
            ExecutionError.MISSING_KARAKA,
            "Circumference operation requires a radius under Karman.",
            listOf("Selected operation ${operation.name}."),
        )

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Radius is not a valid saṅkhyā value.",
            listOf("Selected operation ${operation.name}."),
        )

        val radius = values.firstOrNull() ?: 1L
        val circum = (2.0 * Math.PI * radius.toDouble()).toLong()
        val resultStr = renderSankhyaResult(circum) ?: "$circum"

        return ExecutionResult.Success(
            resultStr,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Evaluated radius $radius.",
                "Produced circumference $resultStr.",
            ),
            SanskritValue.Sankhya(circum, resultStr),
        )
    }
}

/** Hypotenuse calculation (कर्ण) for a triangle given base and height. */
object SanskritHypotenuseAction : DhatuAction("कर्णसाधनम्", "त्रिभुजस्य कर्णसाधनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: return ExecutionResult.Failure(
            ExecutionError.MISSING_KARAKA,
            "Hypotenuse operation requires side lengths under Karman.",
            listOf("Selected operation ${operation.name}."),
        )

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Sides are not valid saṅkhyā values.",
            listOf("Selected operation ${operation.name}."),
        )

        val bhuja = values.getOrNull(0) ?: 0L
        val koti = values.getOrNull(1) ?: 0L
        val karna = kotlin.math.sqrt((bhuja * bhuja + koti * koti).toDouble()).toLong()
        val resultStr = renderSankhyaResult(karna) ?: "$karna"

        return ExecutionResult.Success(
            resultStr,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Evaluated sides $bhuja and $koti.",
                "Produced hypotenuse $resultStr.",
            ),
            SanskritValue.Sankhya(karna, resultStr),
        )
    }
}

/** Area calculation (क्षेत्रफल) for a field given radius / side. */
object SanskritAreaAction : DhatuAction("क्षेत्रफलसाधनम्", "क्षेत्रस्य फलसाधनम्") {
    override fun execute(context: ExecutionContext, operation: DhatuOperation): ExecutionResult {
        val expression = context.bindings[Karaka.KARMAN] ?: return ExecutionResult.Failure(
            ExecutionError.MISSING_KARAKA,
            "Area operation requires a dimension under Karman.",
            listOf("Selected operation ${operation.name}."),
        )

        val values = context.resolveSankhyaValues(expression) ?: return ExecutionResult.Failure(
            ExecutionError.INVALID_VALUE,
            "Dimension is not a valid saṅkhyā value.",
            listOf("Selected operation ${operation.name}."),
        )

        val dim = values.firstOrNull() ?: 1L
        val area = (Math.PI * dim.toDouble() * dim.toDouble()).toLong()
        val resultStr = renderSankhyaResult(area) ?: "$area"

        return ExecutionResult.Success(
            resultStr,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Evaluated dimension $dim.",
                "Produced area $resultStr.",
            ),
            SanskritValue.Sankhya(area, resultStr),
        )
    }
}
