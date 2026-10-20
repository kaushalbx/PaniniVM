package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object HastaKuntaAntaVataVrataDutaSutra : LinganushasanaSutra(
    number = "111",
    text = "हस्तकुन्तान्तव्रातवातदूतधूतसूतचूतमुहूर्ताः",
    hindiExplanation = "सूत्र में निर्दिष्ट शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("हस्त", "कुन्त", "अन्त", "व्रात", "वात", "दूत", "धूत", "सूत", "चूत", "मुहूर्त")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
