package dev.panini.ashtadhyayi.adhyaya5.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.TermKind
import dev.panini.sankhya.SankhyaResolver
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

import dev.panini.shiksha.Samjna

/**
 * 5.4.17: संख्यायाः क्रियाअभ्यावृत्तिगणने कृत्वसुच्.
 * Introduces Taddhita suffix 'कृत्वसुच्' (कृत्वः - kṛtvas) after numerals to denote repetition/frequency of action.
 * Examples: पञ्चकृत्वः (pañcakṛtvaḥ - 5 times), दशकृत्वः (daśakṛtvaḥ - 10 times), शतकृत्वः (śatakṛtvaḥ).
 */
object SankhyayahKriyaAbhyavrttiKrtvasucSutra : Sutra<DerivationState, DerivationChange>(
    number = "5.4.17",
    text = "संख्यायाः क्रियाअभ्यावृत्तिगणने कृत्वसुच्",
    hindiExplanation = "क्रिया की आवृत्ति गणना अर्थ में संख्या प्रातिपदिक से 'कृत्वसुच्' (कृत्वः) तद्धित प्रत्यय होता है (उदा. पञ्चकृत्वः)।",
    type = SutraType.UTSARGA,
    chapter = 5,
    pada = 4,
    optional = false,
    kramaValue = 540017,
    role = SutraRole.Vidhi,
    action = SutraAction.PRATYAYA_SELECTION,
    scope = SutraScope.DERIVATION,
    stage = SutraStage.PRATYAYA_SELECTION,
), DerivationSutra {

    override fun matches(context: DerivationState): Boolean {
        if (context.samjnas.any { it.samjna == Samjna.PURANA || it.samjna == Samjna.DHATU }) return false
        if (context.terms.any { it.kind == TermKind.DHATU }) return false
        val requested = context.samjnas.map { it.samjna }.toSet()
        val hasTaddhitaRequest = Samjna.KRTVASUC in requested ||
            (Samjna.TADDHITA in requested && requested.none { it in specializedSankhyaTaddhitas })
        if (!hasTaddhitaRequest) return false
        val lastTerm = context.terms.lastOrNull() ?: return false
        val isAlreadyApplied = context.terms.any { it.upadesha == "कृत्वसुच्" || it.surface == "कृत्वः" }
        return !isAlreadyApplied && SankhyaResolver.isSankhya(lastTerm.upadesha, context.samjnas.map { it.samjna }.toSet())
    }

    private val specializedSankhyaTaddhitas = setOf(Samjna.KRTVASUC, Samjna.SUC, Samjna.DHA)

    override fun apply(context: DerivationState): DerivationChange {
        val krtvasTerm = DerivationTerm(
            id = "taddhita_krtvasuc",
            surface = "कृत्वः",
            kind = TermKind.PRATYAYA,
            upadesha = "कृत्वसुच्",
            createdBySutra = sutra,
        )
        return DerivationChange(
            state = context.copy(terms = context.terms + krtvasTerm),
            explanation = "$text: added frequency suffix कृत्वसुच् (कृत्वः)"
        )
    }
}
