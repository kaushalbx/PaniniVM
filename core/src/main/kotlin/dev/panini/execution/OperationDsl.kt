package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.shiksha.Samjna

fun DhatuAction.op(definition: OperationDefinition.() -> Unit = {}): DhatuOperation {
    val builder = OperationDefinition().apply(definition)
    return DhatuOperation(
        signature = OperationSignature(builder.requirements, builder.optionalKarakas, builder.forbiddenKarakas,
            builder.copularPredicates),
        action = this,
        trigger = builder.trigger,
        effects = builder.effects,
        resultSamjnas = builder.resultSamjnas,
        resultBindingKaraka = builder.resultBindingKaraka,
        controlSignal = builder.controlSignal,
    )
}

fun DhatuAction.numericOp(
    minimum: Int = 2,
    definition: OperationDefinition.() -> Unit = {},
): DhatuOperation = op {
    requiresNumbers(minimum = minimum, shape = ExpressionShape.COORDINATION)
    bindsResultTo(Karaka.SAMPRADANA)
    returns(Samjna.SANKHYA)
    definition()
}

class OperationDefinition {
    internal val requirements = mutableListOf<KarakaRequirement>()
    internal val optionalKarakas = mutableSetOf<Karaka>()
    internal val forbiddenKarakas = mutableSetOf<Karaka>()
    internal val copularPredicates = mutableSetOf<CopularPredicate>()
    internal var trigger = OperationTrigger()
    internal var effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE)
    internal var resultSamjnas: Set<Samjna> = emptySet()
    internal var resultBindingKaraka: Karaka? = null
    internal var controlSignal: ExecutionControlSignal? = null

    fun signals(signal: ExecutionControlSignal) {
        controlSignal = signal
    }

    fun requires(
        karaka: Karaka,
        minimum: Int = 1,
        maximum: Int? = null,
        shape: ExpressionShape? = null,
        vararg samjnas: Samjna,
        valueType: OperationValueType? = null,
    ) {
        requirements += KarakaRequirement(karaka, minimum, maximum, shape, samjnas.toSet(), valueType)
    }

    fun requiresNumbers(minimum: Int = 1, shape: ExpressionShape? = null) {
        requires(Karaka.KARMAN, minimum, shape = shape, samjnas = arrayOf(Samjna.SANKHYA),
            valueType = OperationValueType.NUMBER)
    }

    fun optional(vararg karakas: Karaka) {
        optionalKarakas += karakas
    }

    fun forbids(vararg karakas: Karaka) {
        forbiddenKarakas += karakas
    }

    fun predicates(vararg predicates: CopularPredicate) {
        copularPredicates += predicates
    }

    fun triggeredBy(
        requiredUpasargas: Set<String> = emptySet(),
        forbiddenUpasargas: Set<String> = emptySet(),
        requiredSanadi: Set<String> = emptySet(),
        forbiddenSanadi: Set<String> = emptySet(),
        requiredAvyayas: Set<String> = emptySet(),
        forbiddenAvyayas: Set<String> = emptySet(),
        allowedLakaras: Set<Lakara> = emptySet(),
        requiredUpasargaIdentities: Set<dev.panini.core.Upasarga> = emptySet(),
        forbiddenUpasargaIdentities: Set<dev.panini.core.Upasarga> = emptySet(),
        requiredSanadiIdentities: Set<dev.panini.core.SanadiAffix> = emptySet(),
        forbiddenSanadiIdentities: Set<dev.panini.core.SanadiAffix> = emptySet(),
    ) {
        trigger = OperationTrigger(
            requiredUpasargas + requiredUpasargaIdentities.map { it.upadesha },
            forbiddenUpasargas + forbiddenUpasargaIdentities.map { it.upadesha },
            requiredSanadi + requiredSanadiIdentities.map { it.upadesha },
            forbiddenSanadi + forbiddenSanadiIdentities.map { it.upadesha },
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
