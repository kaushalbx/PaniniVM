package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

object DandaMandaKhandaSavaSaindhavaParsvaSutra : LinganushasanaSutra(
    number = "181",
    text = "दण्डमण्डखमशमसैन्धवपार्श्वाकाशकुशकाशाङ्कुशकुलिशाः",
    hindiExplanation = "ये शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMNAPUMSAKALINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("दण्ड", "मण्ड", "खम", "शम", "सैन्धव", "पार्श्व", "आकाश", "कुश", "काश", "अङ्कुश", "कुलिश")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
