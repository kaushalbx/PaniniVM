package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** Ghi stem before singular ṅasi/ṅas: realize the e/oḥ ending. */
object GherNgasyoSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.125",
    text = "घेर्ङसिङसोः",
    hindiExplanation = "घि-अन्त पुंलिङ्ग में एकवचन ङसि और ङस् के परे ए/ओ के बाद ऽः रूप होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730125,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("7.3.111"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2 ||
            context.effectiveContext.rupa.linga != Linga.PUMS ||
            context.effectiveContext.rupa.vacana != Vacana.EKAVACANA ||
            context.effectiveContext.rupa.vibhakti !in setOf(Vibhakti.PANCHAMI, Vibhakti.SASTHI)
        ) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return context.samjnas.any { it.targetId == stem.id && it.samjna == Samjna.GHI } &&
            affix.upadesha in setOf("ङसि", "ङस्") &&
            stem.surface.lastOrNull() in setOf('ए', 'े', 'ओ', 'ो')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        return DerivationChange(
            state = context.copy(
                terms = context.terms.dropLast(2) + stem.copy(surface = stem.surface + "ऽः"),
                droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                stage = DerivationStage.FINAL,
            ),
            explanation = "7.3.125: Formed the singular Ghi ङसि/ङस् ending after guṇa.",
        )
    }
}
