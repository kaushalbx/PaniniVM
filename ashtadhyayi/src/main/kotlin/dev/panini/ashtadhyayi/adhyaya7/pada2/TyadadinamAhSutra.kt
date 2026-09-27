package dev.panini.ashtadhyayi.adhyaya7.pada2

import dev.panini.core.Linga
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
        if (stem.surface == "अयम्") return false
        val isTyadadi = listOf(stem.upadesha, stem.surface).any { text ->
            SarvadiGana.antarGanasContaining(text).any { it.name == "त्यदादिः" }
        }
        val hasConsonantEnding = stem.varnas.lastOrNull() is Vyanjana || stem.varnas in setOf(idam, dvi)
        return isTyadadi && hasConsonantEnding &&
            (affix.id.startsWith("sup-") || context.droppedTerms.any { it.id.startsWith("sup-") })
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val result = when (stem.varnas) {
            idam -> listOf(Svara.I, Vyanjana.MA, Svara.A)
            // In the feminine derivation, 4.1.4 supplies टाप् after this
            // substitution; retain its आ so 7.1.18 can operate on द्वा + औ.
            dvi -> listOf(Vyanjana.DA, Vyanjana.VA, if (context.effectiveContext.rupa.linga == Linga.STRI) Svara.AA else Svara.A)
            else -> stem.varnas.dropLast(1)
        }

        return DerivationChange(
            state = context.substituteTermVarnas(stem.id, result, stem.varnas.last(), listOf(Svara.A), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.2.102 substitutes 'a' for the final letter of tyadādi stem ${stem.surface}."
        )
    }

    private val idam = listOf(Svara.I, Vyanjana.DA, Svara.A, Vyanjana.MA)
    private val dvi = listOf(Vyanjana.DA, Vyanjana.VA, Svara.I)
}
