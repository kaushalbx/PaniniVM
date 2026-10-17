package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.derivation.isPadaFinalVarna
import dev.panini.derivation.firstVarnaAfterTerm
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.3.23: mo'nusvāraḥ.
 * Word-final 'm' becomes Anusvāra when followed by a consonant (hal).
 */
object MonusvarahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.23",
    text = "मोऽनुस्वारः",
    hindiExplanation = "पदान्त मकार के स्थान पर अनुस्वार होता है यदि बाद में कोई व्यञ्जन हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830023,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        return context.terms.any { eligible(context, it) }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val left = context.terms.first { eligible(context, it) }
        val replacement = listOf(Ayogavaha.ANUSVARA)

        return DerivationChange(
            state = context.replaceTermVarna(left.id, left.varnas.lastIndex, replacement, sutra),
            explanation = "8.3.23: Final 'm' became Anusvāra before consonant."
        )
    }

    private fun eligible(context: DerivationState, left: dev.panini.derivation.DerivationTerm): Boolean {
        if (!context.isPadaFinalVarna(left, left.varnas.lastIndex) || left.varnas.lastOrNull() != Vyanjana.MA) return false
        val index = context.terms.indexOfFirst { it.id == left.id }
        val right = context.terms.drop(index + 1).firstOrNull { it.varnas.isNotEmpty() } ?: return false
        return !context.isBlockedAtBoundary(sutra, left.id, right.id) &&
            Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, right.varnas.first())
    }
}

/**
 * 8.3.24: naścāpadāntasya jhali.
 * Non-word-final 'n' and 'm' become Anusvāra when followed by a jhal sound.
 */
object NashcapadantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.24",
    text = "नश्चापदान्तस्य झलि",
    hindiExplanation = "अपदान्त 'न' और 'म' के स्थान पर अनुस्वार होता है यदि बाद में झल् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830024,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        return target(context) != null
    }

    override fun apply(context: DerivationState): DerivationChange {
        val (termIndex, localIndex) = requireNotNull(target(context))
        val targetTerm = context.terms[termIndex]
        val source = targetTerm.varnas[localIndex]
        val replacement = listOf(Ayogavaha.ANUSVARA)

        return DerivationChange(
            state = context.replaceTermVarna(
                targetTerm.id, localIndex, replacement, sutra,
            ),
            explanation = "8.3.24: Internal $source became Anusvāra before jhal."
        )
    }

    private fun target(context: DerivationState): Pair<Int, Int>? {
        // A retained grammatical affix is not a pada boundary. Only unfinished
        // it-processing prevents inspection of its operative phonemes.
        if (context.terms.any { it.itProcessingPending || it.itDesignations.isNotEmpty() ||
                it.deferredItDesignations.isNotEmpty() }) return null
        context.terms.forEachIndexed { termIndex, term ->
            term.varnas.forEachIndexed { index, varna ->
                if (varna in setOf(Vyanjana.NA, Vyanjana.MA)) {
                    val padaFinal = context.isPadaFinalVarna(term, index)
                    val next = term.varnas.getOrNull(index + 1)
                        ?: context.firstVarnaAfterTerm(term.id)
                    if (!padaFinal && next != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, next)) {
                        return termIndex to index
                    }
                }
            }
        }
        return null
    }
}
