package dev.panini.sutra

import dev.panini.shiksha.Samjna
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue

/** A concrete grammatical concept that receives a technical saṃjñā. */
enum class Samjni {
    ADARSHANA,
    PRATYAYA_ADARSHANA,
    PENULTIMATE_SOUND,
    PRATHAMA_DESIGNATED_COMPOUND_TERM,
    MEANINGFUL_NON_DHATU_NON_PRATYAYA,
    KRT_ENDING,
    TADDHITA_ENDING,
    SAMASA,
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

    }
}

/** Typed definition assigning a closed set of technical names to one concept. */
data class SamjnaSetDefinitionArtha(
    val samjni: Samjni,
    val samjnas: Set<Samjna>,
) : SutraArthaDefinition {
    init {
        require(samjnas.isNotEmpty()) { "A saṃjñā-set definition requires at least one name." }
    }

    override fun toSutraArtha(): SutraArtha = SutraArtha(
        kind = KIND,
        fields = mapOf(
            SAMJNI_FIELD to SutraArthaValue.Symbol(samjni.name),
            SAMJNAS_FIELD to SutraArthaValue.Sequence(
                samjnas.map { SutraArthaValue.Symbol(it.canonicalName()) },
            ),
        ),
    )

    companion object {
        const val KIND: String = "samjna-set-definition"
        private const val SAMJNI_FIELD: String = "samjni"
        private const val SAMJNAS_FIELD: String = "samjnas"

        fun fromSutraArtha(artha: SutraArtha): SamjnaSetDefinitionArtha {
            require(artha.kind == KIND) {
                "Expected $KIND artha, found '${artha.kind}'."
            }
            val samjni = (artha.fields[SAMJNI_FIELD] as? SutraArthaValue.Symbol)?.name
                ?: error("Saṃjñā-set field '$SAMJNI_FIELD' must be a symbol.")
            val samjnas = (artha.fields[SAMJNAS_FIELD] as? SutraArthaValue.Sequence)?.values
                ?.mapTo(linkedSetOf()) {
                    val name = (it as? SutraArthaValue.Symbol)?.name
                        ?: error("Saṃjñā-set names must be symbols.")
                    Samjna.valueOf(name)
                }
                ?: error("Saṃjñā-set field '$SAMJNAS_FIELD' must be a sequence.")
            return SamjnaSetDefinitionArtha(enumValueOf(samjni), samjnas)
        }
    }
}

/** Typed definition assigning one technical name to a closed set of concepts. */
data class SamjniSetDefinitionArtha(
    val samjnis: Set<Samjni>,
    val samjna: Samjna,
) : SutraArthaDefinition {
    init {
        require(samjnis.isNotEmpty()) { "A saṃjñin-set definition requires at least one concept." }
    }

    override fun toSutraArtha(): SutraArtha = SutraArtha(
        kind = KIND,
        fields = mapOf(
            SAMJNIS_FIELD to SutraArthaValue.Sequence(
                samjnis.map { SutraArthaValue.Symbol(it.name) },
            ),
            SAMJNA_FIELD to SutraArthaValue.Symbol(samjna.canonicalName()),
        ),
    )

    companion object {
        const val KIND: String = "samjni-set-definition"
        private const val SAMJNIS_FIELD: String = "samjnis"
        private const val SAMJNA_FIELD: String = "samjna"

        fun fromSutraArtha(artha: SutraArtha): SamjniSetDefinitionArtha {
            require(artha.kind == KIND) {
                "Expected $KIND artha, found '${artha.kind}'."
            }
            val samjnis = (artha.fields[SAMJNIS_FIELD] as? SutraArthaValue.Sequence)?.values
                ?.mapTo(linkedSetOf()) {
                    val name = (it as? SutraArthaValue.Symbol)?.name
                        ?: error("Saṃjñin-set concepts must be symbols.")
                    enumValueOf<Samjni>(name)
                }
                ?: error("Saṃjñin-set field '$SAMJNIS_FIELD' must be a sequence.")
            val samjna = (artha.fields[SAMJNA_FIELD] as? SutraArthaValue.Symbol)?.name
                ?: error("Saṃjñin-set field '$SAMJNA_FIELD' must be a symbol.")
            return SamjniSetDefinitionArtha(samjnis, Samjna.valueOf(samjna))
        }
    }
}

private fun Samjna.canonicalName(): String = when (this) {
    is Enum<*> -> name
    is Samjna.Rudhi -> word
    else -> error("Unsupported saṃjñā representation: ${this::class.simpleName}.")
}
