package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 123: यद्यढग्यगञण्वुञ्छाश्च भावकर्म्मणि. */
object YadYadhakBhavaKarmaniSutra : LinganushasanaSutra(
    number = "123", text = "यद्यढग्यगञण्वुञ्छाश्च भावकर्म्मणि ।",
    hindiExplanation = "सूत्रोक्त तद्धितप्रत्यय भाव अथवा कर्म में नपुंसकलिङ्ग करते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) {
    private val affixes = setOf("यत्", "य", "ढक्", "यक्", "अञ्", "अण्", "वुञ्", "छ")
    override fun matches(context: LingaRuleContext): Boolean = context.pratyaya in affixes &&
        context.semanticClasses.any { it == LingaSemanticClass.BHAVA || it == LingaSemanticClass.KARMAN }
}
