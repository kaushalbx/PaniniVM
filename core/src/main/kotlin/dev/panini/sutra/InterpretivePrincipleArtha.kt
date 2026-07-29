package dev.panini.sutra

import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue

/** A grammatical interpretation convention established by a paribhāṣā. */
enum class InterpretivePrinciple {
    SELF_FORM_REFERENCE,
    SAVARNA_INCLUSION_EXCEPT_AFFIX,
    T_MARKED_SOUND_SAME_DURATION,
    PRATYAHARA_RANGE,
}

data class InterpretivePrincipleArtha(
    val principle: InterpretivePrinciple,
) : SutraArthaDefinition {
    override fun toSutraArtha(): SutraArtha = SutraArtha(
        kind = KIND,
        fields = mapOf(
            PRINCIPLE_FIELD to SutraArthaValue.Symbol(principle.name),
        ),
    )

    companion object {
        const val KIND: String = "interpretive-principle"
        private const val PRINCIPLE_FIELD: String = "principle"

        fun fromSutraArtha(artha: SutraArtha): InterpretivePrincipleArtha {
            require(artha.kind == KIND) {
                "Expected $KIND artha, found '${artha.kind}'."
            }
            val principle = (artha.fields[PRINCIPLE_FIELD] as? SutraArthaValue.Symbol)?.name
                ?: error("Interpretive-principle field '$PRINCIPLE_FIELD' must be a symbol.")
            return InterpretivePrincipleArtha(enumValueOf(principle))
        }
    }
}
