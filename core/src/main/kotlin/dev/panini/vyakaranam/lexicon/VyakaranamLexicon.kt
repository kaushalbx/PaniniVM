package dev.panini.vyakaranam.lexicon

import dev.panini.core.Linga
import dev.panini.dhatupatha.Dhatu

import dev.panini.core.NominalCategory
import dev.panini.analysis.SemanticRelation

data class PratipadikaEntry(
    val text: String,
    val linga: Set<Linga>,
    val stemClass: String? = null,
    val ganaNames: Set<String> = emptySet(),
    val categories: Set<NominalCategory> = emptySet(),
    val semanticRelations: Set<SemanticRelation> = emptySet(),
)

interface PratipadikaLexicon {
    fun findPratipadika(text: String): PratipadikaEntry?
}

interface DhatuLexicon {
    fun findDhatu(text: String): Dhatu?
}

interface VyakaranamLexicon :
    PratipadikaLexicon,
    DhatuLexicon

class InMemoryVyakaranamLexicon(
    pratipadikas: Collection<PratipadikaEntry>,
    dhatus: Collection<Dhatu>,
) : VyakaranamLexicon {

    private val pratipadikaByText =
        pratipadikas.associateBy { normalize(it.text) }

    private val dhatuByForm =
        buildMap {
            dhatus.forEach { dhatu ->
                put(normalize(dhatu.upadesha), dhatu)
                put(normalize(dhatu.sourceSurface), dhatu)
                put(normalize(dhatu.derivationalSurface), dhatu)
            }
        }

    override fun findPratipadika(text: String): PratipadikaEntry? =
        pratipadikaByText[normalize(text)]

    override fun findDhatu(text: String): Dhatu? =
        dhatuByForm[normalize(text)]

    private fun normalize(text: String): String =
        text.trim()
}
