package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toVarnas

/** Liṅgānuśāsanam 51: उकारान्तः. */
object UkarantahSutra : LinganushasanaSutra(
    number = "51",
    text = "उकारान्तः",
    hindiExplanation = "ह्रस्व उकारान्त शब्द पुंलिङ्ग होते हैं; उत्तरसूत्रों में पठित अपवाद पृथक् हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 10,
) {
    private val exclusiveExceptions = setOf(
        "धेनु", "रज्जु", "कुहु", "सरयु", "तनु", "रेणु", "प्रियङ्गु",
        "श्मश्रु", "जानु", "स्वादु", "अश्रु", "जतु", "त्रपु", "तालु",
        "दारु", "कसेरु", "जत्रु", "वस्तु", "मस्तु",
    )

    override fun matches(context: LingaRuleContext): Boolean =
        !context.pratipadika.phonologicallyIn(exclusiveExceptions) && context.pratipadika.toVarnas().lastOrNull() == Svara.U
}
