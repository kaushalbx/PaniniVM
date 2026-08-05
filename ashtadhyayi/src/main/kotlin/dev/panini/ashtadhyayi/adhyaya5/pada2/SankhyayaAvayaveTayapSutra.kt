package dev.panini.ashtadhyayi.adhyaya5.pada2

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sankhya.SankhyaResolver
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 5.2.42: संख्याया अवयवे तयप्.
 * Introduces Taddhita portion/collective suffix 'तयप्' (तय - taya) after numerals in the sense of components or parts.
 * Examples: पञ्चतयम् (pañcatayam), दशतयम् (daśatayam), द्वितयम् (dvitayam).
 */
object SankhyayaAvayaveTayapSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.2.42",
    text = "संख्याया अवयवे तयप्",
    hindiExplanation = "संख्यावाचक प्रातिपदिक से अवयव (भाग/समूह) अर्थ में 'तयप्' (तय) तद्धित प्रत्यय होता है (उदा. पञ्चतयम्, दशतयम्)।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 2,
    optional = false,
    kramaValue = 520042,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val hasAvayavaRequest = context.samjnas.any { it.samjna == Samjna.AVAYAVA || it.samjna == Samjna.TADDHITA }
        if (!hasAvayavaRequest) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "तयप्" || it.surface == "तय" }
        return !isAlreadyApplied && SankhyaResolver.isSankhya(lastTerm.upadesha, context.samjnas.map { it.samjna }.toSet())
    }

    override fun apply(context: DerivationState): DerivationChange {
        val tayapTerm = DerivationTerm(
            id = "taddhita_tayap",
            surface = "तय",
            kind = TermKind.PRATYAYA,
            upadesha = "तयप्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + tayapTerm),
            explanation = "$text: added portion suffix तयप् (तय)"
        )
    }
}
