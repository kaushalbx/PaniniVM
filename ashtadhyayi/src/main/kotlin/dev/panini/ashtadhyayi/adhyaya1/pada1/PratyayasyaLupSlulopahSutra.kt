package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SamjnaSetDefinitionArtha
import dev.panini.sutra.Samjni
import dev.panini.sutra.SamjnaSamuhVidhayakaSutra
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.61 प्रत्ययस्य लुप्श्लुलोपाः.
 * Classifies the deletion/elision of an affix into lup, ślu, or lopa.
 */
object PratyayasyaLupSlulopahSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.61", text = "प्रत्ययस्य लुप्श्लुलोपाः",
    hindiExplanation = "प्रत्यय के अदर्शन/लोप की सञ्ज्ञा 'लुप्', 'श्लु' अथवा 'लोप' होती है।",
    type = SutraType.SAMJNA, chapter = 1, pada = 1, optional = false, kramaValue = 110061,
    role = SutraRole.Samjna, action = SutraAction.LOPA, scope = SutraScope.DERIVATION,
    inputs = setOf(SutraInput.PRATYAYA),
), DerivationSutra, SamjnaSamuhVidhayakaSutra {
    override val artha = SamjnaSetDefinitionArtha(
        samjni = Samjni.PRATYAYA_ADARSHANA,
        samjnas = setOf(
            dev.panini.shiksha.Samjna.LUK,
            dev.panini.shiksha.Samjna.SHLU,
            dev.panini.shiksha.Samjna.LUP,
        ),
    )

    override fun matches(context: DerivationState): Boolean =
        "1.1.61" !in context.activeAdhikaras

    override fun apply(context: DerivationState): DerivationChange =
        DerivationChange(
            state = context.activateAdhikara("1.1.61"),
            explanation = "1.1.61 defines lup, ślu, lopa elisions for affixes.",
        )
}
