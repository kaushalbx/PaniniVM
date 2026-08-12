package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 2.5: नराख्याः.
 * Male person/deity/agent names are masculine.
 */
object NranahSutra : LinganushasanaSutra(
    number = "2.5",
    text = "नराख्याः",
    hindiExplanation = "पुरुषवाची तथा देववाची नाम पुंलिङ्ग होते हैं (उदा. पुरुषः, नरः, देवः, रामः, कृष्णः, हरिः, विष्णुः)।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 20,
) {
    private val MALE_NAMES = setOf(
        "पुरुष", "नर", "देव", "राम", "कृष्ण", "हरि", "विष्णु", "शिव", "इन्द्र", "सूर्य", "वरुण", "अग्नि", "वायु",
        "लक्ष्मण", "भरत", "शत्रुघ्न", "अर्जुन", "भीम", "युधिष्ठिर", "नकुल", "सहदेव"
    )

    override fun matches(context: LingaRuleContext): Boolean {
        return context.pratipadika in MALE_NAMES
    }
}
