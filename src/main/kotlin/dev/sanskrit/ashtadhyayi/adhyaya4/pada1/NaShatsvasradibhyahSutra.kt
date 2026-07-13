package dev.sanskrit.ashtadhyayi.adhyaya4.pada1

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.ganapatha.GanaPatha
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 4.1.10: न षट्स्वस्रादिभ्यः. */
object NaShatsvasradibhyahSutra : Sutra<DerivationState, DerivationChange>(
    number = "4.1.10", text = "न षट्स्वस्रादिभ्यः",
    hindiExplanation = "षट्-संज्ञक तथा स्वस्रादि प्रातिपदिकों से स्त्रीप्रत्यय नहीं होता।",
    type = SutraType.NISHEDHA, chapter = 4, pada = 1, optional = false, kramaValue = 410010,
    role = SutraRole.Nishedha, action = SutraAction.NISHEDHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        HasMorphosyntax(linga = Linga.STRI).matches(context) && context.terms.any {
            it.kind == TermKind.PRATIPADIKA &&
                (it.surface == "षट्" || GanaPatha.isEligibleMember(46, it.surface, it.lexicalUses))
        }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.blockSutra("STRI_PRATYAYA", "4.1.10"),
        "4.1.10 blocks every feminine affix after a ṣaṭ-designated or eligible स्वस्रादि stem.",
    )
}
