package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.1.73: छे च. Inserts तुक् after a short vowel before छ्.
 * The executable environment currently covers छ् introduced by 7.3.77.
 */
object CheCaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.73",
    text = "छे च",
    hindiExplanation = "ह्रस्व स्वर के बाद छकार परे होने पर तुक् आगम होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 1,
    optional = false,
    kramaValue = 610073,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (termIndex, charIndex) = findMatch(context)!!
        val term = context.terms[termIndex]
        return DerivationChange(
            state = context.replaceTerm(
                term.id,
                term.copy(surface = term.surface.replaceRange(charIndex, charIndex, "त्")),
            ),
            explanation = "6.1.73 inserts तुक् after a short vowel before छ्.",
        )
    }

    private fun findMatch(context: DerivationState): Pair<Int, Int>? {
        if (context.substitutions.none { it.sutra == "7.3.77" }) return null
        context.terms.forEachIndexed { termIndex, term ->
            term.surface.forEachIndexed { charIndex, char ->
                if (char == 'छ' && charIndex > 0 && isShortVowelBefore(term.surface, charIndex)) {
                    return termIndex to charIndex
                }
            }
        }
        return null
    }

    private fun isShortVowelBefore(surface: String, index: Int): Boolean {
        val previous = surface[index - 1]
        return previous in setOf('अ', 'इ', 'उ', 'ऋ', 'ऌ', 'ि', 'ु', 'ृ', 'ॢ') ||
            (Varnamala.isConsonant(previous) && surface.getOrNull(index - 2) != '्')
    }
}
