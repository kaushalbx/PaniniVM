package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.core.ItMarker
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 1.1.26: क्तक्तवतू निष्ठा / 3.2.102: निष्ठा.
 * Introduces the past participle suffixes 'क्त' (kta) and 'क्तवतु' (ktavatu) after a verbal root.
 */
object NisthaSutra : Sutra<DerivationState, DerivationChange>(
    number = "1.1.26",
    text = "क्तक्तवतू निष्ठा",
    hindiExplanation = "क्त तथा क्तवतु प्रत्ययों की निष्ठा संज्ञा होती है। भूतकाल में धातु से निष्ठा प्रत्यय होते हैं।",
    type = SutraType.UTSARGA,
    chapter = 1,
    pada = 1,
    optional = false,
    kramaValue = 110026,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = dev.panini.sutra.SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        val isNisthaRequested = context.samjnas.any { it.samjna == Samjna.KTA || it.samjna == Samjna.KTAVATU }
        val hasPratyaya = context.terms.any { it.upadesha == "क्त" || it.upadesha == "क्तवतु" }
        return isNisthaRequested && !hasPratyaya
    }

    override fun apply(context: DerivationState): DerivationChange {
        val isKtavatu = context.samjnas.any { it.samjna == Samjna.KTAVATU }
        val pratyayaTerm = if (isKtavatu) {
            DerivationTerm(
                id = "ktavatu_pratyaya",
                surface = "तवत्",
                kind = TermKind.PRATYAYA,
                itMarkers = setOf(ItMarker.KIT, ItMarker.U),
                upadesha = "क्तवतु",
                createdBySutra = sutra,
            )
        } else {
            DerivationTerm(
                id = "kta_pratyaya",
                surface = "क्त",
                kind = TermKind.PRATYAYA,
                upadesha = "क्त",
                createdBySutra = sutra,
                itProcessingPending = true,
            )
        }

        return DerivationChange(
            state = context.copy(
                terms = context.terms + pratyayaTerm,
                stage = DerivationStage.PRATYAYA_SELECTED,
            ),
            explanation = "1.1.26/3.2.102 introduces suffix ${pratyayaTerm.upadesha}."
        )
    }
}
