package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.core.SupAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.hasCurrentAffix
import dev.panini.shiksha.Svara
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType
import dev.panini.sutra.SutraPriority

/**
 * 6.1.93: auto 'm-śasoḥ.
 * Final o and the initial vowel of sup-am/śas coalesce as ā.
 * The stem prefix and the affix's consonants are not substituted by this rule.
 */
object AutoAmSasoSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.93",
    text = "औतोऽम्शसोः",
    hindiExplanation = "ओकारान्त अङ्ग से उत्तर अम् और शस् के परे होने पर पूर्व-पर दोनों के स्थान पर आकार एकादेश होता है (गाम्, गाः)।",
    type = SutraType.APAVADA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610093,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    priority = SutraPriority.APAVADA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isOStem = stem.varnas.lastOrNull() == Svara.O
        if (!isOStem) return false

        return affix.sourceSupAffix in setOf(SupAffix.AM, SupAffix.SAS) &&
            listOf(SupAffix.AM, SupAffix.SAS).any(affix::hasCurrentAffix) &&
            affix.itProcessingPhase == ItProcessingPhase.PROCESSED && affix.varnas.firstOrNull() == Svara.A
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        return DerivationChange(
            state = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(Svara.AA), sutra)
                .deleteTermVarnas(affix.id, 0, 1, sutra)
                .concatenateAfterInitialVowelCoalescence(stem.id, affix, sutra, transferDeferredDesignations = true)
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.1.93 coalesces final o and the am/śas vowel as ā, retaining the stem and affix consonants."
        )
    }
}
