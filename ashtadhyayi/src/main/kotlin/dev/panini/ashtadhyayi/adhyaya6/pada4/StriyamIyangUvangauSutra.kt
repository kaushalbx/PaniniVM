package dev.panini.ashtadhyayi.adhyaya6.pada4

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * 6.4.79: striyāḥ.
 * The 'strī' stem receives 'iyaṅ' (iy) substitution before vowel-initial case affixes (yielding striyam, striyā, etc.).
 */
object StriyamIyangUvangauSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.79",
    text = "स्त्रियाः",
    hindiExplanation = "स्त्री अङ्ग के स्थान पर इयङ् (इय्) आदेश होता है अच्-आदि सर्वनामस्थान/विभक्ति परे होने पर।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640079,
    role = SutraRole.Vidhi,
    action = SutraAction.ADESHA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        if (stem.surface == "स्त्रिय्") return false
        val isStriStem = stem.upadesha == "स्त्री" || stem.surface == "स्त्री"
        if (!isStriStem) return false

        val firstChar = affix.surface.firstOrNull() ?: return false
        val isVowelAffix = firstChar in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ', 'ा', 'ि', 'ी', 'ु', 'ू', 'े', 'ै', 'ो', 'ौ') ||
            affix.upadesha in setOf("अम्", "औ", "जस्", "शस्", "टा", "ङे", "ङसि", "ङस्", "ओस्", "आम्", "ङि")
        return isVowelAffix
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = "स्त्रिय्"))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "6.4.79: Applied 'iyaṅ' (iy) substitution to 'strī' stem before vowel affix (becoming striy-)."
        )
    }
}
