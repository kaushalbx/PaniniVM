package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.55: ṣaṭ-caturbhyaś ca.
 * Adds the augment 'nuṭ' before the genitive plural affix 'ām' after 'ṣaṭ' designated stems and 'catur'.
 */
object SatCaturbhyascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.55",
    text = "षट्-चतुर्भ्यश्च",
    hindiExplanation = "षट् संज्ञक शब्दों और 'चतुर्' के बाद आम् को नुट् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710055,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    dependencies = setOf("6.4.1", "1.1.46")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isCaturOrShat = stem.upadesha in setOf("चतुर्", "षट्") || stem.surface in setOf("चतुर्", "षट्")

        return isCaturOrShat && affix.upadesha == "आम्" && !affix.surface.startsWith("नाम") && !affix.surface.startsWith("णाम") && context.allEffectiveTerms.none { it.upadesha == "नुट्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        val newSurface = "नाम्"
        val newTerms = context.terms.dropLast(1) + affix.copy(surface = newSurface)

        return DerivationChange(
            state = context.copy(terms = newTerms),
            explanation = "7.1.55: Added 'nuṭ' augment before 'ām' after catur/ṣaṭ."
        )
    }
}
