package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdehParasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AlAntyasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AnekalSitSarvasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.IkoGunaVrddhiSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.MidacoAntyatParahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.NgitScaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SasthiSthaneyogaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthaneAntaratamahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SthanivadAdesoAnalvidhauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasmadItyUttarasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasminnitiNirdishtePurvasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.UranRaparahSutra
import dev.panini.sutra.Sutra

enum class ParibhashaScope {
    VOWEL_SUBSTITUTION,
    AUGMENT_PLACEMENT,
    LOCATIVE_TRIGGER,
    ABLATIVE_TRIGGER,
    GENITIVE_RELATION,
    PHONETIC_SIMILARITY,
    PHONEME_TARGET_LAST,
    PHONEME_TARGET_FIRST,
    RAPARA_AUGMENTATION,
    FULL_TERM_SUBSTITUTION,
    ORIGINAL_PROPERTY_INHERITANCE,
    GENERAL,
}

data class ParibhashaRule(
    val sutra: Sutra<*, *>,
    val targetScope: ParibhashaScope = ParibhashaScope.GENERAL,
) {
    val sutraNumber: String get() = sutra.number
    val sutraText: String get() = sutra.text
}

object ParibhashaRegistry {
    val rules: List<ParibhashaRule> = listOf(
        ParibhashaRule(sutra = IkoGunaVrddhiSutra, targetScope = ParibhashaScope.VOWEL_SUBSTITUTION),
        ParibhashaRule(sutra = AdyantauTakitauSutra, targetScope = ParibhashaScope.AUGMENT_PLACEMENT),
        ParibhashaRule(sutra = MidacoAntyatParahSutra, targetScope = ParibhashaScope.AUGMENT_PLACEMENT),
        ParibhashaRule(sutra = SasthiSthaneyogaSutra, targetScope = ParibhashaScope.GENITIVE_RELATION),
        ParibhashaRule(sutra = SthaneAntaratamahSutra, targetScope = ParibhashaScope.PHONETIC_SIMILARITY),
        ParibhashaRule(sutra = UranRaparahSutra, targetScope = ParibhashaScope.RAPARA_AUGMENTATION),
        ParibhashaRule(sutra = AlAntyasyaSutra, targetScope = ParibhashaScope.PHONEME_TARGET_LAST),
        ParibhashaRule(sutra = NgitScaSutra, targetScope = ParibhashaScope.PHONEME_TARGET_LAST),
        ParibhashaRule(sutra = AdehParasyaSutra, targetScope = ParibhashaScope.PHONEME_TARGET_FIRST),
        ParibhashaRule(sutra = AnekalSitSarvasyaSutra, targetScope = ParibhashaScope.FULL_TERM_SUBSTITUTION),
        ParibhashaRule(sutra = SthanivadAdesoAnalvidhauSutra, targetScope = ParibhashaScope.ORIGINAL_PROPERTY_INHERITANCE),
        ParibhashaRule(sutra = TasminnitiNirdishtePurvasyaSutra, targetScope = ParibhashaScope.LOCATIVE_TRIGGER),
        ParibhashaRule(sutra = TasmadItyUttarasyaSutra, targetScope = ParibhashaScope.ABLATIVE_TRIGGER),
    )

    fun findByNumber(number: String): ParibhashaRule? =
        rules.firstOrNull { it.sutraNumber == number }
}
