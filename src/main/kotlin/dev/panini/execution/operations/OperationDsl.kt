package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara

internal data class OperationRegistration(val dhatuId: String, val operation: DhatuOperation)

internal fun operation(
    dhatuId: String,
    action: DhatuAction,
    definition: OperationDefinition.() -> Unit = {},
): OperationRegistration {
    val builder = OperationDefinition().apply(definition)
    return OperationRegistration(
        dhatuId,
        DhatuOperation(
            signature = OperationSignature(builder.requirements, builder.optionalKarakas),
            action = action,
            trigger = builder.trigger,
            effects = builder.effects,
            resultSamjnas = builder.resultSamjnas,
        ),
    )
}

internal class OperationDefinition {
    internal val requirements = mutableListOf<KarakaRequirement>()
    internal val optionalKarakas = mutableSetOf<Karaka>()
    internal var trigger = OperationTrigger()
    internal var effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE)
    internal var resultSamjnas: Set<ExecutionSamjna> = emptySet()

    fun requires(
        karaka: Karaka,
        minimum: Int = 1,
        maximum: Int? = null,
        shape: ExpressionShape? = null,
        vararg samjnas: ExecutionSamjna,
    ) {
        requirements += KarakaRequirement(karaka, minimum, maximum, shape, samjnas.toSet())
    }

    fun requiresNumbers(minimum: Int = 1, shape: ExpressionShape? = null) {
        requires(Karaka.KARMAN, minimum, shape = shape, samjnas = arrayOf(ExecutionSamjna.SANKHYA))
    }

    fun optional(vararg karakas: Karaka) {
        optionalKarakas += karakas
    }

    fun triggeredBy(
        requiredUpasargas: Set<String> = emptySet(),
        forbiddenUpasargas: Set<String> = emptySet(),
        requiredSanadi: Set<String> = emptySet(),
        requiredAvyayas: Set<String> = emptySet(),
        forbiddenAvyayas: Set<String> = emptySet(),
        allowedLakaras: Set<Lakara> = emptySet(),
    ) {
        trigger = OperationTrigger(
            requiredUpasargas,
            forbiddenUpasargas,
            requiredSanadi,
            requiredAvyayas,
            forbiddenAvyayas,
            allowedLakaras,
        )
    }

    fun effects(vararg values: ExecutionEffect) {
        effects = values.toSet()
    }

    fun returns(vararg samjnas: ExecutionSamjna) {
        resultSamjnas = samjnas.toSet()
    }
}

internal fun registrations(vararg groups: List<OperationRegistration>): Map<String, List<DhatuOperation>> =
    groups.asSequence().flatten().groupBy({ it.dhatuId }, { it.operation })
