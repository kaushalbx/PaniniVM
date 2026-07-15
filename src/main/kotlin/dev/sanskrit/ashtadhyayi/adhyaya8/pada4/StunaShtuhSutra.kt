package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
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
 * 8.4.41: ṣṭunā ṣṭuḥ.
 * The sounds of 'stu' (s and tu-varga) are replaced by 'ṣṭu' (ṣ and ṭu-varga)
 * when they are in contact with 'ṣṭu' sounds.
 */
object StunaShtuhSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.4.41",
    text = "ष्टुना ष्टुः",
    hindiExplanation = "सकार और त-वर्ग के स्थान पर षकार और ट-वर्ग आदेश होते हैं, यदि षकार या ट-वर्ग का योग हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 4,
    optional = false,
    kramaValue = 840041,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
    dependencies = setOf("8.4.40")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean = findMatch(context.surface) != null

    override fun apply(context: DerivationState): DerivationChange {
        val (targetIndex, triggerChar) = findMatch(context.surface)!!
        val targetChar = context.surface[targetIndex]
        val replacement = getReplacement(targetChar)

        // Find the term containing targetIndex
        var offset = 0
        val targetTerm = context.terms.find { term ->
            val start = offset
            offset += term.surface.length
            targetIndex in start until offset
        }!!

        val newSurface = targetTerm.surface.replaceFirst(targetChar.toString(), replacement)

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.41: Retroflexed $targetChar to $replacement in contact with $triggerChar."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, targetChar, replacement, sutra))) }
    }

    private fun findMatch(surface: String): Pair<Int, Char>? {
        for (i in 0 until surface.length - 1) {
            val curr = surface[i]
            val next = surface[i+1]
            if (isStu(curr) && isShtu(next)) {
                return Pair(i, next)
            }
            if (isShtu(curr) && isStu(next)) {
                return Pair(i + 1, curr)
            }
        }
        return null
    }

    private fun isStu(c: Char): Boolean = c in setOf('स', 'त', 'थ', 'द', 'ध', 'न') || c.toString().startsWithAny(setOf("स", "त", "थ", "द", "ध", "न"))
    private fun isShtu(c: Char): Boolean = c in setOf('ष', 'ट', 'ठ', 'ड', 'ढ', 'ण') || c.toString().startsWithAny(setOf("ष", "ट", "ठ", "ड", "ढ", "ण"))

    private fun String.startsWithAny(set: Set<String>) = set.any { this.startsWith(it) }

    private fun getReplacement(target: Char): String {
        if (target == 'स') return "ष"
        val vargaInfo = Varnamala.getVargaInfo(target) ?: return target.toString()
        // Map tu-varga to ṭu-varga (same index)
        return Varnamala.getVargaMember("टु", vargaInfo.second)?.toString() ?: target.toString()
    }
}
