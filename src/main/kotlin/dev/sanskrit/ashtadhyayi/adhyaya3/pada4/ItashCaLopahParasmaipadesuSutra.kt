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

/** 3.4.97: इतश्च लोपः परस्मैपदेषु. Gives the secondary-ending LET variants. */
object ItashCaLopahParasmaipadesuSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.97",
    text = "इतश्च लोपः परस्मैपदेषु",
    hindiExplanation = "लेट् के परस्मैपद प्रत्ययों के अन्त्य इकार का विकल्प से लोप होता है।",
    type = SutraType.VIBHASHA,
    chapter = 3,
    pada = 4,
    optional = true,
    kramaValue = 340097,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val ending = context.terms.lastOrNull() ?: return false
        return context.effectiveContext.rupa.lakara == Lakara.LET &&
            ending.upadesha in setOf("तिप्", "झि", "सिप्") && ending.surface.endsWith('ि')
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ending = context.terms.last()
        return DerivationChange(
            context.replaceTerm(ending.id, ending.copy(surface = ending.surface.dropLast(1) + "्")),
            "3.4.97 optionally deletes the final इ of the LET Parasmaipada ending.",
        )
    }
}
