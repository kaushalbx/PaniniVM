package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 16: उभावन्यत्र पुंसि. */
object DundubhiNabhiAnyatraPumsiSutra : LinganushasanaSutra(
    number = "16",
    text = "उभावन्यत्र पुंसि ।",
    hindiExplanation = "पूर्वोक्त विशेष अर्थों से अन्यत्र दुन्दुभि और नाभि पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.PUMS,
    priority = 40,
) {
    private val members = setOf("दुन्दुभि", "नाभि")

    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyIn(members) &&
            LingaSemanticClass.CONTRASTED_OTHER_SENSE in context.semanticClasses
}
