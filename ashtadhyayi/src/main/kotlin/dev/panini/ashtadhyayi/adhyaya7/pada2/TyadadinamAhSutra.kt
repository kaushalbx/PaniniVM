package dev.panini.ashtadhyayi.adhyaya7.pada2

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
import dev.panini.ganapatha.SarvadiGana
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana

/**
 * 7.2.102: tyadādīnām aḥ.
 * Substitutes 'a' for the final letter of tyadādi pronominal stems before a case affix (vibhakti).
 */
object TyadadinamAhSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.102",
    text = "त्यदादीनामः",
    hindiExplanation = "त्यदादि गण के शब्दों के अन्त्य वर्ण के स्थान पर अकार आदेश होता है विभक्तौ परे होने पर।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720102,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()
        if (context.substitutions.any { it.targetId == stem.id && it.sutra == "7.2.108" }) return false
        if (stem.varnas == listOf(Svara.A, Vyanjana.YA, Svara.A, Vyanjana.MA)) return false
        val isTyadadi = SarvadiGana.antarGanasContaining(stem.upadesha).any { it.name == "त्यदादिः" }
        // The prescription targets the lexical ending, not any later consonant
        // (in particular not num newly inserted by 7.1.72).
        val hasConsonantEnding = stem.varnas.lastOrNull() is Vyanjana &&
            stem.varnas.lastOrNull() == stem.upadeshaVarnas.lastOrNull() || stem.varnas in setOf(idam, dvi)
        return isTyadadi && hasConsonantEnding &&
            (affix.sourceSupAffix != null || context.droppedTerms.any { it.sourceSupAffix != null })
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val replaced = context.replaceTermVarna(stem.id, stem.varnas.lastIndex, listOf(Svara.A), sutra)
        val current = replaced.terms.single { it.id == stem.id }
        val pending = if (current.varnas.takeLast(2) == listOf(Svara.A, Svara.A))
            dev.panini.derivation.PendingBaseComposition(current.varnas.lastIndex - 1,
                current.varnas.takeLast(2), sutra) else null
        return DerivationChange(
            replaced.replaceTerm(stem.id, current.copy(pendingBaseComposition = pending))
                .copy(stage = DerivationStage.ANGAKARYA),
            "7.2.102 replaces the exact final occurrence; exposed base composition remains pending.",
        )
    }

    private val idam = listOf(Svara.I, Vyanjana.DA, Svara.A, Vyanjana.MA)
    private val dvi = listOf(Vyanjana.DA, Vyanjana.VA, Svara.I)
}
