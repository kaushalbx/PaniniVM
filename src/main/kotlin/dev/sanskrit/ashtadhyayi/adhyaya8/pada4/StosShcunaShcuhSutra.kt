package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

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
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context.surface) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (targetIndex, triggerChar) = findMatch(context.surface)!!
        val targetChar = context.surface[targetIndex]
        val replacement = getReplacement(targetChar)

        var offset = 0
        val targetTerm = context.terms.find { term ->
            val start = offset
            offset += term.surface.length
            targetIndex in start until offset
        }!!

        val newSurface = targetTerm.surface.replaceFirst(targetChar.toString(), replacement)

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.40: Palatalized $targetChar to $replacement in contact with $triggerChar."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, targetChar, replacement, sutra))) }
    }

    private fun findMatch(surface: String): Pair<Int, Char>? {
        for (i in 0 until surface.length - 1) {
            val curr = surface[i]
            val next = surface[i+1]
            if (isStu(curr, surface, i) && isShcu(next, surface, i+1)) {
                return Pair(i, next)
            }
            if (isShcu(curr, surface, i) && isStu(next, surface, i+1)) {
                return Pair(i + 1, curr)
            }
        }
        return null
    }

    private fun isStu(c: Char, surface: String, index: Int): Boolean {
        val isConsonant = c in setOf('स', 'त', 'थ', 'द', 'ध', 'न')
        val hasVirama = index + 1 < surface.length && surface[index + 1] == '्'
        return isConsonant || (c == 'स' && hasVirama) // Simplified
    }

    private fun isShcu(c: Char, surface: String, index: Int): Boolean {
        return c in setOf('श', 'च', 'छ', 'ज', 'झ', 'ञ')
    }

    private fun getReplacement(target: Char): String {
        if (target == 'स') return "श"
        val vargaInfo = Varnamala.getVargaInfo(target) ?: return target.toString()
        return Varnamala.getVargaMember("चु", vargaInfo.second)?.toString() ?: target.toString()
    }
}
