package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.core.Vacana
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
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

        if (completedForms.any { form -> stem.varnas.take(form.size) == form }) return false

        val hasSup = context.terms.size >= 2 || context.droppedTerms.any { it.id.startsWith("sup-") }
        return hasSup
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms.first()
        val rupa = context.effectiveContext.rupa
        val affix = context.terms.lastOrNull()

        if (rupa.vacana == Vacana.DVIVACANA && affix != null && affix.upadesha in setOf("औ", "औट्")) {
            return DerivationChange(
                state = context.substituteTermVarnas(stem.id, amuu, Vyanjana.DA, listOf(Vyanjana.MA), sutra)
                    .removeTerm(affix.id, sutra = sutra)
                    .copy(stage = DerivationStage.FINAL),
                explanation = "8.2.80 & 8.2.81: Derived the dual adas form 'अमू'.",
            )
        }
        if (rupa.vacana == Vacana.BAHUVACANA && affix != null && affix.upadesha == "शी") {
            return DerivationChange(
                state = context.substituteTermVarnas(stem.id, amii, Vyanjana.DA, listOf(Vyanjana.MA), sutra)
                    .removeTerm(affix.id, sutra = sutra)
                    .copy(stage = DerivationStage.FINAL),
                explanation = "8.2.80 & 8.2.81: Derived the nominative-plural adas form 'अमी'.",
            )
        }

        val replacement = when {
            rupa.vacana == Vacana.BAHUVACANA && stem.varnas.lastOrNull() == Svara.E ->
                stem.varnas.dropLast(1) + Svara.II
            rupa.vacana == Vacana.BAHUVACANA && stem.varnas in setOf(ada, ama) -> amii
            stem.varnas.lastOrNull() == Svara.AA -> amuu
            stem.varnas.lastOrNull() == Svara.E -> amii
            else -> amu
        }

        return DerivationChange(
            state = context.substituteTermVarnas(stem.id, replacement, Vyanjana.DA, listOf(Vyanjana.MA), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "8.2.80 & 8.2.81 substitutes m and the prescribed vowel grade in the adas stem."
        )
    }

    private val asau: List<Varna> = listOf(Svara.A, Vyanjana.SA, Svara.AU)
    private val amu: List<Varna> = listOf(Svara.A, Vyanjana.MA, Svara.U)
    private val amuu: List<Varna> = listOf(Svara.A, Vyanjana.MA, Svara.UU)
    private val amii: List<Varna> = listOf(Svara.A, Vyanjana.MA, Svara.II)
    private val ada: List<Varna> = listOf(Svara.A, Vyanjana.DA, Svara.A)
    private val ama: List<Varna> = listOf(Svara.A, Vyanjana.MA, Svara.A)
    private val completedForms = listOf(asau, amu, amuu, amii)
}
