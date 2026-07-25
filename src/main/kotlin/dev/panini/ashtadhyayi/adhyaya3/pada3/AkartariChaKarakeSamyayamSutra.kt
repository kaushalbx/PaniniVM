package dev.panini.ashtadhyayi.adhyaya3.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.3.102 अकर्तरि च कारके संज्ञायाम्.
 * Prescribes ghañ for non-agent kārakas in names.
 */
object AkartariChaKarakeSamyayamSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.3.102", text = "अकर्तरि च कारके संज्ञायाम्",
    hindiExplanation = "संज्ञा (नाम) विषय में अकर्ता कारक (कर्म, करण, सम्प्रदान, अपादान, अधिकरण) अर्थ में 'घञ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 3, optional = false, kramaValue = 330102,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.effectiveContext.rupa.lakara == null &&
        context.allEffectiveTerms.none { it.upadesha == "घञ्" }

    override fun apply(context: DerivationState): DerivationChange {
        val ghan = DerivationTerm("ghan", "अ", TermKind.PRATYAYA, upadesha = "घञ्")
        return DerivationChange(
            state = context.addTerm(ghan),
            explanation = "3.3.102 prescribes घञ् for non-agent kārakas in saṃjñā.",
        )
    }
}
