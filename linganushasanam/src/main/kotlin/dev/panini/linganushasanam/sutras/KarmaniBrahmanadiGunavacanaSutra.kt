package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.LingaSemanticClass
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 122: कर्मणि च ब्राह्मणादिगुणवचनेभ्यः. */
object KarmaniBrahmanadiGunavacanaSutra : LinganushasanaSutra(
    number = "122", text = "कर्मणि च ब्राह्मणादिगुणवचनेभ्यः",
    hindiExplanation = "ब्राह्मणादि गुणवाचक आधार से कर्म/भाव में बने त्व अथवा ष्यञन्त शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.NAPUMSAKA), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.isQualityExpression &&
        context.pratyaya in setOf("त्व", "ष्यञ्") &&
        context.semanticClasses.any { it == LingaSemanticClass.BHAVA || it == LingaSemanticClass.KARMAN }
}
