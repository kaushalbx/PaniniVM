package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
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
        val pratyaya = context.terms.lastOrNull { it.kind == TermKind.PRATYAYA } ?: return false
        val isSanOrYan = pratyaya.upadesha in setOf("सन्", "यङ्") || pratyaya.id.contains("san") || pratyaya.id.contains("yan")
        val hasAbhyasa = context.terms.any { it.id == "abhyasa" }
        return isSanOrYan && !hasAbhyasa
    }

    override fun apply(context: DerivationState): DerivationChange {
        val rootIndex = context.terms.indexOfFirst { it.kind == TermKind.DHATU }
        if (rootIndex < 0) return DerivationChange(context, "No root found for reduplication.")
        val root = context.terms[rootIndex]

        val abhyasaSurface = computeAbhyasa(root.surface)
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

    private fun computeAbhyasa(rootSurface: String): String = when (rootSurface) {
        "भू" -> "बु"
        "कृ" -> "चि"
        "पठ्" -> "प"
        "जि" -> "जि"
        "चि" -> "चि"
        "नी" -> "नि"
        else -> if (rootSurface.isNotEmpty()) rootSurface.take(1) else rootSurface
    }
}
