package dev.panini.vyakaranam.lexicon

import dev.panini.core.Linga
import dev.panini.dhatupatha.Dhatu

import dev.panini.core.NominalCategory
import dev.panini.analysis.SemanticRelation
import dev.panini.derivation.NominalStemFormation

data class PratipadikaEntry(
    val text: String,
    val linga: Set<Linga>,
    val stemClass: String? = null,
    val ganaNames: Set<String> = emptySet(),
    val lingaRuleIds: Set<String> = emptySet(),
    val categories: Set<NominalCategory> = emptySet(),
    val semanticRelations: Set<SemanticRelation> = emptySet(),
    /** Declared morphology, independent of the spelling or gender. */
    val stemFormation: NominalStemFormation = NominalStemFormation.UNSPECIFIED,
    val upadesha: String = text,
)

interface PratipadikaLexicon {
    fun findPratipadika(text: String): PratipadikaEntry?
}

/** Shared lexical metadata for established pratipadikas used across runtimes. */
object StandardPratipadikaLexicon : PratipadikaLexicon {
    private val entries = buildMap {
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
        listOf("मूल्य", "परिमाण", "मान", "फल", "परीक्षण").forEach { text ->
            put(text, PratipadikaEntry(text = text, linga = setOf(Linga.NAPUMSAKA)))
        }
        // Technical category and grammatical gender are independent metadata.
        put("सङ्ख्या", getValue("सङ्ख्या").copy(linga = setOf(Linga.STRI), stemFormation = NominalStemFormation.AP))
        put("सूची", PratipadikaEntry("सूची", setOf(Linga.STRI)))
        put("शब्द", PratipadikaEntry("शब्द", setOf(Linga.PUMS)))
        // Independently attested masculine nominal examples, not corpus output aliases:
        // Kāśikā 6.1.113 (vṛkṣa/plakṣa), 6.1.114 (puruṣa), and
        // Siddhāntakaumudī/Bālamanoramā on those rules (Śiva).
        listOf("वृक्ष", "प्लक्ष", "पुरुष", "शिव").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS)))
        }
        // Kāśikā 6.1.113 independently attests अग्निरत्र (not preceding अ).
        put("अग्नि", PratipadikaEntry("अग्नि", setOf(Linga.PUMS)))
        // Independently attested masculine dictionary entries: Śabdakalpadruma
        // kapi (also Amarakośa 2.5.4), and Vācaspatyam udadhi.
        listOf("कपि", "उदधि").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS)))
        }
        // Siddhāntakaumudī 187ff. (Rāma); Śabdamañjarī, nominal table p.15 (deva).
        // These are lexical stems; their case forms are generated with full proofs.
        listOf("राम", "देव").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS)))
        }
        // Śabdakalpadruma's Indra entry and Amarakośa's svargavarga synonym list.
        // Admit stems, never completed visarga spellings or guessed sources.
        listOf("इन्द्र", "शक्र").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS)))
        }
        // Sarvādi pronouns; lexical gender is independent of their derived
        // ka/ya/sa and feminine forms (7.2.102/103, then genuine ṭāp formation).
        listOf("किम्", "यद्", "तद्", "एतद्", "इदम्").forEach { text ->
            put(text, PratipadikaEntry(text, Linga.entries.toSet()))
        }
        // Ordinal adjective stems retain gender independently of numeric identity.
        listOf("प्रथम", "द्वितीय", "तृतीय").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS, Linga.NAPUMSAKA)))
        }
        listOf("प्रथमा" to "प्रथम", "द्वितीया" to "द्वितीय", "तृतीया" to "तृतीय").forEach { (text, base) ->
            put(text, PratipadikaEntry(text, setOf(Linga.STRI),
                stemFormation = NominalStemFormation.AP, upadesha = base))
        }
    }

    override fun findPratipadika(text: String): PratipadikaEntry? = entries[text.trim()]

    /** Declared lexical evidence, not inferred stems or generated surface aliases. */
    fun declaredPratipadikas(): List<PratipadikaEntry> = entries.values.toList()
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
