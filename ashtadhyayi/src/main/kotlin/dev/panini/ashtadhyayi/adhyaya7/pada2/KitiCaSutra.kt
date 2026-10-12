package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.ItMarker
import dev.panini.ashtadhyayi.initialVrddhiTaddhitaIdentities
import dev.panini.derivation.hasCurrentAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.shiksha.withInitialVrddhi
import dev.panini.shiksha.toDevanagari
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varnamala

/**
 * 7.2.118: kiti ca.
 * Substitutes Vṛddhi for the first vowel of a stem before a kit Taddhita affix.
 */
object KitiCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.118",
    text = "किति च",
    hindiExplanation = "कित् तद्धित प्रत्यय परे होने पर भी अङ्ग के प्रथम अच् को वृद्धि आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720118,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isTaddhita = "4.1.76" in context.activeAdhikaras ||
            initialVrddhiTaddhitaIdentities.any(pratyaya::hasCurrentAffix)
        if (!isTaddhita) return false

        val isKit = pratyaya.hasEffectiveMarker(ItMarker.KIT)
        if (!isKit) return false

        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        return stem.varnas.withInitialVrddhi() != stem.varnas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stemIndex = context.terms.indexOfFirst { it.kind == TermKind.PRATIPADIKA }
        val stem = context.terms[stemIndex]

        val index = stem.varnas.indexOfFirst { it is Svara }
        val source = stem.varnas[index] as Svara
        val replacement = requireNotNull(Varnamala.getVrddhi(source))
        val result = context.replaceTermVarna(stem.id, index, replacement, sutra)
        return DerivationChange(
            state = result
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.118 applies initial vowel Vṛddhi before kit affix: '${stem.surface}' -> '${result.terms[stemIndex].surface}'.",
        )
    }

}
