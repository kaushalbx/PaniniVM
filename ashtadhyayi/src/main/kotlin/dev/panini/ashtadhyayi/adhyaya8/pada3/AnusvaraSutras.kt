package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
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
        return context.terms.zipWithNext().any { (left, right) ->
            val isBoundaryTerm = context.samjnas.any {
                it.targetId == left.id && it.samjna in setOf(Samjna.PADA, Samjna.UPASARGA)
            }
            isBoundaryTerm && left.varnas.lastOrNull() == Vyanjana.MA &&
                right.varnas.firstOrNull()?.let { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, it) } == true
        }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val left = context.terms.zipWithNext().first { (candidate, right) ->
            context.samjnas.any { it.targetId == candidate.id && it.samjna in setOf(Samjna.PADA, Samjna.UPASARGA) } &&
                candidate.varnas.lastOrNull() == Vyanjana.MA &&
                right.varnas.firstOrNull()?.let { Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, it) } == true
        }.first
        val replacement = listOf(Ayogavaha.ANUSVARA)

        return DerivationChange(
            state = context.replaceTermVarna(left.id, left.varnas.lastIndex, replacement, sutra),
            explanation = "8.3.23: Final 'm' became Anusvāra before consonant."
        )
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
        if (context.terms.any { it.kind == TermKind.PRATYAYA }) return null
        context.terms.forEachIndexed { termIndex, term ->
            term.varnas.forEachIndexed { index, varna ->
                if (varna in setOf(Vyanjana.NA, Vyanjana.MA)) {
                    val padaFinal = index == term.varnas.lastIndex && context.samjnas.any {
                        it.targetId == term.id && it.samjna in setOf(Samjna.PADA, Samjna.UPASARGA)
                    }
                    val next = term.varnas.getOrNull(index + 1)
                        ?: context.terms.getOrNull(termIndex + 1)?.varnas?.firstOrNull()
                    if (!padaFinal && next != null && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, next)) {
                        return termIndex to index
                    }
                }
            }
        }
        return null
    }
}
