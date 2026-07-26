package dev.panini.ashtadhyayi.adhyaya6.pada3

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 6.3.76: एकादिश्चैकस्य चादुक्। */
object EkadishCaikasyaCadukSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.3.76", text = "एकादिश्चैकस्य चादुक्", hindiExplanation = "एकादिगणे एकस्य आदुक् आगमः।",
    type = SutraType.NITYA, chapter = 6, pada = 3, optional = false, kramaValue = 630076,
    role = SutraRole.Vidhi, action = SutraAction.AGAMA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = context.terms.size > 1 &&
        context.terms[0].surface == "एक" && context.terms[1].surface == "दश" &&
        context.samjnas.any { it.targetId == context.terms[1].id && it.samjna == Samjna.SANKHYA }

    override fun apply(context: DerivationState): DerivationChange {
        val target = context.terms[0]
        return DerivationChange(
            context.replaceTerm(target.id, target.copy(surface = "एका")),
            "6.3.76: आदुक् gives एका in एकादश."
        )
    }
}
