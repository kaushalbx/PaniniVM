package dev.panini.vyakaranam.lexicon

import dev.panini.vyakaranam.ast.Linga
import dev.panini.vyakaranam.ast.PadaPrakara

data class PratipadikaEntry(
    val text: String,
    val linga: Set<Linga>,
    val stemClass: String? = null,
    val ganaNames: Set<String> = emptySet(),
)

data class DhatuEntry(
    val upadesha: String,
    val derivationalSurface: String,
    val gana: String,
    val pada: Set<PadaPrakara>,
    val sakarmaka: Boolean,
)

interface PratipadikaLexicon {
    fun findPratipadika(text: String): PratipadikaEntry?
}

interface DhatuLexicon {
    fun findDhatu(text: String): DhatuEntry?
}

interface VyakaranamLexicon :
    PratipadikaLexicon,
    DhatuLexicon

class InMemoryVyakaranamLexicon(
    pratipadikas: Collection<PratipadikaEntry>,
    dhatus: Collection<DhatuEntry>,
) : VyakaranamLexicon {

    private val pratipadikaByText =
        pratipadikas.associateBy { normalize(it.text) }

    private val dhatuByForm =
        buildMap {
            dhatus.forEach { dhatu ->
                put(normalize(dhatu.upadesha), dhatu)
                put(normalize(dhatu.derivationalSurface), dhatu)
            }
        }

    override fun findPratipadika(text: String): PratipadikaEntry? =
        pratipadikaByText[normalize(text)]

    override fun findDhatu(text: String): DhatuEntry? =
        dhatuByForm[normalize(text)]

    private fun normalize(text: String): String =
        text.trim()
}
