package dev.panini.ashtadhyayi.adhyaya7.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.core.Lakara
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/** 7.4.70: अत आदेः. */
object AtaAdesSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.4.70", text = "अत आदेः",
    hindiExplanation = "लिट् में अकार से आरम्भ होने वाले अभ्यास का अकार दीर्घ होकर आकार होता है।",
    type = SutraType.NITYA, chapter = 7, pada = 4, optional = false, kramaValue = 740070,
    role = SutraRole.Vidhi, action = SutraAction.ADESHA, scope = SutraScope.DHATU,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val abhyasa = context.terms.firstOrNull { it.id == "abhyasa" } ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LIT &&
            context.samjnas.any { it.targetId == abhyasa.id && it.samjna == Samjna.ABHYASA } &&
            abhyasa.surface.startsWith('अ')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val abhyasa = context.terms.first { it.id == "abhyasa" }
        val lengthened = 'आ' + abhyasa.surface.drop(1)
        return DerivationChange(
            context.replaceTerm(abhyasa.id, abhyasa.copy(surface = lengthened)),
            "7.4.70 lengthens the initial अ of the abhyāsa ${abhyasa.surface} in लिट्.",
        )
    }
}
