package dev.panini.vyakaranam.analysis

import dev.panini.ashtadhyayi.adhyaya1.pada1.AdyantauTakitauSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.IkoGunaVrddhiSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.MidacoAntyatParahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasmadItyUttarasyaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TasminnitiNirdishtePurvasyaSutra
import dev.panini.sutra.Sutra

enum class ParibhashaScope {
    VOWEL_SUBSTITUTION,
    AUGMENT_PLACEMENT,
    LOCATIVE_TRIGGER,
    ABLATIVE_TRIGGER,
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
        ParibhashaRule(sutra = TasminnitiNirdishtePurvasyaSutra, targetScope = ParibhashaScope.LOCATIVE_TRIGGER),
        ParibhashaRule(sutra = TasmadItyUttarasyaSutra, targetScope = ParibhashaScope.ABLATIVE_TRIGGER),
    )

    fun findByNumber(number: String): ParibhashaRule? =
        rules.firstOrNull { it.sutraNumber == number }
}
