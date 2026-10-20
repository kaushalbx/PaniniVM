package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object SandaMandaKarandaSutra : LinganushasanaSutra(
    number = "112",
    text = "षण्डमण्डकरण्डभरण्डवरण्डतुण्डगण्डमुण्डपाषण्डशिखण्डाः",
    hindiExplanation = "सूत्र में निर्दिष्ट शब्द पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS),
    priority = 50,
) {
    private val members = setOf("षण्ड", "मण्ड", "करण्ड", "भरण्ड", "वरण्ड", "तुण्ड", "गण्ड", "मुण्ड", "पाषण्ड", "शिखण्ड")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
