package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.shiksha.Samjna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.2.23: saṃyogāntasya lopaḥ.
 * At the end of a pada, if there is a consonant cluster (saṃyoga),
 * its final member (per 1.1.52) is deleted. The pada need not be the last term: a stem made pada
 * by 1.4.17 before a सुप् (पुम्स् + सु → पुम् + सु → पुंसु) loses its final member too.
 */
object SamyogantasyaLopaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.23",
    text = "संयोगान्तस्य लोपः",
    hindiExplanation = "पदान्त में संयोग (हल्-समुदाय) के अन्तिम वर्ण का लोप होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820023,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.VARNA,
    dependencies = setOf("1.1.7", "1.4.14", "1.1.52")
), DerivationSutra {
    private fun DerivationState.endsInSamyoga(term: DerivationTerm): Boolean =
        term.varnas.size >= 2 && term.varnas.takeLast(2).all { it is Vyanjana } &&
            // 8.2.24 restricts cluster-final deletion after r to s alone, handled there.
            term.varnas[term.varnas.lastIndex - 1] != Vyanjana.RA

    private fun DerivationState.isPada(term: DerivationTerm) =
        samjnas.any { it.targetId == term.id && it.samjna == Samjna.PADA }

    private fun target(context: DerivationState): DerivationTerm? {
        val last = context.terms.lastOrNull() ?: return null
        if (context.isPada(last) && context.endsInSamyoga(last)) return last
        // A non-final pada (1.4.17 before सुप्) is deleted once; the deletion is not repeated.
        return context.terms.dropLast(1).firstOrNull { term ->
            term.kind == dev.panini.derivation.TermKind.PRATIPADIKA && context.isPada(term) &&
                context.endsInSamyoga(term) && context.appliedSutras.count { it == sutra } == 0
        }
    }

    override fun matches(context: DerivationState): Boolean = target(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val term = requireNotNull(target(context))
        // 1.1.52: Delete only the final phonological member of the cluster.
        val deleted = context.deleteTermVarnas(term.id, term.varnas.lastIndex, 1, sutra)
        return DerivationChange(
            state = if (term.id == context.terms.last().id) deleted.copy(stage = DerivationStage.FINAL) else deleted,
            explanation = "8.2.23: Deleted final member of consonant cluster at the end of pada ${term.surface}."
        )
    }
}
