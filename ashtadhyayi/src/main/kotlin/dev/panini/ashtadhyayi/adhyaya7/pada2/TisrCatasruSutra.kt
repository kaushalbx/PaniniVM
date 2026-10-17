package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 7.2.99: tri-caturoḥ striyāṁ tisṛ-catasṛ.
 * Substitutes 'tisṛ' for 'tri' and 'catasṛ' for 'catur' before a case affix in feminine gender.
 */
object TisrCatasruSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.2.99",
    text = "त्रिचतुरोः स्त्रियां तिसृचतसृ",
    hindiExplanation = "स्त्रीलिङ्ग में विभक्तौ परे होने पर 'त्रि' के स्थान पर 'तिसृ' और 'चतुर्' के स्थान पर 'चतसृ' आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 2,
    optional = false,
    kramaValue = 720099,
    role = SutraRole.Apavada,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    private val tri = listOf(Vyanjana.TA, Vyanjana.RA, Svara.I)
    private val catur = listOf(Vyanjana.CA, Svara.A, Vyanjana.TA, Svara.U, Vyanjana.RA)

    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        if (context.effectiveContext.rupa.linga != Linga.STRI) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // This ādeśa applies to the original surface once. The retained
        // upadeśa must not cause it to reapply after later ṛ-stem operations.
        if (stem.varnas != tri && stem.varnas != catur) return false

        return affix.sourceSupAffix != null || context.droppedTerms.any { it.sourceSupAffix != null }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val replaced = when (stem.varnas) {
            catur -> context.replaceTermVarnaRange(stem.id, 3, 2,
                listOf(Svara.A, Vyanjana.SA, Svara.R), mapOf(0 to 0, 2 to 1), sutra)
            tri -> context.replaceTermVarnaRange(stem.id, 1, 2,
                listOf(Svara.I, Vyanjana.SA, Svara.R), mapOf(0 to 1, 2 to 0), sutra)
            else -> error("7.2.99 requires a current tri or catur stem")
        }

        return DerivationChange(
            state = replaced
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.99 substitutes the prescribed feminine numeral allomorph before vibhakti."
        )
    }
}
