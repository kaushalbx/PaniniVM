package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 25: तिथिनाडिरुचिवीचिनालिधूलिकिकिकेलिच्छविरात्र्यादयः । */
object TithiNadiRuciViciSutra : LinganushasanaSutra(
    number = "25",
    text = "तिथिनाडिरुचिवीचिनालिधूलिकिकिकेलिच्छविरात्र्यादयः ।",
    hindiExplanation = "सूत्र में पठित तिथि आदि शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    private val members = setOf("तिथि", "नाडि", "रुचि", "वीचि", "नालि", "धूलि", "किकि", "केलि", "छवि", "रात्रि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

