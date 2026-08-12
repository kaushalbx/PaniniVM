package dev.panini.ashtadhyayi.adhyaya4.pada1

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.derivation.TermKind
import dev.panini.ganapatha.GanaPatha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 4.1.10: न षट्स्वस्रादिभ्यः. */
object NaShatsvasradibhyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.10", text = "न षट्स्वस्रादिभ्यः",
    hindiExplanation = "षट्-संज्ञक तथा स्वस्रादि प्रातिपदिकों से स्त्रीप्रत्यय नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 4, pada = 1, optional = false, kramaValue = 410010,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        "STRI_PRATYAYA" !in context.blockedSutras &&
            HasMorphosyntax(linga = Linga.STRI).matches(context) && context.terms.any {
            it.kind == TermKind.PRATIPADIKA &&
                (context.samjnas.any { assignment ->
                    assignment.targetId == it.id && assignment.samjna == Samjna.SHAT
                } || it.surface == "षट्" || GanaPatha.isEligibleMember(46, it.surface, it.lexicalUses))
        }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.blockSutra("STRI_PRATYAYA", "4.1.10"),
        "4.1.10 blocks every feminine affix after a ṣaṭ-designated or eligible स्वस्रादि stem.",
    )
}
