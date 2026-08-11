package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.99: tri-caturoḥ striyāṁ tisṛ-catasṛ.
 * Substitutes 'tisṛ' for 'tri' and 'catasṛ' for 'catur' before a case affix in feminine gender.
 */
object TisrCatasruSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.99",
    text = "त्रिचतुरोः स्त्रियां तिसृचतसृ",
    hindiExplanation = "स्त्रीलिङ्ग में विभक्तौ परे होने पर 'त्रि' के स्थान पर 'तिसृ' और 'चतुर्' के स्थान पर 'चतसृ' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720099,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.linga != Linga.STRI) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // This ādeśa applies to the original surface once. The retained
        // upadeśa must not cause it to reapply after later ṛ-stem operations.
        if (stem.surface !in setOf("त्रि", "चतुर्")) return false

        return affix.id.startsWith("sup-") || context.droppedTerms.any { it.id.startsWith("sup-") }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val replacement = when (stem.surface) {
            "चतुर्" -> "चतसृ"
            else -> "तिसृ"
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = replacement))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.99: Substituted '$replacement' for '${stem.surface}' in feminine gender."
        )
    }
}
