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
import dev.panini.actions.approximateNumber

/** Circumference calculation (परिधि) for a circle given radius r. */
object CircumferenceAction : DhatuAction("परिधिसाधनम्", "वृत्तस्य परिधिसाधनम्") {
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
        if (radius < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Radius cannot be negative.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val resultValue = approximateNumber(2.0 * Math.PI * radius.toDouble())
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Circumference is outside the supported numeric range.",
            )
        val resultStr = resultValue.toDisplayText()

        return ExecutionResult.Success(
            resultStr,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Evaluated radius $radius.",
                "Produced circumference $resultStr.",
            ),
            resultValue,
        )
    }
}

/** Hypotenuse calculation (कर्ण) for a triangle given base and height. */
object HypotenuseAction : DhatuAction("कर्णसाधनम्", "त्रिभुजस्य कर्णसाधनम्") {
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
        val squared = runCatching {
            Math.addExact(Math.multiplyExact(bhuja, bhuja), Math.multiplyExact(koti, koti))
        }.getOrElse {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Numeric overflow while calculating the hypotenuse.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val karna = kotlin.math.sqrt(squared.toDouble()).toLong()
        if (karna > 0 && karna > Long.MAX_VALUE / karna || karna * karna != squared) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "The hypotenuse is not an exact integer in the current Sanskrit number model.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val resultStr = context.renderSankhyaResult(karna) ?: "$karna"

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
object AreaAction : DhatuAction("क्षेत्रफलसाधनम्", "क्षेत्रस्य फलसाधनम्") {
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
        if (dim < 0) {
            return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Dimension cannot be negative.",
                listOf("Selected operation ${operation.name}."),
            )
        }
        val resultValue = approximateNumber(Math.PI * dim.toDouble() * dim.toDouble())
            ?: return ExecutionResult.Failure(
                ExecutionError.INVALID_VALUE,
                "Area is outside the supported numeric range.",
            )
        val resultStr = resultValue.toDisplayText()

        return ExecutionResult.Success(
            resultStr,
            operation.name,
            listOf(
                "Selected operation ${operation.name}.",
                "Evaluated dimension $dim.",
                "Produced area $resultStr.",
            ),
            resultValue,
        )
    }
}
