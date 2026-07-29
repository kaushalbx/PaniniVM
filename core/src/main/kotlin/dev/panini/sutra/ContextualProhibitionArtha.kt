package dev.panini.sutra

import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraId

enum class ProhibitionTarget {
    VIBHAKTI_FINAL_TUSMA,
}

/** Typed meaning for a contextual prohibition of another sūtra. */
data class ContextualProhibitionArtha(
    val target: ProhibitionTarget,
    val prohibitedSutra: SutraId,
) : SutraArthaDefinition {
    override fun toSutraArtha(): SutraArtha = SutraArtha(
        kind = KIND,
        fields = mapOf(
            TARGET_FIELD to SutraArthaValue.Symbol(target.name),
            PROHIBITED_SUTRA_FIELD to SutraArthaValue.SutraReference(prohibitedSutra),
        ),
    )

    companion object {
        const val KIND: String = "contextual-prohibition"
        private const val TARGET_FIELD: String = "target"
        private const val PROHIBITED_SUTRA_FIELD: String = "prohibited-sutra"

        fun fromSutraArtha(artha: SutraArtha): ContextualProhibitionArtha {
            require(artha.kind == KIND) {
                "Expected $KIND artha, found '${artha.kind}'."
            }
            val target = (artha.fields[TARGET_FIELD] as? SutraArthaValue.Symbol)?.name
                ?: error("Contextual prohibition field '$TARGET_FIELD' must be a symbol.")
            val prohibited =
                (artha.fields[PROHIBITED_SUTRA_FIELD] as? SutraArthaValue.SutraReference)
                    ?.id
                    ?: error(
                        "Contextual prohibition field '$PROHIBITED_SUTRA_FIELD' " +
                            "must be a sūtra reference.",
                    )
            return ContextualProhibitionArtha(enumValueOf(target), prohibited)
        }
    }
}
