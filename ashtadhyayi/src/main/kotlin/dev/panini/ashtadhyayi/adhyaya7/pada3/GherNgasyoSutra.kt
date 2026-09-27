package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.OrthographicSign
import dev.panini.shiksha.OrthographicSignPlacement
import dev.panini.shiksha.Svara
import dev.panini.shiksha.toDevanagari
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
            stem.varnas.lastOrNull() in setOf(Svara.E, Svara.O)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val resultVarnas = stem.varnas + Ayogavaha.VISARGA
        val signs = listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, stem.varnas.size))
        val surface = resultVarnas.toDevanagari(signs)
        val merged = context.mergeTermsByVarnaSubstitution(
            stem.id, affix.id, surface, affix.varnas.first(), listOf(Ayogavaha.VISARGA), sutra,
        )
        return DerivationChange(
            state = merged.replaceTerm(
                stem.id,
                merged.terms.single { it.id == stem.id }.copy(orthographicSigns = signs),
            ).copy(stage = DerivationStage.FINAL),
            explanation = "7.3.125: Formed the singular Ghi ङसि/ङस् ending after guṇa.",
        )
    }
}
