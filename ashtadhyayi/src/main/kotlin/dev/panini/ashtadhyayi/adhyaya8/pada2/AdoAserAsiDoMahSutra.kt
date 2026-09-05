package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.core.Vacana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.2.80 & 8.2.81: adau d -> m, and vowel mutation (amu / amī) for adas stem.
 */
object AdoAserAsiDoMahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.80",
    text = "अदोऽसेरसि दो मः",
    hindiExplanation = "अदस् अङ्ग के 'द्' के स्थान पर 'म्' आदेश होता है (अदू/अमु/अमी)।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820080,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.isEmpty()) return false

        val stem = context.terms.first()
        if (stem.upadesha != "अदस्") return false

        if (stem.surface == "असौ" || stem.surface.startsWith("अमु") || stem.surface.startsWith("अमू") || stem.surface.startsWith("अमी")) return false

        val hasSup = context.terms.size >= 2 || context.droppedTerms.any { it.id.startsWith("sup-") }
        return hasSup
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first()
        val rupa = context.effectiveContext.rupa
        val affix = context.terms.lastOrNull()

        if (rupa.vacana == Vacana.DVIVACANA && affix != null && affix.upadesha in setOf("औ", "औट्")) {
            return DerivationChange(
                state = context.copy(
                    terms = context.terms.dropLast(2) + stem.copy(surface = "अमू"),
                    droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                    stage = DerivationStage.FINAL,
                ),
                explanation = "8.2.80 & 8.2.81: Derived the dual adas form 'अमू'.",
            )
        }
        if (rupa.vacana == Vacana.BAHUVACANA && affix != null && affix.upadesha == "शी") {
            return DerivationChange(
                state = context.copy(
                    terms = context.terms.dropLast(2) + stem.copy(surface = "अमी"),
                    droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra),
                    stage = DerivationStage.FINAL,
                ),
                explanation = "8.2.80 & 8.2.81: Derived the nominative-plural adas form 'अमी'.",
            )
        }

        val replacement = when {
            rupa.vacana == Vacana.BAHUVACANA && stem.surface.endsWith("े") -> stem.surface.dropLast(1) + "ी"
            rupa.vacana == Vacana.BAHUVACANA && (stem.surface == "अद" || stem.surface == "अम") -> "अमी"
            stem.surface.endsWith("ा") -> "अमू"
            stem.surface.endsWith("े") -> "अमी"
            else -> "अमु"
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = replacement))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "8.2.80 & 8.2.81: Substituted '$replacement' (d->m and vowel mutation) for adas stem."
        )
    }
}
