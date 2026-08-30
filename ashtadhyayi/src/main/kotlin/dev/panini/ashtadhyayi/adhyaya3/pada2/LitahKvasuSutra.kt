package dev.panini.ashtadhyayi.adhyaya3.pada2

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 3.2.106: लिटः क्वसुश्च.
 * Prescribes 'क्वसु' (kvasu -> वस्) perfect participle affix after a verbal root in Liṭ.
 */
object LitahKvasuSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.2.106",
    text = "लिटः क्वसुश्च",
    hindiExplanation = "छन्दस् (तथा भाषा में भी) धातु से लिट् के विषय में 'क्वसु' प्रत्यय होता है।",
    type = SutraType.UTSARGA,
    chapter = 3,
    pada = 2,
    optional = false,
    kramaValue = 320106,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.stage != DerivationStage.INITIAL && context.stage != DerivationStage.PRATYAYA_SELECTED) return false
        val isKvasuRequested = context.samjnas.any { it.samjna == Samjna.KVASU }
        val hasDhatu = context.terms.any { it.kind == TermKind.DHATU }
        val hasPratyaya = context.terms.any { it.kind == TermKind.PRATYAYA }
        return isKvasuRequested && hasDhatu && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val kvasuTerm = DerivationTerm(
            id = "kvasu_pratyaya",
            surface = "क्वसुँ",
            kind = TermKind.PRATYAYA,
            upadesha = "क्वसु",
            createdBySutra = sutra,
            itProcessingPhase = dev.panini.derivation.ItProcessingPhase.RAW_UPADESHA,
        )
        return DerivationChange(
            state = context.copy(
                terms = context.terms + kvasuTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "3.2.106 introduces perfect participle affix क्वसु (वस्)."
        )
    }
}
