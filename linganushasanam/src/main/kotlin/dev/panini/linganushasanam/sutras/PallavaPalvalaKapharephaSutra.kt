package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object PallavaPalvalaKapharephaSutra : LinganushasanaSutra(
    number = "116",
    text = "पल्लवपल्वलकफरेफकटाहनिर्व्यूहमठमणितरङ्गतुरङ्गगन्धमृदङ्गसङ्गसमुद्गपुङ्खाः",
    hindiExplanation = "सूत्र में निर्दिष्ट शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("पल्लव", "पल्वल", "कफ", "रेफ", "कटाह", "निर्व्यूह", "मठ", "मणि", "तरङ्ग", "तुरङ्ग", "गन्ध", "मृदङ्ग", "सङ्ग", "समुद्ग", "पुङ्ख")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
