package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Complete transmitted enumerations:
 * https://sanskritdocuments.org/doc_z_misc_major_works/lingAnushAsanam.html
 */
object SilaMulaMangalaSalaKamalaTalaMusalaKundalaSutra : LinganushasanaSutra(
    number = "145",
    text = "शीलमूलमङ्गलसालकमलतलमुसलकुण्डलपललमृणालबालनिगलपलालबिडालखिलशूलाः पुंसि च",
    hindiExplanation = "ये नपुंसकलिङ्ग शब्द पुंलिङ्ग में भी प्रयुक्त होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf(
        "शील", "मूल", "मङ्गल", "साल", "कमल", "तल", "मुसल", "कुण्डल", "पलल",
        "मृणाल", "बाल", "निगल", "पलाल", "बिडाल", "खिल", "शूल",
    )
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika in members
}

object DandaMandaKhandaSavaSaindhavaParsvaSutra : LinganushasanaSutra(
    number = "183",
    text = "दण्डमण्डखण्डशवसैन्धवपार्श्वकाशकुशकाशाङ्कुशकुलिशाः",
    hindiExplanation = "ये शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMNAPUMSAKALINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf(
        "दण्ड", "मण्ड", "खण्ड", "शव", "सैन्धव", "पार्श्व", "काश", "कुश", "अङ्कुश", "कुलिश",
    )
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika in members
}
