package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 139: मुखनयनलोहवनमांसरुधिरकार्मुकविवरजलहलधनान्नाभिधानानि । */
object MukhaNayanaLohaVanaSutra : LinganushasanaSutra(
    number = "139", text = "मुखनयनलोहवनमांसरुधिरकार्मुकविवरजलहलधनान्नाभिधानानि ।", hindiExplanation = "सूत्र में पठित मुख आदि अभिधान नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.NAPUMSAKA, priority = 50,
) {
    private val members = setOf("मुख", "नयन", "लोह", "वन", "मांस", "रुधिर", "कार्मुक", "विवर", "जल", "हल", "धन", "अन्न")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

