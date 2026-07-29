package dev.panini.sutra

import dev.panini.shiksha.Samjna
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue

/** A concrete derivation-context selector used by a saṃjñā assignment rule. */
enum class SamjnaAssignmentTarget {
    UPADESHA_NASALIZED_VOWEL,
    UPADESHA_FINAL_CONSONANT,
    DHATU_UPADESHA_INITIAL_NI_TU_DU,
    PRATYAYA_INITIAL_SSA,
    PRATYAYA_INITIAL_CU_TTU,
}

/** Typed meaning for a saṃjñā assigned to matching derivation material. */
data class ContextualSamjnaAssignmentArtha(
    val target: SamjnaAssignmentTarget,
    val samjna: Samjna,
) : SutraArthaDefinition {
    override fun toSutraArtha(): SutraArtha = SutraArtha(
        kind = KIND,
        fields = mapOf(
            TARGET_FIELD to SutraArthaValue.Symbol(target.name),
            SAMJNA_FIELD to SutraArthaValue.Symbol(samjna.canonicalName()),
        ),
    )

    companion object {
        const val KIND: String = "contextual-samjna-assignment"
        private const val TARGET_FIELD: String = "target"
        private const val SAMJNA_FIELD: String = "samjna"

        fun fromSutraArtha(artha: SutraArtha): ContextualSamjnaAssignmentArtha {
            require(artha.kind == KIND) {
                "Expected $KIND artha, found '${artha.kind}'."
            }
            val target = (artha.fields[TARGET_FIELD] as? SutraArthaValue.Symbol)?.name
                ?: error("Contextual assignment field '$TARGET_FIELD' must be a symbol.")
            val samjna = (artha.fields[SAMJNA_FIELD] as? SutraArthaValue.Symbol)?.name
                ?: error("Contextual assignment field '$SAMJNA_FIELD' must be a symbol.")
            return ContextualSamjnaAssignmentArtha(
                target = enumValueOf(target),
                samjna = Samjna.valueOf(samjna),
            )
        }
    }
}

private fun Samjna.canonicalName(): String = when (this) {
    is Enum<*> -> name
    is Samjna.Rudhi -> word
    else -> error("Unsupported saṃjñā representation: ${this::class.simpleName}.")
}
