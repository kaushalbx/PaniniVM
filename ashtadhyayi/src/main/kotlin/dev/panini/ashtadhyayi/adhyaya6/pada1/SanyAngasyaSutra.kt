package dev.panini.ashtadhyayi.adhyaya6.pada1

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
 * 6.1.9: सन्यङ्गोः.
 * Performs root reduplication before the desiderative 'सन्' or frequentative 'यङ्' suffix.
 */
object SanyAngasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.9",
    text = "सन्यङ्गोः",
    hindiExplanation = "सन् तथा यङ् परे होने पर अनभ्यास अङ्ग का द्वित्व होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610009,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val pratyaya = context.terms.lastOrNull {
            it.kind == TermKind.PRATYAYA && it.upadesha in setOf("सन्", "यङ्")
        } ?: return false
        val isSanOrYan = pratyaya.upadesha in setOf("सन्", "यङ्")
        val hasAbhyasa = context.terms.any { it.id == "abhyasa" }
        return isSanOrYan && !hasAbhyasa
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (rootIndex < 0) return DerivationChange(context, "No root found for reduplication.")
        val root = context.terms[rootIndex]

        val abhyasaVarnas = computeAbhyasa(root.varnas)
        val abhyasaSurface = abhyasaVarnas.toDevanagari()
        val abhyasaTerm = DerivationTerm(
            id = "abhyasa",
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
            explanation = "6.1.9 performs reduplication of ${root.surface} into abhyāsa $abhyasaSurface."
        )
    }

    private fun computeAbhyasa(root: List<Varna>): List<Varna> = when (root) {
        listOf<Varna>(Vyanjana.BHA, Svara.UU) -> listOf(Vyanjana.BA, Svara.U)
        listOf<Varna>(Vyanjana.KA, Svara.R) -> listOf(Vyanjana.CA, Svara.I)
        listOf<Varna>(Vyanjana.PA, Svara.A, Vyanjana.THA) -> listOf(Vyanjana.PA, Svara.A)
        listOf<Varna>(Vyanjana.JA, Svara.I) -> root
        listOf<Varna>(Vyanjana.CA, Svara.I) -> root
        listOf<Varna>(Vyanjana.NA, Svara.II) -> listOf(Vyanjana.NA, Svara.I)
        else -> when (val initial = root.firstOrNull()) {
            null -> emptyList()
            is Vyanjana -> listOf(initial, Svara.A)
            else -> listOf(initial)
        }
    }
}
