package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.core.ItMarker
import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 4.1.4: अजाद्यतष्टाप्.
 * Prescribes 'टाप्' (ṭāp -> आ) feminine suffix after ajādi gaṇa words and short 'a'-ending stems.
 */
object AjadyatasTapSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.4",
    text = "अजाद्यतष्टाप्",
    hindiExplanation = "अजादि गण के शब्दों तथा अकारान्त प्रातिपदिक से परे स्त्रीत्व की विवक्षा में टाप् प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 4,
    pada = 1,
    optional = false,
    kramaValue = 410004,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val stem = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        val isAjadiMember = GanaPatha.isEligibleMember(45, stem.surface, stem.lexicalUses)
        val endsInA = stem.surface.endsWith('अ')
        val processedAng = context.terms.firstOrNull {
            it.kind == TermKind.PRATYAYA && it.upadesha == "अङ्" &&
                it.itProcessingPhase == dev.panini.derivation.ItProcessingPhase.PROCESSED && it.surface == "अ"
        }

        val isTapRequested = context.samjnas.any { it.samjna == Samjna.TAP } ||
            (context.activeAdhikaras.contains("4.1.3") && (isAjadiMember || endsInA)) ||
            (context.effectiveContext.rupa.linga == Linga.STRI && processedAng != null)
        val hasFemininePratyaya = context.terms.any {
            it.kind == TermKind.PRATYAYA && it.upadesha != "अङ्"
        }
        return isTapRequested && !hasFemininePratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tapTerm = DerivationTerm(
            id = "tap_pratyaya",
            surface = "टाप्",
            kind = TermKind.PRATYAYA,
            upadesha = "टाप्",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + tapTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "4.1.4 introduces feminine suffix टाप् (आ)."
        )
    }
}

typealias AjadyatastapSutra = AjadyatasTapSutra
