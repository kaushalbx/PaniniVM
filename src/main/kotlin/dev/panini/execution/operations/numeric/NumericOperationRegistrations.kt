package dev.panini.execution

import dev.panini.core.Karaka

internal object NumericOperationRegistrations {
    val all = listOf(
        numeric("01.1153", "सङ्ख्याभागः", "सङ्ख्यानां भागः त्रैराशिकं वा", SanskritFractionAction),
        numeric("01.1046", "सङ्ख्याहरणम्", "सङ्ख्यानां विभाजनम्", SanskritDivisionAction),
        operation("01.0607", "सङ्ख्यामूलम्", "सङ्ख्यायाः वर्गमूलम्", SanskritSquareRootAction) {
            requiresNumbers(); returns(ExecutionSamjna.SANKHYA)
        },
        numeric("01.0863", "सङ्ख्याघातः", "सङ्ख्यायाः घातवर्धनम्", SanskritExponentiationAction),
        numeric("07.0014", "सङ्ख्याशेषः", "सङ्ख्याविभाजनात् शेषः", SanskritModuloAction),
        numeric("07.0007", "सङ्ख्यायोजनम्", "सङ्ख्यानां योगः", SanskritAdditionAction) {
            triggeredBy(forbiddenUpasargas = setOf("वि")); returns(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        },
        numeric("07.0007", "सङ्ख्यावियोगः", "सङ्ख्यानां वियोगः", SanskritSubtractionAction) {
            triggeredBy(requiredUpasargas = setOf("वि")); returns(ExecutionSamjna.SANKHYA, ExecutionSamjna.SHABDA)
        },
        numeric("10.0391", "सङ्ख्यागुणनम्", "सङ्ख्यानां गुणनम्", SanskritMultiplicationAction) {
            triggeredBy(forbiddenUpasargas = setOf("सम्", "सम"))
        },
        operation("10.0391", "सङ्ख्यागणनम्", "पदार्थानां सङ्ख्यानम्", SanskritCountingAction) {
            requires(Karaka.KARMAN, shape = ExpressionShape.COORDINATION); returns(ExecutionSamjna.SANKHYA)
        },
        operation("10.0391", "सङ्ख्यासाम्यम्", "सङ्ख्यानां माध्यमम्", SanskritAverageAction) {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredUpasargas = setOf("सम्")); returns(ExecutionSamjna.SANKHYA)
        },
        operation("07.0013", "सङ्ख्यातुलना", "सङ्ख्यानां तुलना", SanskritComparisonAction) {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(forbiddenAvyayas = setOf("न्यूनतया"))
            returns(ExecutionSamjna.SANKHYA)
        },
        operation("07.0013", "सङ्ख्यान्यूनत्वम्", "सङ्ख्यानां न्यूनत्वम्", SanskritMinAction) {
            requiresNumbers(shape = ExpressionShape.COORDINATION)
            triggeredBy(requiredAvyayas = setOf("न्यूनतया")); returns(ExecutionSamjna.SANKHYA)
        },
    )

    private fun numeric(
        dhatuId: String,
        id: String,
        description: String,
        action: DhatuAction,
        extra: OperationDefinition.() -> Unit = {},
    ): OperationRegistration = operation(dhatuId, id, description, action) {
        requiresNumbers(minimum = 2, shape = ExpressionShape.COORDINATION)
        returns(ExecutionSamjna.SANKHYA)
        extra()
    }
}
