package dev.panini.linganushasanam.sutras

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 128: राजाऽमनुष्यपूर्वा सभा. */
object RajaAmanusyaPurvaSabhaSutra : LinganushasanaSutra(
    number = "128", text = "राजाऽमनुष्यपूर्वा सभा", hindiExplanation = "राजवाचक अथवा अमनुष्यवाचक पूर्वपद के साथ सभा-उत्तरपद तत्पुरुष नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull()?.phonologicallyEquals("सभा") == true && context.semanticClasses.any { it == LingaSemanticClass.KING_TITLE || it == LingaSemanticClass.NON_HUMAN } }
