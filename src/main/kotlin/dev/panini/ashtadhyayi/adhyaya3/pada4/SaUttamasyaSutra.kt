package dev.panini.ashtadhyayi.adhyaya3.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.Lakara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
