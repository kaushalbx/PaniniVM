package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 130: सुरासेनाच्छायाशालानिशाः स्त्रियाञ्च. */
object SuraSenaChayaSalaNisaSutra : LinganushasanaSutra(
    number = "130", text = "सुरासेनाच्छायाशालानिशाः स्त्रियाञ्च ।", hindiExplanation = "सूत्रोक्त उत्तरपद वाले तत्पुरुष स्त्रीलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) {
    override val targetLingas = setOf(Linga.STRI, Linga.NAPUMSAKA)
    private val finalMembers = setOf("सुरा", "सेना", "छाया", "शाला", "निशा")
    override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull()?.phonologicallyIn(finalMembers) == true
}
