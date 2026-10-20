package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 128: अनल्पे छाया. */
object AnalpeChayaSutra : LinganushasanaSutra(
    number = "128", text = "अनल्पे छाया ।", hindiExplanation = "बहुलता के अर्थ में छाया उत्तरपद वाला तत्पुरुष नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType == SamasaType.TATPURUSA && context.padas.lastOrNull()?.phonologicallyEquals("छाया") == true && LingaSemanticClass.ABUNDANT in context.semanticClasses }
