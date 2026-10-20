package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.*
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 132: रात्राह्नाहाः पुंसि. */
object RatraAhnaAhaPumsiSutra : LinganushasanaSutra(
    number = "132", text = "रात्राह्नाहाः पुंसि ।", hindiExplanation = "रात्र, अह्न अथवा अह उत्तरपद वाले समास पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 40,
) { override fun matches(context: LingaRuleContext): Boolean = context.samasaType != null && context.padas.lastOrNull()?.phonologicallyIn(setOf("रात्र", "अह्न", "अह")) == true }
