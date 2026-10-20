package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 172: अक्षमिन्द्रिये । */
object AksaIndriyeSutra : LinganushasanaSutra(
    number = "172", text = "अक्षमिन्द्रिये ।", hindiExplanation = "इन्द्रिय के अर्थ में अक्ष शब्द नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("अक्ष") && LingaSemanticClass.SENSE_ORGAN in context.semanticClasses
}
