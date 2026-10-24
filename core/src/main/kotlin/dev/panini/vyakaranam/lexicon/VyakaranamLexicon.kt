package dev.panini.vyakaranam.lexicon

import dev.panini.core.Linga
import dev.panini.dhatupatha.Dhatu

import dev.panini.core.NominalCategory
import dev.panini.analysis.SemanticRelation
import dev.panini.derivation.NominalStemFormation
import dev.panini.core.LingaSemanticClass

data class PratipadikaEntry(
    val text: String,
    val linga: Set<Linga>,
    val stemClass: String? = null,
    val ganaNames: Set<String> = emptySet(),
    val lingaRuleIds: Set<String> = emptySet(),
    val categories: Set<NominalCategory> = emptySet(),
    val semanticRelations: Set<SemanticRelation> = emptySet(),
    /** Independently sourced meaning classes consumed by Liṅgānuśāsana rules. */
    val lingaSemanticClasses: Set<LingaSemanticClass> = emptySet(),
    /** Independently established derivational affix used by Liṅgānuśāsana. */
    val lingaPratyaya: String? = null,
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
        put("कर्मन्", PratipadikaEntry("कर्मन्", setOf(Linga.NAPUMSAKA),
            categories = setOf(NominalCategory.TECHNICAL_SAMJNA)))
        put("शब्द", PratipadikaEntry("शब्द", emptySet()))
        // Independently attested masculine nominal examples, not corpus output aliases:
        // Kāśikā 6.1.113 (vṛkṣa/plakṣa), 6.1.114 (puruṣa), and
        // Siddhāntakaumudī/Bālamanoramā on those rules (Śiva).
        listOf("वृक्ष", "प्लक्ष", "पुरुष", "शिव").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS)))
        }
        // Kāśikā 6.1.113 independently attests अग्निरत्र (not preceding अ).
        put("अग्नि", PratipadikaEntry("अग्नि", setOf(Linga.PUMS)))
        // Independently attested masculine dictionary entry: Vācaspatyam udadhi.
        // Kapi belongs to the exact Liṅgānuśāsana 109 enumeration below.
        put("उदधि", PratipadikaEntry("उदधि", setOf(Linga.PUMS)))
        // Siddhāntakaumudī 187ff. (Rāma); Śabdamañjarī, nominal table p.15 (deva).
        // These are lexical stems; their case forms are generated with full proofs.
        listOf("राम", "देव").forEach { text ->
            put(text, PratipadikaEntry(text, setOf(Linga.PUMS)))
        }
        // Masculine nominal stems explicitly attested by Amarakośa:
        // ghaṭa 2.9.32, putra 2.6.27, doṣa 3.3.226, guru 1.3.24/2.7.7,
        // hetu 1.4.28, and prabhu 3.1.23. Admit stems, never corpus surface forms; gender is
        // attached by the Liṅgānuśāsanam-layer PaninianPratipadikaLexicon.
        listOf("घट", "पुत्र", "दोष", "गुरु", "हेतु", "प्रभु").forEach { text ->
            put(text, PratipadikaEntry(text, emptySet()))
        }
        // Amarakośa 1.11.518–520 enumerates these as ocean synonyms.
        // Gender is assigned by Liṅgānuśāsana 43, not stored lexically.
        listOf("सागर", "रत्नाकर").forEach { text ->
            put(text, PratipadikaEntry(text, emptySet(),
                lingaSemanticClasses = setOf(LingaSemanticClass.SAMUDRA_ABHIDHANA)))
        }
        // Amarakośa 2.8.1152 attests विजयः; its vi-√ji-ac formation is in the
        // domain of Liṅgānuśāsana 37. Gender remains assigned by that rule.
        put("विजय", PratipadikaEntry("विजय", emptySet(), lingaPratyaya = "अच्"))
        // Independently selected prātipadika identities from the exact
        // Liṅgānuśāsana 109–117 and 158 enumerations. Gender
        // is intentionally absent here and is supplied only by the registered
        // LinganushasanaSutra. The rule objects retain the complete lists.
        // Source text:
        // https://sanskritdocuments.org/doc_z_misc_major_works/lingAnushAsanam.pdf
        // Their complete paradigms are generated; no corpus surface is entered.
        listOf(
            "ऋषि", "राशि", "ध्वनि", "रवि", "कवि", "कपि", "मुनि",
            "ध्वज", "गज",
            "हस्त", "अन्त", "दूत", "सूत", "मुहूर्त",
            "मुण्ड", "वंश", "अंश", "पुरोडाश",
            "ह्रद", "कुन्द", "शब्द",
            "पथि", "पूग",
            "कफ", "मठ", "मणि", "गन्ध", "स्कन्ध", "मृदङ्ग", "सङ्ग",
            "सारथि", "अतिथि", "कुक्षि", "पाणि", "अञ्जलि",
            "अमित्र", "छात्र", "मन्त्र", "उष्ट्र",
        ).forEach { text -> put(text, PratipadikaEntry(text, emptySet())) }
        // Exact identities in Liṅgānuśāsana 145 and 183. Gender is supplied by
        // those registered rules; the complete enumerations remain in that module.
        listOf("मूल", "बाल", "शूल", "दण्ड", "शव").forEach { text ->
            put(text, PratipadikaEntry(text, emptySet()))
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
