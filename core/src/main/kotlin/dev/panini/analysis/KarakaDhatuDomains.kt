package dev.panini.analysis

import dev.panini.shiksha.Varna
import dev.panini.shiksha.toVarnas
import java.util.concurrent.ConcurrentHashMap

/**
 * Typed lexical domains used by kāraka rules.
 *
 * Spellings are parsed once at this model boundary. Matching below is entirely
 * over phonological varṇa sequences, so individual sūtras do not normalize or
 * search Devanāgarī text.
 */
object KarakaDhatuDomains {
    private val forms = ConcurrentHashMap<String, List<Varna>>()

    private fun form(value: String): List<Varna> = forms.computeIfAbsent(value) { it.toVarnas() }
    private fun List<Varna>.startsWith(prefix: List<Varna>): Boolean =
        size >= prefix.size && subList(0, prefix.size) == prefix
    private fun List<Varna>.endsWith(suffix: List<Varna>): Boolean =
        size >= suffix.size && subList(size - suffix.size, size) == suffix
    private fun List<Varna>.contains(sequence: List<Varna>): Boolean =
        sequence.isEmpty() || indices.any { start ->
            start + sequence.size <= size && subList(start, start + sequence.size) == sequence
        }

    private fun DhatuIdentity.exact(vararg values: String): Boolean = values.any { varnas == form(it) }
    private fun DhatuIdentity.starts(vararg values: String): Boolean = values.any { varnas.startsWith(form(it)) }
    private fun DhatuIdentity.ends(vararg values: String): Boolean = values.any { varnas.endsWith(form(it)) }
    private fun DhatuIdentity.containsAny(vararg values: String): Boolean = values.any { varnas.contains(form(it)) }
    private fun DhatuIdentity.overlaps(vararg values: String): Boolean = values.any {
        val candidate = form(it)
        varnas.contains(candidate) || candidate.contains(varnas)
    }

    fun isAbhinivish(dhatu: DhatuIdentity): Boolean = dhatu.starts("अभिनिविश")

    fun isExcludedAdhikaranaLocus(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("अधिशी", "अधिस्था", "अधिआस्", "अधिशे", "अधितिष्ठ्", "अध्यास्", "उपवस्", "अनुवस्", "अधिवस्", "आवस्") ||
            dhatu.starts("अधिशे", "अधितिष्ठ", "अध्यास्", "उपवस", "अनुवस", "अधिवस", "आवस", "अभिनिविश")

    fun isDvikarmaka(dhatu: DhatuIdentity): Boolean = dhatu.overlaps(
        "दुह्", "याच्", "रुध्", "प्रच्छ्", "चि", "ब्रू", "शास्", "जि", "मन्थ्", "मुष्", "नी", "हृ", "कृष्", "वह्",
        "दोह्", "रोध्", "चे", "जे", "मोष्", "ने", "हार", "कर्ष", "वाह",
    )

    fun isLearning(dhatu: DhatuIdentity): Boolean = dhatu.exact("अधी", "पठ") || dhatu.starts("अधी", "पठ")
    fun isHiding(dhatu: DhatuIdentity): Boolean = dhatu.exact("निली", "तिरोभू") || dhatu.starts("निली", "तिरोभ")
    fun isFearOrProtection(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("भी", "बिभ", "त्रा", "त्राय") || dhatu.starts("बिभे", "त्राय")
    fun isOriginBhu(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("भू", "भव्", "प्रभू", "प्रभव्") || dhatu.starts("भव", "प्रभव")
    fun isBirth(dhatu: DhatuIdentity): Boolean = dhatu.exact("जन्", "जाय्", "जायते") || dhatu.starts("जन")
    fun isParaji(dhatu: DhatuIdentity): Boolean = dhatu.exact("पराजि", "पराजय") || dhatu.starts("पराजय")
    fun isVarana(dhatu: DhatuIdentity): Boolean = dhatu.exact("वृ", "वारय") || dhatu.starts("वारय")

    fun isExcludedGeneralApadana(dhatu: DhatuIdentity): Boolean =
        isOriginBhu(dhatu) || isBirth(dhatu) || isFearOrProtection(dhatu) || isParaji(dhatu) ||
            isLearning(dhatu) || isVarana(dhatu) || isHiding(dhatu)

    fun isDiv(dhatu: DhatuIdentity): Boolean = dhatu.exact("दिव", "दीव्") || dhatu.starts("दीव्य")
    fun isDhari(dhatu: DhatuIdentity): Boolean = dhatu.exact("धृ", "धारय") || dhatu.starts("धारय")
    fun isHrKr(dhatu: DhatuIdentity): Boolean = dhatu.overlaps("हृ", "कृ", "हार", "कार", "हर", "कर")
    fun isJugupsaViramaPramada(dhatu: DhatuIdentity): Boolean =
        dhatu.overlaps("जुगुप्स्", "रम्", "मद्", "जुगुप्सते", "विराम", "प्रमाद्यति")

    fun isAnger(dhatu: DhatuIdentity): Boolean =
        dhatu.containsAny("क्रुध्", "क्रुध", "द्रुह्", "द्रुह") ||
            dhatu.exact("ईर्ष्या", "असूया") || dhatu.starts("ईर्ष्य", "असूय")
    fun hasAngerUpasarga(dhatu: DhatuIdentity): Boolean = dhatu.starts("अभि", "प्र", "प्रति", "अनु")
    fun isPrefixedKrudhDruh(dhatu: DhatuIdentity): Boolean = hasAngerUpasarga(dhatu) &&
        (dhatu.ends("क्रुध्", "द्रुह्", "क्रुध", "द्रुह") || dhatu.containsAny("क्रुध्य", "द्रुह्य"))

    fun isRuc(dhatu: DhatuIdentity): Boolean = dhatu.exact("रुच", "रोच") || dhatu.starts("रोच")
    fun isSprha(dhatu: DhatuIdentity): Boolean = dhatu.exact("स्पृह", "स्पृहय्") || dhatu.starts("स्पृह")
    fun isShlaghHnuSthaShap(dhatu: DhatuIdentity): Boolean =
        dhatu.exact("श्लाघ", "ह्नु", "स्था", "शप") || dhatu.starts("श्लाघ", "ह्नु", "तिष्ठ", "शप")
    fun isParikrayana(dhatu: DhatuIdentity): Boolean = dhatu.exact("परिक्री", "क्री") || dhatu.starts("परिक्री", "क्री")

    fun isExcludedGeneralSampradana(dhatu: DhatuIdentity): Boolean =
        isAnger(dhatu) || isRuc(dhatu) || isSprha(dhatu) || isDhari(dhatu) || isShlaghHnuSthaShap(dhatu) ||
            dhatu.exact("प्रतिश्रु", "आश्रु", "अनुगृ", "प्रतिगृ") ||
            dhatu.starts("प्रतिशृ", "आशृ", "अनुगृ", "प्रतिगृ")
}
