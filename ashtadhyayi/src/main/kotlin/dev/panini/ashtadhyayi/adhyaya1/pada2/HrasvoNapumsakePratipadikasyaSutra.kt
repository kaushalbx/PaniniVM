package dev.panini.ashtadhyayi.adhyaya1.pada2

import dev.panini.core.Linga
import dev.panini.derivation.*
import dev.panini.shiksha.isDirgha
import dev.panini.shiksha.lastSvara
import dev.panini.shiksha.withFinalHrasva
import dev.panini.sutra.*

/** 1.2.47 ह्रस्वो नपुंसके प्रातिपदिकस्य. */
object HrasvoNapumsakePratipadikasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.2.47", text = "ह्रस्वो नपुंसके प्रातिपदिकस्य",
    hindiExplanation = "नपुंसकलिङ्ग में प्रातिपदिक के अन्त्य दीर्घ स्वर का ह्रस्व होता है।",
    type = SutraType.NITYA, chapter = 1, pada = 2, optional = false, kramaValue = 120047,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DERIVATION,
    stage = SutraStage.ANGAKARYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.linga == Linga.NAPUMSAKA &&
            context.terms.any {
                it.kind == TermKind.PRATIPADIKA && it.surface == it.upadesha && longFinal(it.surface)
            }

    override fun apply(context: DerivationState): DerivationChange {
        val term = context.terms.first {
            it.kind == TermKind.PRATIPADIKA && it.surface == it.upadesha && longFinal(it.surface)
        }
        val surface = term.surface.withFinalHrasva()
        return DerivationChange(
            context.replaceWholeTermSurface(term.id, surface, sutra),
            "1.2.47 shortens the final vowel of a neuter prātipadika.",
        )
    }

    private fun longFinal(surface: String): Boolean = surface.lastSvara()?.isDirgha == true
}
