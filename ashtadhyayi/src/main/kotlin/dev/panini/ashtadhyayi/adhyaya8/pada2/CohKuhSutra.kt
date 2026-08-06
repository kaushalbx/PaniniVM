package dev.panini.ashtadhyayi.adhyaya8.pada2

import dev.panini.ashtadhyayi.Ashtadhyayi
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
 * 8.2.30: coḥ kuḥ.
 * Substitutes ku (ka-varga) for cu (ca-varga)
 * at the end of a pada or before a jhal sound.
 */
object CohKuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.2.30",
    text = "चोः कुः",
    hindiExplanation = "पदान्त में या झल् वर्ण परे होने पर च-वर्ग के स्थान पर क-वर्ग आदेश होता है।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 2,
    optional = false,
    kramaValue = 820030,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val targetChar = targetTerm.surface[match.charIndex]
        val vargaInfo = Varnamala.getVargaInfo(targetChar)!!
        val replacement = Varnamala.getVargaMember("कु", vargaInfo.second)!!.toString()
        val newSurface = targetTerm.surface.replaceRange(match.charIndex, match.charIndex + 1, replacement)

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.2.30: Substituted ka-varga '$replacement' for ca-varga '$targetChar'."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, targetChar, replacement, sutra))) }
    }

    private fun findMatch(context: DerivationState): Match? {
        val abhyasaIds = context.samjnas
            .filter { it.samjna == dev.panini.shiksha.Samjna.ABHYASA }
            .mapTo(mutableSetOf()) { it.targetId }
        val characters = context.terms.flatMapIndexed { termIndex, term ->
            term.surface.mapIndexed { charIndex, char -> OwnedChar(termIndex, charIndex, term.id, char) }
        }
        val finalConsonant = characters.getOrNull(characters.lastIndex - 1)
        if (characters.lastOrNull()?.char == '्' && finalConsonant != null &&
            finalConsonant.char in CU_CHARS && finalConsonant.termId !in abhyasaIds
        ) {
            return Match(finalConsonant.termIndex, finalConsonant.charIndex)
        }
        for (i in 0 until characters.size - 2) {
            val target = characters[i]
            if (target.char !in CU_CHARS || target.termId in abhyasaIds) continue
            if (characters[i + 1].char != '्') continue
            if (target.termId != characters[i + 2].termId && Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, characters[i + 2].char)) {
                return Match(target.termIndex, target.charIndex)
            }
        }
        return null
    }

    private data class OwnedChar(val termIndex: Int, val charIndex: Int, val termId: String, val char: Char)
    private data class Match(val termIndex: Int, val charIndex: Int)

    private val CU_CHARS = setOf('च', 'छ', 'ज', 'झ', 'ञ')
}
