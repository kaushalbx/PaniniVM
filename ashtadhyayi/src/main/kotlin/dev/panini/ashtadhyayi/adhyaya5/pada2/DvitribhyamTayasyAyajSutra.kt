package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 5.2.43: द्वित्रिभ्याम् तयस्यायज्वोत्पत्तौ.
 * Introduces alternate portion suffix 'आयच्' (अय - aya) after 'द्वि' and 'त्रि' stems yielding 'द्वयम्' (dvayam) and 'त्रयम्' (trayam).
 */
object DvitribhyamTayasyAyajSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.43",
    text = "द्वित्रिभ्याम् तयस्यायज्वोत्पत्तौ",
    hindiExplanation = "द्वि तथा त्रि प्रातिपदिक से अवयव अर्थ में तयप् के स्थान पर विकल्प से आयच् (अय) प्रत्यय होता है (उदा. द्वयम्, त्रयम्)।",
    type = SutraType.VIBHASHA,
    chapter = 5,
    pada = 2,
    optional = true,
    kramaValue = 520043,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
    blocks = setOf("5.2.42"),
), DerivationSutra {

    private val supportedStems = setOf("द्वि", "त्रि")

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val hasAvayavaRequest = context.samjnas.any { it.samjna == Samjna.AVAYAVA || it.samjna == Samjna.TADDHITA }
        if (!hasAvayavaRequest) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "आयच्" || it.surface == "अय" }
        return !isAlreadyApplied && lastTerm.upadesha in supportedStems
    }

    override fun apply(context: DerivationState): DerivationChange {
        val ayacTerm = DerivationTerm(
            id = "taddhita_ayac",
            surface = "अय",
            kind = TermKind.PRATYAYA,
            upadesha = "आयच्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + ayacTerm),
            explanation = "$text: added alternate portion suffix आयच् (अय)"
        )
    }
}
