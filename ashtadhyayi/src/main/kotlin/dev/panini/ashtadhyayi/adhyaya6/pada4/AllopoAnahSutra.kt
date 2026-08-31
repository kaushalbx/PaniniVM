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
 * 6.4.134: allopo 'naḥ.
 * The vowel 'a' of an 'an'-ending stem is elided before weak (bha) vowel affixes.
 */
object AllopoAnahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.4.134",
    text = "अल्लोपोऽनः",
    hindiExplanation = "अनन्त अङ्ग के अकार का लोप होता है अच्-आदि भ-प्रत्यय परे होने पर।",
    type = SutraType.NITYA,
    chapter = 6,
    pada = 4,
    optional = false,
    kramaValue = 640134,
    role = SutraRole.Vidhi,
    action = SutraAction.LOPA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1", "1.4.18"),
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if (context.terms.size < 2) return false
        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // śi is sarvanāmasthāna, not a weak bha affix. In particular, a
        // śas -> śi substitution retains that grammatical identity even
        // though the term id still records its original sup slot.
        if (affix.upadesha == "शि") return false

        // 1. Stem must end in 'न्' (an-ending stem)
        if (!stem.surface.endsWith("न्")) return false

        // Penultimate character check: must have implicit 'a' (consonant before 'न्' without matra)
        val surface = stem.surface
        if (surface.length < 3) return false
        val preConsonant = surface[surface.length - 3]
        if (preConsonant in setOf('ा', 'ि', 'ी', 'ु', 'ू', 'ृ', 'ॄ', 'े', 'ै', 'ो', 'ौ', '्')) return false

        // 6.4.137: na saṃyogād vamantāt - Elision of 'a' is blocked after conjuncts ending in v/m (like 'ātman', 'brahman')
        val isConjunctOrVamanta = surface.contains("त्मन्") || surface.contains("ह्मन्") || surface.endsWith("वन्")
        if (isConjunctOrVamanta) return false

        // 2. Affix must start with a vowel (ac-adi bha affix)
        val firstChar = affix.surface.firstOrNull() ?: return false
        val isVowelAffix = firstChar in setOf('अ', 'आ', 'इ', 'ई', 'उ', 'ऊ', 'ऋ', 'ॠ', 'ए', 'ऐ', 'ओ', 'औ', 'ा', 'ि', 'ी', 'ु', 'ू', 'े', 'ै', 'ो', 'ौ')
        val isBhaVowelAffix = isVowelAffix && affix.id in setOf(
            "sup-ta", "sup-nge", "sup-ngasi", "sup-ngas", "sup-os_6", "sup-os_7", "sup-am_6", "sup-ngi", "sup-sas"
        )
        return isBhaVowelAffix
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val surface = stem.surface
        val newSurface = surface.dropLast(2) + "्न्"

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newSurface))
                .copy(stage = DerivationStage.PADA_FORMED),
            explanation = "6.4.134: Elided the vowel 'a' of an-stem before weak vowel affix."
        )
    }
}
