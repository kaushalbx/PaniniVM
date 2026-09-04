package dev.panini.ashtadhyayi.adhyaya7.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.ItProcessingPhase
import dev.panini.derivation.TermKind
import dev.panini.derivation.WholeAffixDesignationPolicy
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.3.50: ठस्येकः. Substitutes इक for the processed ठ of a ठ-initial affix. */
object ThasyaIkahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.3.50",
    text = "ठस्येकः",
    hindiExplanation = "प्रत्यय के आदि ठ के स्थान पर इक आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 3,
    optional = false,
    kramaValue = 730050,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.any {
        it.kind == TermKind.PRATYAYA &&
            it.surface == "ठ" &&
            it.itProcessingPhase == ItProcessingPhase.PROCESSED &&
            it.upadesha in setOf("ठक्", "ठच्", "ष्ठन्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms.first {
            it.kind == TermKind.PRATYAYA && it.surface == "ठ" && it.upadesha in setOf("ठक्", "ठच्", "ष्ठन्")
        }
        return DerivationChange(
            state = context.replaceWholeAffix(
                id = target.id,
                surface = "इक",
                sutra = sutra,
                policy = WholeAffixDesignationPolicy.Consume,
            ),
            explanation = "7.3.50 substitutes इक for the processed ठ of ${target.upadesha}.",
        )
    }
}
