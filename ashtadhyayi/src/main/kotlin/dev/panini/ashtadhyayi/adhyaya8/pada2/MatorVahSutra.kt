package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.2.9: मादुपधायाश्च मतोर्वोऽध्यादिभ्यः.
 * Replaces 'म' of 'मतुप्' with 'व' (vatup -> वत्) after stems ending in 'm' or 'a' or having 'a'/'m' in penult.
 */
object MatorVahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.9",
    text = "मादुपधायाश्च मतोर्वोऽध्यादिभ्यः",
    hindiExplanation = "मकारान्त, अकारान्त तथा म-कार उपधा वाले अङ्ग से परे मतुप् के मकार को वकार होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820009,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val matupTerm = context.terms.firstOrNull { it.upadesha == "मतुप्" || it.id.contains("matup") } ?: return false
        val stemTerm = context.terms.firstOrNull { it.kind == TermKind.PRATIPADIKA } ?: return false
        return matupTerm.surface == "मत्" && isAdantaOrM(stemTerm.surface)
    }

    private fun isAdantaOrM(stem: String): Boolean {
        if (stem.isEmpty()) return false
        if (stem.endsWith("म्") || stem.endsWith("म")) return true
        val matras = setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'े', 'ै', 'ो', 'ौ', 'ं', 'ः', '्')
        return stem.last() !in matras
    }

    override fun apply(context: DerivationState): DerivationChange {
        val matupTerm = context.terms.first { it.upadesha == "मतुप्" || it.id.contains("matup") }
        val updatedTerm = matupTerm.copy(surface = "वत्")
        return DerivationChange(
            state = context.replaceTerm(matupTerm.id, updatedTerm).copy(
                stage = maxOf(context.stage, DerivationStage.ANGAKARYA)
            ),
            explanation = "8.2.9 changes मतुप् (मत् → वत्) after a/m-ending stem."
        )
    }
}
