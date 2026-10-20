package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 129: राजाऽमनुष्यपूर्वा सभा. */
object RajaAmanusyaPurvaSabhaSutra : LinganushasanaSutra(
    number = "129", text = "राजाऽमनुष्यपूर्वा सभा ।", hindiExplanation = "राजवाचक अथवा अमनुष्यवाचक पूर्वपद के साथ सभा-उत्तरपद तत्पुरुष नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull()?.phonologicallyEquals("सभा") == true && context.semanticClasses.any { it == LingaSemanticClass.KING_TITLE || it == LingaSemanticClass.NON_HUMAN } }
