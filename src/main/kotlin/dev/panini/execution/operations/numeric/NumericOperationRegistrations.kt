package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.execution.operations.numeric.SanskritAdditionAction
import dev.panini.execution.operations.numeric.SanskritAverageAction
import dev.panini.execution.operations.numeric.SanskritComparisonAction
import dev.panini.execution.operations.numeric.SanskritDivisionAction
import dev.panini.execution.operations.numeric.SanskritExponentiationAction
import dev.panini.execution.operations.numeric.SanskritFractionAction
import dev.panini.execution.operations.numeric.SanskritMinAction
import dev.panini.execution.operations.numeric.SanskritModuloAction
import dev.panini.execution.operations.numeric.SanskritMultiplicationAction
import dev.panini.execution.operations.numeric.SanskritSquareRootAction
import dev.panini.execution.operations.numeric.SanskritSubtractionAction

internal object NumericOperationRegistrations {
    val all = listOf(
        numeric("01.1153", SanskritFractionAction),
        numeric("01.1046", SanskritDivisionAction),
        operation("01.0607", SanskritSquareRootAction) {
            requiresNumbers(); returns(ExecutionSamjna.SANKHYA)
        },
        numeric("01.0863", SanskritExponentiationAction),
        numeric("07.0014", SanskritModuloAction),
        numeric("07.0007", SanskritAdditionAction) {
            triggeredBy(forbiddenUpasargas = setOf("वि")); returns(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        },
        numeric("07.0007", SanskritSubtractionAction) {
            triggeredBy(requiredUpasargas = setOf("वि")); returns(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        },
        numeric("10.0391", SanskritMultiplicationAction) {
            triggeredBy(forbiddenUpasargas = setOf("सम्", "सम"))
        },
        operation("10.0391", SanskritCountingAction) {
            requires(Karaka.KARMAN, shape = ExpressionShape.COORDINATION); returns(ExecutionSamjna.SANKHYA)
        },
        operation("10.0391", SanskritAverageAction) {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredUpasargas = setOf("सम्")); returns(ExecutionSamjna.SANKHYA)
        },
        operation("07.0013", SanskritComparisonAction) {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(forbiddenAvyayas = setOf("न्यूनतया"))
            returns(ExecutionSamjna.SANKHYA)
        },
        operation("07.0013", SanskritMinAction) {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredAvyayas = setOf("न्यूनतया")); returns(ExecutionSamjna.SANKHYA)
        },
    )

    private fun numeric(
        dhatuId: String,
        action: DhatuAction,
        extra: OperationDefinition.() -> Unit = {},
    ): OperationRegistration = operation(dhatuId, action) {
        requiresNumbers(minimum = 2, shape = ExpressionShape.COORDINATION)
        returns(ExecutionSamjna.SANKHYA)
        extra()
    }
}
