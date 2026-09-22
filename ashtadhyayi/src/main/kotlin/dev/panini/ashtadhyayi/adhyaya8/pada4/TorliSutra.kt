package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.60: tor li.
 * The sounds of ta-varga (t, th, d, dh, n) are replaced by 'l' (or nasalized l̐ for 'n')
 * when immediately followed by 'l'.
 */
object TorliSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.60",
    text = "तोर्लि",
    hindiExplanation = "त-वर्ग (त्, थ्, द्, ध्, न्) के स्थान पर ल-कार आदेश होता है, यदि ल-कार परे हो (न् का अनुनासिक लँ्)।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840060,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.PADA_BOUNDARY,
    stage = SutraStage.SANDHI,
), DerivationSutra {
    private val tuVarga = Varnamala.expandUdit("तु")

    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = requireNotNull(findMatch(context))
        val targetTerm = context.terms[match.termIndex]
        val surface = targetTerm.surface
        val isNasal = match.targetChar == 'न'
        val replacement = if (isNasal) "ँल्" else "ल्"
        val viramaFollowsInSameTerm = surface.getOrNull(match.charIndex + 1) == '्'
        val end = match.charIndex + if (viramaFollowsInSameTerm) 2 else 1
        val newSurface = surface.replaceRange(match.charIndex, end, replacement)

        return DerivationChange(
            state = context.substituteTermSurface(targetTerm.id, newSurface, match.targetChar, replacement, sutra),
            explanation = "8.4.60: Assimilated ta-varga to $replacement before 'l'."
        )
    }

    private fun findMatch(context: DerivationState): Match? {
        val characters = context.terms.flatMapIndexed { termIndex, term ->
            term.surface.mapIndexed { charIndex, char -> OwnedChar(termIndex, charIndex, char) }
        }
        for (index in 0 until characters.size - 2) {
            val target = characters[index]
            val virama = characters[index + 1]
            val trigger = characters[index + 2]
            if (target.char in tuVarga && virama.char == '्' && trigger.char == 'ल') {
                return Match(target.termIndex, target.charIndex, target.char)
            }
        }
        return null
    }

    private data class OwnedChar(val termIndex: Int, val charIndex: Int, val char: Char)
    private data class Match(val termIndex: Int, val charIndex: Int, val targetChar: Char)
}
