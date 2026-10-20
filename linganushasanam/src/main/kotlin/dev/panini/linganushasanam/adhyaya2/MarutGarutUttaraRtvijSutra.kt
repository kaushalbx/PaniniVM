package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 108: मरुद्गरुदुत्तरदृत्विजः । */
object MarutGarutUttaraRtvijSutra : LinganushasanaSutra(
    number = "108", text = "मरुद्गरुदुत्तरदृत्विजः ।", hindiExplanation = "मरुत्, गरुत्, उत्तर और ऋत्विज् पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.PUMS, priority = 50,
) {
    private val members = setOf("मरुत्", "गरुत्", "उत्तर", "ऋत्विज्")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

