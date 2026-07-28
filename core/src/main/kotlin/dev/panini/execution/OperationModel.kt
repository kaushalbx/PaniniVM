package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.core.Lakara
import dev.panini.shiksha.Samjna

data class GrammaticalFeatures(
    val upasargas: Set<String> = emptySet(),
    val sanadi: Set<String> = emptySet(),
    val avyayas: Set<String> = emptySet(),
    val lakara: Lakara? = null,
)

data class OperationTrigger(
    val requiredUpasargas: Set<String> = emptySet(),
    val forbiddenUpasargas: Set<String> = emptySet(),
    val requiredSanadi: Set<String> = emptySet(),
    val forbiddenSanadi: Set<String> = emptySet(),
    val requiredAvyayas: Set<String> = emptySet(),
    val forbiddenAvyayas: Set<String> = emptySet(),
    val allowedLakaras: Set<Lakara> = emptySet(),
) {
    init {
        require(requiredUpasargas.intersect(forbiddenUpasargas).isEmpty()) {
            "An operation trigger cannot require and forbid the same upasarga."
        }
        require(requiredAvyayas.intersect(forbiddenAvyayas).isEmpty()) {
            "An operation trigger cannot require and forbid the same avyaya."
        }
        require(requiredSanadi.intersect(forbiddenSanadi).isEmpty()) {
            "An operation trigger cannot require and forbid the same sanādi affix."
        }
    }

    fun matches(features: GrammaticalFeatures): Boolean =
        features.upasargas.containsAll(requiredUpasargas) &&
            features.upasargas.none { it in forbiddenUpasargas } &&
            features.sanadi.containsAll(requiredSanadi) &&
            features.sanadi.none { it in forbiddenSanadi } &&
            features.avyayas.containsAll(requiredAvyayas) &&
            features.avyayas.none { it in forbiddenAvyayas } &&
            (allowedLakaras.isEmpty() || features.lakara in allowedLakaras)
}

enum class ExpressionShape { LITERAL, COORDINATION, REFERENCE }

/** Declarative requirement for one kāraka in an operation signature. */
data class KarakaRequirement(
    val karaka: Karaka,
    val minimumMembers: Int = 1,
    val maximumMembers: Int? = null,
    val shape: ExpressionShape? = null,
    val memberSamjnas: Set<Samjna> = emptySet(),
) {
    init {
        require(minimumMembers >= 1) { "Minimum member count must be positive." }
        require(maximumMembers == null || maximumMembers >= minimumMembers) {
            "Maximum member count cannot be smaller than the minimum."
        }
    }

    internal val specificity: Int
        get() = 1 + (if (shape == null) 0 else 1) + memberSamjnas.size +
            (if (minimumMembers == 1 && maximumMembers == null) 0 else 1)
}

data class OperationSignature(
    val requirements: List<KarakaRequirement>,
    val optionalKarakas: Set<Karaka> = emptySet(),
) {
    init {
        require(requirements.map { it.karaka }.distinct().size == requirements.size) {
            "An operation signature cannot repeat a kāraka requirement."
        }
        require(requirements.none { it.karaka in optionalKarakas }) {
            "A kāraka cannot be both required and optional."
        }
    }

    internal val specificity: Int get() = requirements.sumOf { it.specificity }
}

/** One overload of an executable dhātu, selected by its declarative signature. */
data class DhatuOperation(
    val signature: OperationSignature,
    val action: DhatuAction,
    val trigger: OperationTrigger = OperationTrigger(),
    val effects: Set<ExecutionEffect> = setOf(ExecutionEffect.PURE),
    val resultSamjnas: Set<Samjna> = emptySet(),
    val resultBindingKaraka: Karaka? = null,
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
