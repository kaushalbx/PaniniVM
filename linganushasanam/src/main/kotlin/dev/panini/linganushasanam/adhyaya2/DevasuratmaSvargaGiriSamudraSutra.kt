package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsana 43: देवासुरात्मस्वर्गगिरिसमुद्रनखकेशदन्तस्तनभुजकण्ठ-
 * खड्गशरपङ्काभिधानानि।
 *
 * This implementation currently recognizes only the independently declared
 * samudra-abhidhāna branch. It deliberately does not infer meaning from text.
 */
object DevasuratmaSvargaGiriSamudraSutra : LinganushasanaSutra(
    number = "43",
    text = "देवासुरात्मस्वर्गगिरिसमुद्रनखकेशदन्तस्तनभुजकण्ठखड्गशरपङ्काभिधानानि",
    hindiExplanation = "समुद्र आदि निर्दिष्ट अर्थों के वाचक शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        LingaSemanticClass.SAMUDRA_ABHIDHANA in context.semanticClasses
}
