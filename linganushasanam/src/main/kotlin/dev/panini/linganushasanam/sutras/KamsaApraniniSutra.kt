package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 99: कंसं चाप्राणिनि. */
object KamsaApraniniSutra : LinganushasanaSutra(
    number = "99", text = "कंसं चात्प्राणिनि",
    hindiExplanation = "अप्राणिवाचक कंस नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean =
        context.pratipadika.phonologicallyEquals("कंस") && LingaSemanticClass.NON_ANIMATE in context.semanticClasses
}
