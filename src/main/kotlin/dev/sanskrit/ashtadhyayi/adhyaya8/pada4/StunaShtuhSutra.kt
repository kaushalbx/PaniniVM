package dev.sanskrit.ashtadhyayi.adhyaya8.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
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
    override fun matches(context: DerivationState): Boolean {
        val shnaIndex = context.terms.indexOfFirst { it.id == "shna" }
        if (shnaIndex >= 0 && context.terms[shnaIndex].surface.endsWith("ण्") &&
            context.terms.getOrNull(shnaIndex + 1)?.surface?.startsWith("न") == true) return false
        // In the LET सिप् formation the following त् belongs to अट् + त्;
        // the intervening अ prevents actual ṣṭutva (तारिषत्, not *तारिषट्).
        if (context.terms.any { it.id == "sip-aorist" && 'ष' in it.surface }) return false
        val lungSicIndex = context.terms.indexOfFirst { it.upadesha == "सिच्" && it.surface.endsWith("ष्") }
        if (lungSicIndex >= 0 && context.terms.getOrNull(lungSicIndex + 1)?.surface?.firstOrNull() in
            setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ऌ', 'ए', 'ऐ', 'ओ', 'औ')) return false
        if (crossTermTarget(context) != null) return true
        val match = findMatch(context.surface) ?: return false
        val target = context.surface[match.first]
        return getReplacement(target) != target.toString()
    }

    override fun apply(context: DerivationState): DerivationChange {
        crossTermTarget(context)?.let { (term, replacement) ->
            val target = term.surface.first()
            val newSurface = replacement + term.surface.drop(1)
            return DerivationChange(
                state = context.replaceTerm(term.id, term.copy(surface = newSurface)),
                explanation = "8.4.41: Retroflexed $target to $replacement after a preceding ष्.",
            ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(term.id, target, replacement, sutra))) }
        }
        val (targetIndex, triggerChar) = findMatch(context.surface)!!
        val targetChar = context.surface[targetIndex]
        val replacement = getReplacement(targetChar)

        // Find the term containing targetIndex
        var offset = 0
        var targetTerm = context.terms.first()
        var localIndex = 0
        for (term in context.terms) {
            val start = offset
            offset += term.surface.length
            if (targetIndex in start until offset) {
                targetTerm = term
                localIndex = targetIndex - start
                break
            }
        }

        val newSurface = targetTerm.surface.substring(0, localIndex) + replacement +
            targetTerm.surface.substring(localIndex + 1)

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.4.41: Retroflexed $targetChar to $replacement in contact with $triggerChar."
        ).let { it.copy(state = it.state.addSubstitution(VarnaSubstitution(targetTerm.id, targetChar, replacement, sutra))) }
    }

    private fun findMatch(surface: String): Pair<Int, Char>? {
        for (i in 0 until surface.length - 2) {
            val curr = surface[i]
            if (surface[i + 1] != '्') continue
            val nextIndex = i + 2
            val next = surface[nextIndex]
            if (isStu(curr) && isShtu(next)) {
                return Pair(i, next)
            }
            if (isShtu(curr) && isStu(next)) {
                return Pair(nextIndex, curr)
            }
        }
        return null
    }

    private fun crossTermTarget(context: DerivationState): Pair<dev.sanskrit.derivation.DerivationTerm, String>? {
        for (i in 0 until context.terms.lastIndex) {
            if (!context.terms[i].surface.endsWith("ष्")) continue
            val right = context.terms[i + 1]
            val replacement = when (right.surface.firstOrNull()) {
                'त' -> "ट"
                'थ' -> "ठ"
                'द' -> "ड"
                'ध' -> "ढ"
                'न' -> "ण"
                else -> null
            } ?: continue
            return right to replacement
        }
        return null
    }

    private fun isStu(c: Char): Boolean = c in setOf('स', 'त', 'थ', 'द', 'ध', 'न') || c.toString().startsWithAny(setOf("स", "त", "थ", "द", "ध", "न"))
    private fun isShtu(c: Char): Boolean = c in setOf('ष', 'ट', 'ठ', 'ड', 'ढ', 'ण') || c.toString().startsWithAny(setOf("ष", "ट", "ठ", "ड", "ढ", "ण"))

    private fun String.startsWithAny(set: Set<String>) = set.any { this.startsWith(it) }

    private fun getReplacement(target: Char): String {
        if (target == 'स') return "ष"
        return when (target) {
            'त' -> "ट"
            'थ' -> "ठ"
            'द' -> "ड"
            'ध' -> "ढ"
            'न' -> "ण"
            else -> target.toString()
        }
    }
}
