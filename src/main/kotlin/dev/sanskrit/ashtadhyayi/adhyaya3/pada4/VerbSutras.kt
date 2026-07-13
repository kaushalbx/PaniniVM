package dev.sanskrit.ashtadhyayi.adhyaya3.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.TingAffix
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

object TiptasjhiSutra : Sutra<DerivationState, DerivationChange>(
    number = "3.4.78",
    text = "तिप्तस्झि",
    hindiExplanation = "लट् के स्थान पर पुरुष-वचनानुसार तिङ् प्रत्यय होता है।",
    type = SutraType.NITYA,
    chapter = 3,
    pada = 4,
    optional = false,
    kramaValue = 340078,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.PRATYAYA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean =
        context.terms.lastOrNull()?.id == "lat" &&
            context.effectiveContext.rupa.let { it.purusha != null && it.vacana != null }

    override fun apply(context: DerivationState): DerivationChange = DerivationChange(
        context.replaceTerm(
            "lat", context.effectiveContext.rupa.let { morphology ->
                requireNotNull(TingAffix.select(requireNotNull(morphology.purusha), requireNotNull(morphology.vacana))).term()
            }
        ),
        "3.4.78 substitutes the requested tiṅ termination for लट्.",
    )
}
