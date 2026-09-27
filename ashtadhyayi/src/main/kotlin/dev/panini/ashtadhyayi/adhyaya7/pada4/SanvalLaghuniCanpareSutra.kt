package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.shiksha.toDevanagari
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.4.93: सन्वल्लघूनि चङ्परेऽनग्लोपे.
 * Governs root reduplication and abhyāsa operations before 'चङ्' (caṅ).
 */
object SanvalLaghuniCanpareSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.93",
    text = "सन्वल्लघूनि चङ्परेऽनग्लोपे",
    hindiExplanation = "चङ् परे होने पर अङ्ग का सन्वद्भाव और अभ्यास का कार्य होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 4,
    optional = false,
    kramaValue = 740093,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val hasCan = context.terms.any { it.upadesha == "चङ्" }
        val hasAbhyasa = context.terms.any { it.id == "can_abhyasa" }
        return hasCan && !hasAbhyasa
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (rootIndex < 0) return DerivationChange(context, "No root found for caṅ-reduplication.")
        val root = context.terms[rootIndex]

        val abhyasaSurface = computeCanAbhyasa(root.varnas).toDevanagari()
        val abhyasaTerm = DerivationTerm(
            id = "can_abhyasa",
            surface = abhyasaSurface,
            kind = TermKind.DHATU,
            upadesha = abhyasaSurface,
            createdBySutra = sutra,
        )

        val newTerms = context.terms.take(rootIndex) + abhyasaTerm + context.terms.drop(rootIndex)
        return DerivationChange(
            state = context.copy(
                terms = newTerms,
                stage = maxOf(context.stage, DerivationStage.ANGAKARYA),
            ),
            explanation = "7.4.93 performs caṅ-reduplication of ${root.surface} into $abhyasaSurface."
        )
    }

    private fun computeCanAbhyasa(root: List<Varna>): List<Varna> = when (root) {
        listOf<Varna>(Vyanjana.BHA, Svara.UU) -> listOf(Svara.A, Vyanjana.BA, Svara.II)
        listOf<Varna>(Vyanjana.KA, Svara.R) -> listOf(Svara.A, Vyanjana.CA, Svara.II)
        listOf<Varna>(Vyanjana.PA, Svara.A, Vyanjana.THA) -> listOf(Svara.A, Vyanjana.PA, Svara.II)
        else -> when (val initial = root.firstOrNull()) {
            null -> listOf(Svara.A)
            is Vyanjana -> listOf(Svara.A, initial, Svara.A)
            else -> listOf(Svara.A, initial)
        }
    }
}
