package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.109: सिजभ्यस्तविदिभ्यश्च. झि is replaced by जुस् after an abhyasta base. */
object SijabhyastavidibhyashCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.109",
    text = "सिजभ्यस्तविदिभ्यश्च",
    hindiExplanation = "सिच्, अभ्यस्त और विद् के बाद झि के स्थान पर जुस् आदेश होता है।",
    type = SutraType.APAVADA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340109,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.PRATYAYA,
    blocks = setOf("7.1.4"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LANG &&
            ending.upadesha == TingAffix.JHI.upadesha &&
            ending.surface.startsWith('झ') &&
            context.samjnas.any { it.targetId == "abhyasa" && it.samjna == Samjna.ABHYASA }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = "उस्")),
            "3.4.109 replaces झि with जुस् after the reduplicated base.",
        )
    }
}
