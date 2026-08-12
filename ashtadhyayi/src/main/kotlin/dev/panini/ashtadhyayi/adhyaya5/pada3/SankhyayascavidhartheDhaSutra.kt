package dev.panini.ashtadhyayi.adhyaya5.pada3

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
 * 5.3.42: संख्यायाश्चविधार्थे धा.
 * Introduces Taddhita suffix 'धा' (dhā) after numerals in the sense of kinds/parts/division.
 * Examples: द्विधा (dvidhā), त्रिधा (tridhā), पञ्चधा (pañcadhā), दशधा (daśadhā).
 */
object SankhyayascavidhartheDhaSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.3.42",
    text = "संख्यायाश्चविधार्थे धा",
    hindiExplanation = "संख्यावाचक प्रातिपदिक से प्रकार/विभाग अर्थ में 'धा' तद्धित प्रत्यय होता है (उदा. द्विधा, त्रिधा, पञ्चधा)।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 3,
    optional = false,
    kramaValue = 530042,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val requested = context.samjnas.map { it.samjna }.toSet()
        val hasTaddhitaRequest = Samjna.DHA in requested ||
            (Samjna.TADDHITA in requested && requested.none { it in specializedSankhyaTaddhitas })
        if (!hasTaddhitaRequest) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "धा" || it.surface == "धा" }
        return !isAlreadyApplied && SankhyaResolver.isSankhya(lastTerm.upadesha, context.samjnas.map { it.samjna }.toSet())
    }

    private val specializedSankhyaTaddhitas = setOf(Samjna.KRTVASUC, Samjna.SUC, Samjna.DHA)


    override fun apply(context: DerivationState): DerivationChange {
        val dhaTerm = DerivationTerm(
            id = "taddhita_dha",
            surface = "धा",
            kind = TermKind.PRATYAYA,
            upadesha = "धा",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + dhaTerm),
            explanation = "$text: added Taddhita division suffix धा (dhā)"
        )
    }
}
