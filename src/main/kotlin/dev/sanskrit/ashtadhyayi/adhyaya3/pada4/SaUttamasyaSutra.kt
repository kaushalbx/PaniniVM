package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.Lakara
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/** 3.4.98: स उत्तमस्य. Optionally deletes स् from LET first-person वस् and मस्. */
object SaUttamasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.98",
    text = "स उत्तमस्य",
    hindiExplanation = "लेट् के उत्तमपुरुष प्रत्यय के अन्त्य सकार का विकल्प से लोप होता है।",
    type = SutraType.VIBHASHA,
    chapter = 3,
    pada = 4,
    optional = true,
    kramaValue = 340098,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LET &&
            ending.upadesha in setOf("वस्", "मस्") && ending.surface.endsWith("स्")
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = ending.surface.dropLast(2))),
            "3.4.98 optionally deletes final स् from the LET first-person ending.",
        )
    }
}
