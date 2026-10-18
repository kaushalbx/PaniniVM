package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.TaddhitaAffix
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.SamjnaAssignment
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 1.1.38 तद्धितश्चासर्वविभक्तिः: an asarvavibhakti taddhitānta is an avyaya. */
object TaddhitasCasarvavibhaktihSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.38", text = "तद्धितश्चासर्वविभक्तिः",
    hindiExplanation = "जिस तद्धितान्त से सभी विभक्तियाँ नहीं होतीं उसकी अव्यय संज्ञा होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110038,
    role = SutraRole.Samjna, action = SutraAction.SAMJNA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.lastIndex - 1]
        val affix = context.terms.last()
        return stem.kind == TermKind.PRATIPADIKA &&
            affix.sourceTaddhitaAffix in asarvavibhaktiAffixes &&
            SamjnaAssignment(stem.id, Samjna.AVYAYA) !in context.samjnas
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.lastIndex - 1]
        return DerivationChange(
            context.withSamjnas(setOf(SamjnaAssignment(stem.id, Samjna.AVYAYA))),
            "1.1.38 assigns avyaya to the asarvavibhakti taddhitānta carrier.",
        )
    }

    private val asarvavibhaktiAffixes = setOf(TaddhitaAffix.TASIL, TaddhitaAffix.TRAL)
}
