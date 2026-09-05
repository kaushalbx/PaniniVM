package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.1.99: sāvu padhāyāś ca.
 * Before non-vocative nominative singular 'su', the penultimate vowel of 'anaḍuh' is lengthened,
 * and with 7.1.98/7.1.100 yields 'anaḍvān'.
 */
object SavupadhayascaSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.99",
    text = "सावुपनधायाश्च",
    hindiExplanation = "असमबुद्धौ सौ विभक्तौ परे अनडुह् अङ्गस्य उपधायाः दीर्घः आम् च भवति (अनड्वान्)।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710099,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        val isAnaduh = stem.upadesha == "अनडुह्" || stem.surface in setOf("अनडुह्", "अनड्वाह्")
        if (!isAnaduh) return false

        val isSu = affix.id == "sup-su" || affix.upadesha == "सुँ"
        return isSu
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        val newTerms = context.terms.dropLast(2) + stem.copy(surface = "अनड्वान्")

        return DerivationChange(
            state = context.copy(terms = newTerms, stage = DerivationStage.FINAL)
                .copy(droppedTerms = context.droppedTerms + dev.panini.derivation.consumeAffixForDrop(affix, sutra)),
            explanation = "7.1.99 & 7.1.100: Derived 'anaḍvān' for 'anaḍuh' before nominative singular su."
        )
    }
}
