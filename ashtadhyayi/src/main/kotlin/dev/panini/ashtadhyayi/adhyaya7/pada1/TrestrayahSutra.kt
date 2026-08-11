package dev.panini.ashtadhyayi.adhyaya7.pada1

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
 * 7.1.53: trestrayaḥ.
 * Substitutes 'traya' for the stem 'tri' before 'nāmi' (genitive plural 'ām' with 'nuṭ').
 */
object TrestrayahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.53",
    text = "त्रेस्त्रयः",
    hindiExplanation = "नाम (नाम्/आम्) विभक्तौ परे होने पर 'त्रि' के स्थान पर 'त्रय' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710053,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isTri = stem.upadesha == "त्रि" || stem.surface == "त्रि"
        val isNami = affix.id == "sup-am_6" || affix.upadesha == "आम्" ||
            affix.surface.startsWith("नाम") || affix.surface.startsWith("णाम") ||
            context.droppedTerms.any { it.id == "sup-am_6" }

        return isTri && isNami
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = "त्रय"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.53: Replaced stem 'tri' with 'traya' before genitive plural 'nāmi'."
        )
    }
}
