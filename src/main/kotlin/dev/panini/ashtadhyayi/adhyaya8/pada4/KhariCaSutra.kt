package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Varnamala
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
        val leftChar = leftTerm.surface[target.charIndex]

        val substitute = substituteFor(leftChar)

        val newSurface = leftTerm.surface.replaceRange(target.charIndex, target.charIndex + 1, substitute)

        return DerivationChange(
            state = context.replaceTerm(leftTerm.id, leftTerm.copy(surface = newSurface)),
            explanation = "8.4.55: Devoiced $leftChar to $substitute before voiceless sound."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra))) }
    }

    private fun substituteFor(source: Char): String = when (source) {
        'श', 'ष', 'स' -> source.toString()
        else -> SthaneAntaratamahSutra.selectBest(source, setOf("च", "ट", "त", "क", "प"))
    }

    private fun findTarget(context: DerivationState): Target? {
        val engine = Ashtadhyayi.pratyaharaEngine
        for (termIndex in 0 until context.terms.lastIndex) {
            val leftSurface = context.terms[termIndex].surface
            val charIndex = finalConsonantIndex(leftSurface) ?: continue
            val left = leftSurface[charIndex]
            val right = context.terms[termIndex + 1].surface.firstOrNull() ?: continue
            // A plain final consonant is followed by its inherent अ (फल + स्य),
            // so it is not a direct hal–khar contact.
            if (charIndex == leftSurface.lastIndex && Varnamala.isConsonant(left)) continue
            if (engine.contains(Pratyahara.JHAL, left) &&
                engine.contains(Pratyahara.KHAR, right) &&
                substituteFor(left) != left.toString()
            ) return Target(termIndex, charIndex)
        }
        return null
    }

    private fun finalConsonantIndex(surface: String): Int? = when {
        surface.isEmpty() -> null
        surface.endsWith("्") && surface.length >= 2 -> surface.lastIndex - 1
        else -> surface.lastIndex
    }

    private data class Target(val termIndex: Int, val charIndex: Int)
}
