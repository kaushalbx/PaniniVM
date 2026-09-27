package dev.panini.ashtadhyayi.adhyaya7.pada1

import dev.panini.ashtadhyayi.Ashtadhyayi
import dev.panini.core.Linga
import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationStage
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.derivation.HasMorphosyntax
import dev.panini.pratyahara.Pratyahara
import dev.panini.shiksha.Svara
import dev.panini.shiksha.Vyanjana
import dev.panini.sutra.NimittaScope
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

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
        val final = stem.varnas.lastOrNull() ?: return false
        val engine = Ashtadhyayi.pratyaharaEngine
        val endsInAcOrJhal = final is Svara ||
            final is Vyanjana && engine.contains(Pratyahara.JHAL, final.devanagari.single())

        return endsInAcOrJhal && context.substitutions.none { it.sutra == sutra }
    }

    override fun apply(context: DerivationState): DerivationChange {
        val stem = context.terms[context.terms.size - 2]
        // 1.1.47: m-it is placed after the final vowel, in phonological-token space.
        val lastVowelIndex = stem.varnas.indexOfLast { it is Svara }
        val insertionBoundary = if (lastVowelIndex >= 0) lastVowelIndex + 1 else stem.varnas.size

        return DerivationChange(
            state = context.insertTermVarnas(stem.id, insertionBoundary, listOf(Vyanjana.NA), sutra)
                .copy(stage = DerivationStage.ANGAKARYA),
            explanation = "7.1.72: Added 'num' augment (न्) after the last vowel of the neuter stem."
        )
    }
}
