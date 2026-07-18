package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.1.4: अदभ्यस्तात्. झि is replaced by अत् after a reduplicated verbal base. */
object AdabhyastatSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.4",
    text = "अदभ्यस्तात्",
    hindiExplanation = "अभ्यस्त धातु से परे झि के स्थान पर अत् आदेश होता है।",
    type = SutraType.APAVADA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710004,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("7.1.3"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val affix = context.terms.lastOrNull() ?: return false
        return affix.upadesha == "झि" && affix.surface.startsWith('झ') &&
            context.samjnas.any { it.targetId == "abhyasa" && it.samjna == Samjna.ABHYASA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val affix = context.terms.last()
        return DerivationChange(
            context.replaceTerm(affix.id, affix.copy(surface = "अति")),
            "7.1.4 substitutes अत् for झि after the reduplicated base.",
        )
    }
}
