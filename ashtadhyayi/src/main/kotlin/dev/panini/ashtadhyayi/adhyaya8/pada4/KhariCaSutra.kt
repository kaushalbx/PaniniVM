package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varna
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.55: khari ca.
 * Substitutes car (unaspirated voiceless stops) for jhal (stops + fricatives)
 * when followed by khar (voiceless sounds).
 */
object KhariCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.55",
    text = "खरि च",
    hindiExplanation = "झल् वर्णों के स्थान पर चर् आदेश होता है यदि बाद में खर् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840055,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findTarget(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val target = requireNotNull(findTarget(context))
        val leftTerm = context.terms[target.termIndex]
        val source = leftTerm.varnas[target.varnaIndex]
        val substitute = substituteFor(source)

        return DerivationChange(
            state = context.replaceTermVarna(leftTerm.id, target.varnaIndex, listOf(substitute), sutra),
            explanation = "8.4.55: Devoiced ${source.devanagari} to ${substitute.devanagari} before voiceless sound."
        )
    }

    private fun substituteFor(source: Varna): Varna = when (source) {
        Vyanjana.JA, Vyanjana.JHA -> Vyanjana.CA
        Vyanjana.DDA, Vyanjana.DDHA -> Vyanjana.TTA
        Vyanjana.DA, Vyanjana.DHA -> Vyanjana.TA
        Vyanjana.GA, Vyanjana.GHA -> Vyanjana.KA
        Vyanjana.BA, Vyanjana.BHA -> Vyanjana.PA
        else -> source
    }

    private fun findTarget(context: DerivationState): Target? {
        val engine = Ashtadhyayi.pratyaharaEngine
        for (termIndex in 0 until context.terms.lastIndex) {
            val leftVarnas = context.terms[termIndex].varnas
            val varnaIndex = leftVarnas.lastIndex
            val left = leftVarnas.lastOrNull() ?: continue
            val right = context.terms[termIndex + 1].varnas.firstOrNull() ?: continue
            if (engine.contains(Pratyahara.JHAL, left) &&
                engine.contains(Pratyahara.KHAR, right) &&
                substituteFor(left) != left
            ) return Target(termIndex, varnaIndex)
        }
        return null
    }

    private data class Target(val termIndex: Int, val varnaIndex: Int)
}
