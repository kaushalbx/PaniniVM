package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsana 43: देवासुरात्मस्वर्गगिरिसमुद्रनखकेशदन्तस्तनभुजकण्ठ-
 * खड्गशरपङ्काभिधानानि।
 */
object DevasuratmaSvargaGiriSamudraSutra : LinganushasanaSutra(
    number = "43",
    text = "देवासुरात्मस्वर्गगिरिसमुद्रनखकेशदन्तस्तनभुजकण्ठखड्गशरपङ्काभिधानानि",
    hindiExplanation = "समुद्र आदि निर्दिष्ट अर्थों के वाचक शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 40,
) {
    private val meanings = setOf(
        LingaSemanticClass.DEVA_ABHIDHANA,
        LingaSemanticClass.ASURA_ABHIDHANA,
        LingaSemanticClass.ATMAN_ABHIDHANA,
        LingaSemanticClass.SVARGA_ABHIDHANA,
        LingaSemanticClass.GIRI_ABHIDHANA,
        LingaSemanticClass.SAMUDRA_ABHIDHANA,
        LingaSemanticClass.NAKHA_ABHIDHANA,
        LingaSemanticClass.KESA_ABHIDHANA,
        LingaSemanticClass.DANTA_ABHIDHANA,
        LingaSemanticClass.STANA_ABHIDHANA,
        LingaSemanticClass.BHUJA_ABHIDHANA,
        LingaSemanticClass.KANTHA_ABHIDHANA,
        LingaSemanticClass.KHADGA_ABHIDHANA,
        LingaSemanticClass.SARA_ABHIDHANA,
        LingaSemanticClass.PANKA_ABHIDHANA,
    )

    override fun matches(context: LingaRuleContext): Boolean = context.semanticClasses.any { it in meanings }
}
