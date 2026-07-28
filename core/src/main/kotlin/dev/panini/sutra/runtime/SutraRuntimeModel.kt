package dev.panini.sutra.runtime

import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole

@JvmInline
value class SutraId(val value: String) {
    init {
        require(value.isNotBlank()) { "A runtime sūtra requires a non-blank identity." }
    }

    override fun toString(): String = value
}
/** Marker for a typed state processed by the shared sūtra machine. */
interface SutraAvastha

sealed interface SutraSource {
    val text: String

    data class Ashtadhyayi(
        val number: String,
        override val text: String,
    ) : SutraSource

    data class Vakya(
        val uktiId: String,
        val vakyaIndex: Int,
        override val text: String,
    ) : SutraSource

    data class Program(
        val grantha: String,
        val location: String,
        override val text: String,
    ) : SutraSource
}

sealed interface SutraRelation {
    data class DependsOn(val prerequisite: SutraId) : SutraRelation
    data class Blocks(val target: SutraId) : SutraRelation
    data class PhalaPravaha(val source: SutraId, val target: SutraId) : SutraRelation
}

/**
 * An effect is deliberately an inspectable value rather than an executable
 * function. A domain interpreter owns the authority to apply it.
 */
interface SutraEffect<S : SutraAvastha>

fun interface SutraEvaluator<S : SutraAvastha> {
    fun evaluate(sutra: RuntimeSutra<S>, state: S): SutraNirnaya<S>
}

data class RuntimeSutra<S : SutraAvastha>(
    val id: SutraId,
    val source: SutraSource,
    val role: SutraRole,
    val evaluator: SutraEvaluator<S>,
    val relations: Set<SutraRelation> = emptySet(),
    val governance: SutraGovernance = SutraGovernance(),
)

data class SutraProgram<S : SutraAvastha>(
    val id: String,
    val sutras: List<RuntimeSutra<S>>,
) {
    init {
        require(id.isNotBlank()) { "A sūtra program requires a non-blank identity." }
        require(sutras.map { it.id }.distinct().size == sutras.size) {
            "A sūtra program cannot contain duplicate sūtra identities."
        }
    }
}

sealed interface SutraNirnaya<out S : SutraAvastha> {
    data class Applicable<S : SutraAvastha>(
        val effects: List<SutraEffect<S>>,
        val reasons: List<String> = emptyList(),
    ) : SutraNirnaya<S>

    data class NotApplicable(
        val reasons: List<String> = emptyList(),
    ) : SutraNirnaya<Nothing>

    data class Blocked(
        val blocker: SutraId,
        val reasons: List<String> = emptyList(),
    ) : SutraNirnaya<Nothing>

    data class Invalid(
        val message: String,
    ) : SutraNirnaya<Nothing>
}
