package dev.panini.linganushasanam.adhyaya5

import dev.panini.core.Linga
import dev.panini.core.SamasaType
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LingaRuleResult
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.adhyaya1.AbantahSutra
import dev.panini.linganushasanam.adhyaya1.KtinantahSutra
import dev.panini.linganushasanam.adhyaya1.NgibantahSutra
import dev.panini.linganushasanam.adhyaya3.AsunIsunUsunantahSutra
import dev.panini.linganushasanam.adhyaya3.LyudadyantahSutra

/**
 * Liṅgānuśāsanam 5.1: परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः (Aṣṭādhyāyī 2.4.26).
 * Uttarapada gender for Tatpuruṣa and Dvandva compounds.
 */
object ParavallingamDvandvaTatpurusayohSutra : LinganushasanaSutra(
    number = "5.1",
    text = "परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः",
    hindiExplanation = "द्वन्द्व और तत्पुरुष समास में उत्तरपद के अनुसार लिङ्ग का विधान होता है।",
    section = LinganushasanaSection.SAMASALINGA,
    targetLinga = Linga.PUMS,
    priority = 30,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        return context.samasaType != null &&
            context.samasaType != SamasaType.AVYAYIBHAVA &&
            context.samasaType != SamasaType.DVIGU &&
            context.samasaType != SamasaType.BAHUVRIHI
    }

    override fun apply(context: LingaRuleContext): LingaRuleResult {
        val lastPada = context.padas.lastOrNull() ?: context.pratipadika
        val innerContext = LingaRuleContext(pratipadika = lastPada, pratyaya = context.pratyaya)

        val targetLinga = when {
            AbantahSutra.matches(innerContext) || NgibantahSutra.matches(innerContext) || KtinantahSutra.matches(innerContext) -> Linga.STRI
            LyudadyantahSutra.matches(innerContext) || AsunIsunUsunantahSutra.matches(innerContext) -> Linga.NAPUMSAKA
            else -> Linga.PUMS
        }

        return LingaRuleResult.Matched(
            linga = targetLinga,
            ruleId = number,
            explanation = "5.1 (परवल्लिङ्गं द्वन्द्वतत्पुरुषयोः): Resolved $targetLinga for uttarapada '$lastPada'.",
        )
    }
}
