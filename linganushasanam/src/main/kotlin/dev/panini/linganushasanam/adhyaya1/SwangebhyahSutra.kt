package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/**
 * Liṅgānuśāsanam 1.1: स्वाङ्गेभ्यः.
 * Body part names taking feminine affixes (-ङीष्) are feminine.
 */
object SwangebhyahSutra : LinganushasanaSutra(
    number = "1.1",
    text = "स्वाङ्गेभ्यः",
    hindiExplanation = "स्वाङ्गवाची (शरीरावयववाचक) शब्द स्त्रीलिङ्ग होते हैं (उदा. अङ्गुली, जङ्घा, नासिका)।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 20,
) {
    private val SWANGA_STEMS = setOf("अङ्गुली", "जङ्घा", "नासिका", "ग्रीवा", "रसना", "जिह्वा")

    override fun matches(context: LingaRuleContext): Boolean {
        return context.pratipadika in SWANGA_STEMS
    }
}
