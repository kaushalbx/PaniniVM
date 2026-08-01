package dev.panini.ashtadhyayi.adhyaya8.pada3

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.TermKind
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Ayogavaha
import dev.panini.shiksha.Samjna
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 8.3.23: mo'nusvāraḥ.
 * Word-final 'm' becomes Anusvāra when followed by a consonant (hal).
 */
object MonusvarahSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.23",
    text = "मोऽनुस्वारः",
    hindiExplanation = "पदान्त मकार के स्थान पर अनुस्वार होता है यदि बाद में कोई व्यञ्जन हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830023,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.any { it.kind == TermKind.PRATYAYA }) return false
        if (context.terms.size < 2) return false
        val left = context.terms[context.terms.size - 2]
        val right = context.terms.last()

        // 1. Left must be a Pada and end in 'm'
        val isPada = context.samjnas.any { it.targetId == left.id && it.samjna == Samjna.PADA }
        if (!isPada || !left.surface.endsWith("म्")) return false

        // 2. Right must start with a consonant (hal)
        val firstChar = right.surface.firstOrNull() ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.HAL, firstChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val left = context.terms[context.terms.size - 2]
        val newSurface = left.surface.removeSuffix("म्") + Ayogavaha.ANUSVARA.devanagari

        return DerivationChange(
            state = context.replaceTerm(left.id, left.copy(surface = newSurface)),
            explanation = "8.3.23: Final 'm' became Anusvāra before consonant."
        )
    }
}

/**
 * 8.3.24: naścāpadāntasya jhali.
 * Non-word-final 'n' and 'm' become Anusvāra when followed by a jhal sound.
 */
object NashcapadantasyaSutra : Sutra<DerivationState, DerivationChange>(
    number = "8.3.24",
    text = "नश्चापदान्तस्य झलि",
    hindiExplanation = "अपदान्त 'न' और 'म' के स्थान पर अनुस्वार होता है यदि बाद में झल् वर्ण हो।",
    type = SutraType.NITYA,
    chapter = 8,
    pada = 3,
    optional = false,
    kramaValue = 830024,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.VARNA,
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.any { it.kind == TermKind.PRATYAYA }) return false
        val surface = context.surface
        val nIndex = surface.indexOfAny(setOf('न', 'म'))
        if (nIndex == -1 || nIndex == surface.length - 1) return false
        // A bare consonant carries its inherent vowel; only a halanta nasal is
        // immediately followed by the next consonant for this sandhi rule.
        if (surface.getOrNull(nIndex + 1) != '्') return false

        val nextChar = surface.getOrNull(nIndex + 2) ?: return false
        return Ashtadhyayi.pratyaharaEngine.contains(Pratyahara.JHAL, nextChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val surface = context.surface
        val index = surface.indexOfAny(setOf('न', 'म'))

        var offset = 0
        val targetTerm = context.terms.find {
            val start = offset
            offset += it.surface.length
            index in start until offset
        } ?: return DerivationChange(context, "8.3.24: Target not found.")

        val charAt = surface[index]
        val termStart = offset - targetTerm.surface.length
        val localIndex = index - termStart
        val hasViramaInTerm = localIndex + 1 < targetTerm.surface.length && targetTerm.surface[localIndex + 1] == '्'
        val newSurface = if (hasViramaInTerm) {
            targetTerm.surface.substring(0, localIndex) + "ं" + targetTerm.surface.substring(localIndex + 2)
        } else {
            targetTerm.surface.substring(0, localIndex) + "ं" + targetTerm.surface.substring(localIndex + 1)
        }

        return DerivationChange(
            state = context.replaceTerm(targetTerm.id, targetTerm.copy(surface = newSurface)),
            explanation = "8.3.24: Internal '$charAt' became Anusvāra before jhal."
        )
    }

    private fun String.indexOfAny(chars: Set<Char>): Int {
        for (i in indices) {
            if (this[i] in chars) return i
        }
        return -1
    }
}
