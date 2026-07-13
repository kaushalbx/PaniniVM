package dev.sanskrit.ashtadhyayi.adhyaya7.pada1

import dev.sanskrit.ashtadhyayi.Ashtadhyayi
import dev.sanskrit.derivation.DerivationChange
import dev.sanskrit.derivation.DerivationStage
import dev.sanskrit.derivation.DerivationState
import dev.sanskrit.derivation.DerivationSutra
import dev.sanskrit.derivation.DerivationTerm
import dev.sanskrit.derivation.HasMorphosyntax
import dev.sanskrit.derivation.TermKind
import dev.sanskrit.pratyahara.Pratyahara
import dev.sanskrit.shiksha.Varnamala
import dev.sanskrit.shiksha.Linga
import dev.sanskrit.sutra.NimittaScope
import dev.sanskrit.sutra.Sutra
import dev.sanskrit.sutra.SutraAction
import dev.sanskrit.sutra.SutraRole
import dev.sanskrit.sutra.SutraScope
import dev.sanskrit.sutra.SutraType

/**
 * 7.1.72: napuṃsakasya jhalācaḥ.
 * Adds the augment 'num' (n) to a neuter stem ending in a vowel (ac) or a Jhal consonant
 * when followed by a Sarvanāmasthāna affix.
 */
object NapumsakasyaJhalacahSutra : Sutra<DerivationState, DerivationChange>(
    number = "7.1.72",
    text = "नपुंसकस्य झलचः",
    hindiExplanation = "झलन्त या अजन्त नपुंसक अङ्ग को सर्वनामस्थान परे होने पर 'नुम्' आगम होता है।",
    type = SutraType.NITYA,
    chapter = 7,
    pada = 1,
    optional = false,
    kramaValue = 710072,
    role = SutraRole.Vidhi,
    action = SutraAction.AGAMA,
    scope = SutraScope.DERIVATION,
    nimittaScope = NimittaScope.BOTH,
    dependencies = setOf("6.4.1", "1.1.47")
), DerivationSutra {
    override fun matches(context: DerivationState): Boolean {
        if ("6.4.1" !in context.activeAdhikaras) return false
        if (!HasMorphosyntax(linga = Linga.NAPUMSAKA).matches(context)) return false
        if (context.terms.size < 2) return false

        val stem = context.terms[context.terms.size - 2]
        val affix = context.terms.last()

        // 1. Affix must be Sarvanāmasthāna (for neuter, this is 'śi')
        val isSarvanamasthana = affix.upadesha == "शि"
        if (!isSarvanamasthana) return false

        // 2. Stem must end in Ac or Jhal
        val lastChar = stem.surface.lastOrNull() ?: return false
        val engine = Ashtadhyayi.pratyaharaEngine
        val endsInAcOrJhal = engine.contains(Pratyahara.AC, lastChar) || engine.contains(Pratyahara.JHAL, lastChar)

        return endsInAcOrJhal && context.terms.none { it.upadesha == "नुम्" }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        val surface = stem.surface
        
        // 1.1.47: Mit (marked with M) goes after the last vowel of the term.
        var lastVowelIndex = -1
        for (i in surface.indices.reversed()) {
            if (Varnamala.isVowel(surface[i])) {
                lastVowelIndex = i
                break
            }
        }

        // If no vowel (unlikely for jhal/ac), we'd fallback. 
        // Here we insert 'n' (the essence of num).
        val numSurface = "न्"
        val newStemSurface = if (lastVowelIndex != -1) {
            surface.substring(0, lastVowelIndex + 1) + numSurface + surface.substring(lastVowelIndex + 1)
        } else {
            surface + numSurface
        }

        return DerivationChange(
            state = context.replaceTerm(stem.id, stem.copy(surface = newStemSurface))
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.72: Added 'num' augment (न्) after the last vowel of the neuter stem."
        )
    }
}
