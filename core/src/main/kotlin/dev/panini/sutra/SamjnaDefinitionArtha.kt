package dev.panini.sutra

import dev.panini.shiksha.Samjna
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue

sealed interface SutraArthaDefinition {
    fun toSutraArtha(): SutraArtha
}

/** A concrete grammatical concept that receives a technical saṃjñā. */
enum class Samjni {
    ADARSHANA,
}

/** Typed domain representation of an interpretive saṃjñā definition. */
data class SamjnaDefinitionArtha(
    val samjni: Samjni,
    val samjna: Samjna,
) : SutraArthaDefinition {
    override fun toSutraArtha(): SutraArtha = SutraArtha(
        kind = KIND,
        fields = mapOf(
            SAMJNI_FIELD to SutraArthaValue.Symbol(samjni.name),
            SAMJNA_FIELD to SutraArthaValue.Symbol(samjna.canonicalName()),
        ),
    )

    companion object {
        const val KIND: String = "samjna-definition"
        private const val SAMJNI_FIELD: String = "samjni"
        private const val SAMJNA_FIELD: String = "samjna"

        fun fromSutraArtha(artha: SutraArtha): SamjnaDefinitionArtha {
            require(artha.kind == KIND) {
                "Expected $KIND artha, found '${artha.kind}'."
            }
            return SamjnaDefinitionArtha(
                samjni = enumValueOf(artha.requireSymbol(SAMJNI_FIELD)),
                samjna = Samjna.valueOf(artha.requireSymbol(SAMJNA_FIELD)),
            )
        }

        private fun SutraArtha.requireSymbol(name: String): String =
            (fields[name] as? SutraArthaValue.Symbol)?.name
                ?: error("Saṃjñā-definition field '$name' must be a symbol.")

        private fun Samjna.canonicalName(): String = when (this) {
            is Enum<*> -> name
            is Samjna.Rudhi -> word
            else -> error("Unsupported saṃjñā representation: ${this::class.simpleName}.")
        }
    }
}
