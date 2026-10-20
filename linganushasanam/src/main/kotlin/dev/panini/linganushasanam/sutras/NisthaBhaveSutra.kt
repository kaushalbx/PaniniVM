package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 120: निष्ठा च. */
object NisthaBhaveSutra : LinganushasanaSutra(
    number = "120", text = "निष्ठा च", hindiExplanation = "भावार्थ में निष्ठा-प्रत्ययान्त शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) {
    private val nisthaAffixes = setOf("क्त", "क्तवतुँ")
    override fun matches(context: LingaRuleContext): Boolean = context.pratyaya in nisthaAffixes && LingaSemanticClass.BHAVA in context.semanticClasses
}
