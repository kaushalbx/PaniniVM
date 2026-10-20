package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 105: खारीमानिके स्त्रियाम् । */
object KhariManikaStriyamSutra : LinganushasanaSutra(
    number = "105", text = "खारीमानिके स्त्रियाम्", hindiExplanation = "खारी और मानिका स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.STRI), priority = 50,
) {
    private val members = setOf("खारी", "मानिका")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

