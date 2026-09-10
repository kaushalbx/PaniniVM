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

/** Canonical lexical classes used by rules whose domain is a named gana. */
enum class PratipadikaGana {
    TYADADI,
    PERSONAL_PRONOUN,
}

object PratipadikaGanaMembership {
    private val members = mapOf(
        PratipadikaGana.TYADADI to setOf("त्यद्", "तद्", "यद्", "एतद्", "किम्", "इदम्", "अदस्", "द्वि"),
        PratipadikaGana.PERSONAL_PRONOUN to setOf("युष्मद्", "अस्मद्"),
    )

    fun belongsTo(text: String, gana: PratipadikaGana): Boolean = text.trim() in members.getValue(gana)

    fun isPronominal(text: String): Boolean =
        belongsTo(text, PratipadikaGana.TYADADI) || belongsTo(text, PratipadikaGana.PERSONAL_PRONOUN)
}

/** Shared lexical metadata for established pratipadikas used across runtimes. */
object StandardPratipadikaLexicon : PratipadikaLexicon {
    private val entries = buildMap {
        listOf("हविस्", "मनस्", "पयस्", "उरस्", "चक्षुस्").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.NAPUMSAKA)))
        }
        listOf("सङ्ख्या", "गुण", "वृद्धि", "लोप", "साधकतमम्", "कर्म", "करणम्").forEach { text ->
            put(
                text,
                PratipadikaEntry(
                    text = text,
                    linga = emptySet(),
                    categories = setOf(NominalCategory.TECHNICAL_SAMJNA),
                ),
            )
        }
    }

    override fun findPratipadika(text: String): PratipadikaEntry? = entries[text.trim()]
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
