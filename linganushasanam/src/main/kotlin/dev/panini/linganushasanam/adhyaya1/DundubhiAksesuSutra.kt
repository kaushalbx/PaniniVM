package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 14: दुन्धुभिरक्षेषु. */
object DundubhiAksesuSutra : LinganushasanaSutra(
    number = "14",
    text = "दुन्धुभिरक्षेषु ।",
    hindiExplanation = "अक्ष-क्रीड़ा के अर्थ में दुन्दुभि स्त्रीलिङ्ग होता है।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("दुन्दुभि") &&
            LingaSemanticClass.AKSHA_DICE in context.semanticClasses
}
