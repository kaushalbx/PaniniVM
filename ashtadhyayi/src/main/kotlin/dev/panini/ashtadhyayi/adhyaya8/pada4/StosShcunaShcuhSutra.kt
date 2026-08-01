package dev.panini.ashtadhyayi.adhyaya8.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.VarnaSubstitution
import dev.panini.shiksha.Varnamala
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraStage
import dev.panini.sutra.SutraType

/**
 * 8.4.40: stoḥ ścunā ścuḥ.
 * The sounds of 'stu' (s and tu-varga) are replaced by 'ścu' (ś and cu-varga)
 * when they are in contact with 'ścu' sounds.
 */
object StosShcunaShcuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.40",
    text = "स्तोः श्चुना श्चुः",
    hindiExplanation = "सकार और त-वर्ग के स्थान पर शकार और च-वर्ग आदेश होते हैं, यदि शकार या च-वर्ग का योग हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840040,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    stage = SutraStage.SIBILANT_SANDHI,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context) != null

    override fun apply(context: DerivationState): DerivationChange {
        val match = findMatch(context)!!
        val targetTerm = context.terms[match.termIndex]
        val targetChar = targetTerm.surface[match.charIndex]
        val replacement = getReplacement(targetChar)
        val newSurface = targetTerm.surface.replaceRange(match.charIndex, match.charIndex + 1, replacement)

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.40: Palatalized $targetChar to $replacement in contact with ${match.triggerChar}."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, targetChar, replacement, sutra))) }
    }

    private fun findMatch(context: DerivationState): Match? {
        val characters = context.terms.flatMapIndexed { termIndex, term ->
            term.surface.mapIndexed { charIndex, char -> OwnedChar(termIndex, charIndex, char) }
        }
        for (i in 0 until characters.size - 2) {
            val curr = characters[i]
            val virama = characters[i + 1]
            val next = characters[i + 2]
            if (virama.char != '्') continue
            if (isStu(curr.char) && isShcu(next.char)) {
                return Match(curr.termIndex, curr.charIndex, next.char)
            }
            if (isShcu(curr.char) && isStu(next.char)) {
                return Match(next.termIndex, next.charIndex, curr.char)
            }
        }
        return null
    }

    private fun isStu(c: Char): Boolean = c in setOf('स', 'त', 'थ', 'द', 'ध', 'न')

    private fun isShcu(c: Char): Boolean = c in setOf('श', 'च', 'छ', 'ज', 'झ', 'ञ')

    private fun getReplacement(target: Char): String {
        if (target == 'स') return "श"
        val vargaInfo = Varnamala.getVargaInfo(target) ?: return target.toString()
        return Varnamala.getVargaMember("चु", vargaInfo.second)?.toString() ?: target.toString()
    }

    private data class OwnedChar(val termIndex: Int, val charIndex: Int, val char: Char)
    private data class Match(val termIndex: Int, val charIndex: Int, val triggerChar: Char)
}
