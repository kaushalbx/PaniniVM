package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.shiksha.Samjna

fun DhatuAction.op(definition: OperationDefinition.() -> Unit = {}): DhatuOperation {
    val builder = OperationDefinition().apply(definition)
    return DhatuOperation(
        signature = OperationSignature(builder.requirements, builder.optionalKarakas),
        action = this,
        trigger = builder.trigger,
        effects = builder.effects,
        resultSamjnas = builder.resultSamjnas,
        resultBindingKaraka = builder.resultBindingKaraka,
    )
}

fun DhatuAction.numericOp(
    minimum: Int = 2,
    definition: OperationDefinition.() -> Unit = {},
): DhatuOperation = op {
    requiresNumbers(minimum = minimum, shape = ExpressionShape.COORDINATION)
    returns(Samjna.SANKHYA)
    definition()
}

class OperationDefinition {
    internal val requirements = mutableListOf<KarakaRequirement>()
    internal val optionalKarakas = mutableSetOf<Karaka>()
    internal var trigger = OperationTrigger()
    internal var effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE)
    internal var resultSamjnas: Set<Samjna> = emptySet()
    internal var resultBindingKaraka: Karaka? = null

    fun requires(
        karaka: Karaka,
        minimum: Int = 1,
        maximum: Int? = null,
        shape: ExpressionShape? = null,
        vararg samjnas: Samjna,
    ) {
        requirements += KarakaRequirement(karaka, minimum, maximum, shape, samjnas.toSet())
    }

    fun requiresNumbers(minimum: Int = 1, shape: ExpressionShape? = null) {
        requires(Karaka.KARMAN, minimum, shape = shape, samjnas = arrayOf(Samjna.SANKHYA))
    }

    fun optional(vararg karakas: Karaka) {
        optionalKarakas += karakas
    }

    fun triggeredBy(
        requiredUpasargas: Set<String> = emptySet(),
        forbiddenUpasargas: Set<String> = emptySet(),
        requiredSanadi: Set<String> = emptySet(),
        forbiddenSanadi: Set<String> = emptySet(),
        requiredAvyayas: Set<String> = emptySet(),
        forbiddenAvyayas: Set<String> = emptySet(),
        allowedLakaras: Set<Lakara> = emptySet(),
    ) {
        trigger = OperationTrigger(
            requiredUpasargas,
            forbiddenUpasargas,
            requiredSanadi,
            forbiddenSanadi,
            requiredAvyayas,
            forbiddenAvyayas,
            allowedLakaras,
        )
    }

    fun effects(vararg values: ExecutionEffect) {
        effects = values.toSet()
    }

    fun returns(vararg samjnas: Samjna) {
        resultSamjnas = samjnas.toSet()
    }

    /** Store the result under the literal name supplied by this kāraka. */
    fun bindsResultTo(karaka: Karaka) {
        resultBindingKaraka = karaka
    }
}
