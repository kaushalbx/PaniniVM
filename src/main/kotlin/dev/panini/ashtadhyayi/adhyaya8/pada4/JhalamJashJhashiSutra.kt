package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.pratyahara.Pratyahara
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.4.53: jhalāṃ jaś jhaśi.
 * Substitutes jaś (voiced unaspirated stops) for jhal (stops + fricatives)
 * when followed by jhaś (voiced stops).
 */
object JhalamJashJhashiSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.53",
    text = "झलां जश् झशि",
    hindiExplanation = "झल् वर्णों के स्थान पर जश् आदेश होता है यदि बाद में झश् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840053,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        // Matches adjacent consonants within a word or at word boundaries.
        // For simplicity, we check across term boundaries.
        if (context.terms.size < 2) return false

        val leftSurface = context.terms[context.terms.size - 2].surface
        val leftIndex = finalConsonantIndex(leftSurface) ?: return false
        val left = leftSurface[leftIndex]
        val right = context.terms.last().surface.firstOrNull() ?: return false

        val engine = Ashtadhyayi.pratyaharaEngine
        return engine.contains(Pratyahara.JHAL, left) &&
            engine.contains(Pratyahara.JHASH, right) &&
            substituteFor(left) != left.toString()
    }

    override fun apply(context: DerivationState): DerivationChange {
        val terms = context.terms
        val leftTerm = terms[terms.size - 2]
        val rightTerm = terms.last()

        val leftIndex = requireNotNull(finalConsonantIndex(leftTerm.surface))
        val leftChar = leftTerm.surface[leftIndex]

        val substitute = substituteFor(leftChar)

        val newSurface = leftTerm.surface.replaceRange(leftIndex, leftIndex + 1, substitute)

        return DerivationChange(
            state = context.replaceTerm(leftTerm.id, leftTerm.copy(surface = newSurface))
                .addSubstitution(VarnaSubstitution(leftTerm.id, leftChar, substitute, sutra)),
            explanation = "8.4.53: Substituted voiced $substitute for $leftChar before ${rightTerm.surface.first()}."
        )
    }

    private fun substituteFor(source: Char): String =
        SthaneAntaratamahSutra.selectBest(source, setOf("ज", "ब", "ग", "ड", "द"))

    private fun finalConsonantIndex(surface: String): Int? = when {
        surface.isEmpty() -> null
        surface.endsWith('्') && surface.length >= 2 -> surface.lastIndex - 1
        else -> surface.lastIndex
    }
}
