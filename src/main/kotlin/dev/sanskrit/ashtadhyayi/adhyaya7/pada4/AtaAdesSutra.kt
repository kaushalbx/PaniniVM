package dev.sanskrit.ashtadhyayi.adhyaya7.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.shiksha.Samjna
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
