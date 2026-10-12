package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.core.Upasarga
import dev.panini.core.SanadiAffix
import dev.panini.shiksha.Karmatva
import dev.panini.shiksha.Samjna

data class GrammaticalFeatures(
    val upasargas: Set<String> = emptySet(),
    val sanadi: Set<String> = emptySet(),
    val avyayas: Set<String> = emptySet(),
    val lakara: Lakara? = null,
) {
    val upasargaIdentities: Set<Upasarga> get() = upasargas.mapNotNull(Upasarga::fromUpadesha).toSet()
    val sanadiIdentities: Set<SanadiAffix> get() = sanadi.mapNotNull(SanadiAffix::fromUpadesha).toSet()
}

data class OperationTrigger(
    val requiredUpasargas: Set<String> = emptySet(),
    val forbiddenUpasargas: Set<String> = emptySet(),
    val requiredSanadi: Set<String> = emptySet(),
    val forbiddenSanadi: Set<String> = emptySet(),
    val requiredAvyayas: Set<String> = emptySet(),
    val forbiddenAvyayas: Set<String> = emptySet(),
    val allowedLakaras: Set<Lakara> = emptySet(),
) {
    val requiredUpasargaIdentities: Set<Upasarga> get() = requiredUpasargas.mapNotNull(Upasarga::fromUpadesha).toSet()
    val forbiddenUpasargaIdentities: Set<Upasarga> get() = forbiddenUpasargas.mapNotNull(Upasarga::fromUpadesha).toSet()
    val requiredSanadiIdentities: Set<SanadiAffix> get() = requiredSanadi.mapNotNull(SanadiAffix::fromUpadesha).toSet()
    val forbiddenSanadiIdentities: Set<SanadiAffix> get() = forbiddenSanadi.mapNotNull(SanadiAffix::fromUpadesha).toSet()

    init {
        require(requiredUpasargas.intersect(forbiddenUpasargas).isEmpty() &&
            requiredUpasargaIdentities.intersect(forbiddenUpasargaIdentities).isEmpty()) {
            "An operation trigger cannot require and forbid the same upasarga."
        }
        require(requiredAvyayas.intersect(forbiddenAvyayas).isEmpty()) {
            "An operation trigger cannot require and forbid the same avyaya."
        }
        require(requiredSanadi.intersect(forbiddenSanadi).isEmpty() &&
            requiredSanadiIdentities.intersect(forbiddenSanadiIdentities).isEmpty()) {
            "An operation trigger cannot require and forbid the same sanādi affix."
        }
    }

    fun matches(features: GrammaticalFeatures): Boolean =
        features.upasargaIdentities.containsAll(requiredUpasargaIdentities) &&
            features.upasargaIdentities.none { it in forbiddenUpasargaIdentities } &&
            requiredUpasargas.filter { Upasarga.fromUpadesha(it) == null }.all { it in features.upasargas } &&
            forbiddenUpasargas.filter { Upasarga.fromUpadesha(it) == null }.none { it in features.upasargas } &&
            features.sanadiIdentities.containsAll(requiredSanadiIdentities) &&
            features.sanadiIdentities.none { it in forbiddenSanadiIdentities } &&
            requiredSanadi.filter { SanadiAffix.fromUpadesha(it) == null }.all { it in features.sanadi } &&
            forbiddenSanadi.filter { SanadiAffix.fromUpadesha(it) == null }.none { it in features.sanadi } &&
            features.avyayas.containsAll(requiredAvyayas) &&
            features.avyayas.none { it in forbiddenAvyayas } &&
            (allowedLakaras.isEmpty() || features.lakara in allowedLakaras)
}

enum class ExpressionShape { LITERAL, COORDINATION, REFERENCE }

/** Runtime payload eligibility, independent of inherited grammatical saṃjñās. */
enum class OperationValueType {
    NUMBER, INTEGER, TEXT, BOOLEAN, COLLECTION, RECORD, RANGE;

    fun accepts(value: SanskritValue): Boolean = when (this) {
        NUMBER -> value is SanskritValue.Sankhya ||
            (value is SanskritValue.Rational && value.denominator != 0L)
        INTEGER -> value is SanskritValue.Sankhya
        TEXT -> value is SanskritValue.Shabda
        BOOLEAN -> value is SanskritValue.Satya
        COLLECTION -> value is SanskritValue.Suchi || value is SanskritValue.Gana
        RECORD -> value is SanskritValue.Rupa
        RANGE -> value is SanskritValue.Range
    }
}

/** Declarative requirement for one kāraka in an operation signature. */
data class KarakaRequirement(
    val karaka: Karaka,
    val minimumMembers: Int = 1,
    val maximumMembers: Int? = null,
    val shape: ExpressionShape? = null,
    val memberSamjnas: Set<Samjna> = emptySet(),
    val valueType: OperationValueType? = null,
) {
    init {
        require(minimumMembers >= 1) { "Minimum member count must be positive." }
        require(maximumMembers == null || maximumMembers >= minimumMembers) {
            "Maximum member count cannot be smaller than the minimum."
        }
    }

    internal val specificity: Int
        get() = 1 + (if (shape == null) 0 else 1) + memberSamjnas.size +
            (if (minimumMembers == 1 && maximumMembers == null) 0 else 1) +
            (if (valueType == null) 0 else 1)
}

data class OperationSignature(
    val requirements: List<KarakaRequirement>,
    val optionalKarakas: Set<Karaka> = emptySet(),
    val forbiddenKarakas: Set<Karaka> = emptySet(),
    /** A recognized copular predicate constrains overload selection, not host payloads. */
    val copularPredicates: Set<CopularPredicate> = emptySet(),
) {
    init {
        require(requirements.none { it.karaka in forbiddenKarakas } &&
            optionalKarakas.intersect(forbiddenKarakas).isEmpty()) {
            "An accepted kāraka cannot also be forbidden."
        }
        require(requirements.map { it.karaka }.distinct().size == requirements.size) {
            "An operation signature cannot repeat a kāraka requirement."
        }
        require(requirements.none { it.karaka in optionalKarakas }) {
            "A kāraka cannot be both required and optional."
        }
    }

    internal val specificity: Int get() = requirements.sumOf { it.specificity }

    companion object {
        fun fromKarmatva(
            karmatva: Karmatva,
            optionalKarakas: Set<Karaka> = emptySet(),
        ): OperationSignature {
            val reqs = mutableListOf<KarakaRequirement>()
            reqs.add(KarakaRequirement(Karaka.KARTR))
            when (karmatva) {
                Karmatva.AKARMAKA -> Unit
                Karmatva.SAKARMAKA -> {
                    reqs.add(KarakaRequirement(Karaka.KARMAN))
                }
                Karmatva.DVIKARMAKA -> {
                    reqs.add(KarakaRequirement(Karaka.KARMAN, minimumMembers = 2))
                }
            }
            return OperationSignature(reqs, optionalKarakas)
        }
    }
}

/** One overload of an executable dhātu, selected by its declarative signature. */
data class DhatuOperation(
    val signature: OperationSignature,
    val action: DhatuAction,
    val trigger: OperationTrigger = OperationTrigger(),
    val effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    val resultSamjnas: Set<Samjna> = emptySet(),
    val resultBindingKaraka: Karaka? = null,
    /** Declared control effect; consumers must not infer this from a display name. */
    val controlSignal: ExecutionControlSignal? = null,
) {
    val name: String get() = action.name
    val description: String get() = action.description
}

data class ResolvedOperation(
    val invocation: DhatuInvocation,
    val operation: DhatuOperation,
    val context: ExecutionContext,
    val resolutionTrace: List<String>,
)

sealed interface OperationResolution {
    data class Resolved(val value: ResolvedOperation) : OperationResolution
    data class MissingInput(val karakas: Set<Karaka>, val message: String) : OperationResolution
    data class Invalid(val error: ExecutionError, val message: String) : OperationResolution
    data class Ambiguous(val operations: List<String>, val message: String) : OperationResolution
}
