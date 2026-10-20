package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 91: शुक्रमदेवतायाम्. */
object SukraAdevatayaSutra : LinganushasanaSutra(
    number = "91", text = "शुक्रमदेवतायाम् ।",
    hindiExplanation = "देवता से भिन्न अर्थ में शुक्र नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("शुक्र") && LingaSemanticClass.NON_DEITY in context.semanticClasses
}
