package dev.panini.linganushasanam.sutras

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 129: सुरासेनाच्छायाशालानिशाः स्त्रियाञ्च. */
object SuraSenaChayaSalaNisaSutra : LinganushasanaSutra(
    number = "129", text = "सुरासेनाच्छायाशालानिशा स्त्रियां च", hindiExplanation = "सूत्रोक्त उत्तरपद वाले तत्पुरुष स्त्रीलिङ्ग तथा नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI, Linga.NAPUMSAKA), priority = 40,
) {
    private val finalMembers = setOf("सुरा", "सेना", "छाया", "शाला", "निशा")
    override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull()?.phonologicallyIn(finalMembers) == true
}
