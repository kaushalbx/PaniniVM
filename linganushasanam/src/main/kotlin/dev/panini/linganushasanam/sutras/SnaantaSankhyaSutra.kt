package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toVarnas
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 185: ष्णान्ता सङ्ख्या । */
object SnaantaSankhyaSutra : LinganushasanaSutra(
    "185", "ष्णान्ता सङ्ख्या", "ष्, न् अथवा ण्-अन्त संख्यावाचक शब्द तीनों लिङ्गों में होते हैं।", LinganushasanaSection.AVISISHTALINGA, targetLingas = Linga.entries.toSet(), 10,
) { override fun matches(context: LingaRuleContext) = context.numericalValue != null && context.pratipadika.toVarnas().lastOrNull() in setOf(Vyanjana.SSA, Vyanjana.NA, Vyanjana.NNA) }
