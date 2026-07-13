package dev.sanskrit.ashtadhyayi.adhyaya6.pada4

import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.VarnaSubstitution
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 6.4.3: nāmi.
 * The final vowel of an aṅga is lengthened when followed by 'nām' (genitive plural).
 */
object NamiSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.3",
    text = "नामि",
    hindiExplanation = "नाम् परे होने पर अङ्ग के अन्त्य स्वर को दीर्घ होता है।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640003,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DHATU,
    nimittaScope = NimittaScope.EXTERNAL,
    dependencies = setOf("6.4.1", "7.1.54") // Depends on Anga jurisdiction and 'nuṭ' augment
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Check if affix is 'nām' (combination of nuṭ + ām)
        // In our engine, this appears as 'nuṭ-augment' + 'ām' or merged as 'नाम्'
        val isNam = affix.surface.startsWith("नाम्") || affix.upadesha == "आम्" && context.terms.any { it.upadesha == "नुट्" }
        
        if (!isNam) return false

        // 2. Stem must end in a short vowel
        val lastChar = stem.surface.lastOrNull() ?: return false
        val isConsonant = lastChar !in Varnamala.independentVowelsOrMarks
        if (isConsonant) return true
        return Varnamala.isVowel(lastChar) && !isAlreadyLong(lastChar)
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val lastChar = stem.surface.last()
        val (newSurface, sourceChar, replacement) = if (lastChar !in Varnamala.independentVowelsOrMarks) {
            Triple(stem.surface + "ा", 'अ', "ा")
        } else {
            val lengthened = when (lastChar) {
                'अ' -> "ा"
                'इ', 'ि' -> "ी"
                'उ', 'ु' -> "ू"
                'ऋ', 'ृ' -> "ॄ"
                else -> lastChar.toString()
            }
            Triple(stem.surface.dropLast(1) + lengthened, lastChar, lengthened)
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.ANGAKARYA)
                .addSubstitution(VarnaSubstitution(stem.id, sourceChar, replacement, sutra)),
            explanation = "6.4.3: Lengthened stem vowel before 'nām'."
        )
    }

    private fun isAlreadyLong(c: Char): Boolean = c in setOf('आ', 'ा', 'ई', 'ी', 'ऊ', 'ू', 'ॠ', 'ॄ', 'ए', 'े', 'ऐ', 'ै', 'ओ', 'ो', 'औ', 'ौ')
}
