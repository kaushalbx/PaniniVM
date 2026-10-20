package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.*

/** Liṅgānuśāsanam 125: अभाषायां हेमन्तशिशिरावहोरात्रे च. */
object AbhasayamHemantaSisiraAhoratraSutra : LinganushasanaSutra(
    number = "125", text = "अभाषायां हेमन्तशिशिरावहोरात्रे च",
    hindiExplanation = "लौकिक भाषा से भिन्न प्रयोग में हेमन्तशिशिर पुंलिङ्ग और अहोरात्र नपुंसकलिङ्ग होता है।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean = context.isVedicUsage &&
        context.pratipadika.phonologicallyIn(setOf("हेमन्तशिशिर", "अहोरात्र"))
    override fun apply(context: LingaRuleContext): LingaRuleResult {
        val linga = if (context.pratipadika.phonologicallyEquals("अहोरात्र")) Linga.NAPUMSAKA else Linga.PUMS
        return LingaRuleResult.Matched(linga, number, "Sūtra $number ($text) matched for '${context.pratipadika}'.", setOf(linga))
    }
}
