package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.ashtadhyayi.adhyaya5.pada2.PuranaNumeralClasses
import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.143: teḥ.
 * Deletion of the final 'ṭi' (vowel onwards) of the stem before a ḍ-it (ḍit) suffix.
 * Here, we delete 'ās' from 'tās' to yield 't' before the 'ḍā' suffix (which is marked as ḍit).
 */
object TehSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.143",
    text = "टेः",
    hindiExplanation = "डित् प्रत्यय परे होने पर अङ्ग के टि-भाग का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640143,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.ANGAKARYA,
    blocks = setOf("7.4.50"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        val stem = context.terms.getOrNull(context.terms.lastIndex - 1) ?: return false

        // Triggers before a ḍit suffix like डा. We detect it via ItMarker.T (representing ṭa-varga initial ḍit)
        val isDit = ending.kind == TermKind.PRATYAYA &&
            (((ending.itMarkers + ending.sthaniProps?.itMarkers.orEmpty()).contains(ItMarker.T) &&
                ending.matchesUpadesha("डा")) || ending.upadesha == "डट्")

        return isDit && (
            (stem.id == "tasi" && stem.surface in setOf("तासि", "तास्")) ||
                (stem.id != "tasi" && stem.surface.endsWith("त्")) ||
                (stem.surface.endsWith("ि") && stem.compoundHeadUpadesha in
                    PuranaNumeralClasses.shashtyadiHeads)
            )
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stemIndex = context.terms.lastIndex - 1
        val stem = context.terms[stemIndex]

        // Delete the ṭi portion ('āsi'/'ās') from tāsi, yielding 't' ('त्').
        // In this engine 6.4.143 runs before 1.3.9 consumes the ḍ-it marker,
        // so the tāsi term can still have its upadeśa surface here.
        val newStem = if (stem.id == "tasi") {
            stem.copy(surface = "त्")
        } else if (stem.surface.endsWith("त्")) {
            stem.copy(surface = stem.surface.dropLast(2))
        } else {
            stem.copy(surface = stem.surface.dropLast(1))
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, newStem),
            explanation = "6.4.143 performs lopa of the ṭi portion of ${stem.surface} before a ḍit suffix."
        )
    }
}
