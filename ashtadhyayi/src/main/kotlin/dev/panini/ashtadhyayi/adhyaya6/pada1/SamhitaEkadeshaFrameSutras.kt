package dev.panini.ashtadhyayi.adhyaya6.pada1

import dev.panini.derivation.DerivationChange
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationSutra
import dev.panini.sutra.*

/*
 * Framework sūtras of the 6.1 sandhi section. They change no state themselves; the engine realises them:
 *  - 6.1.72 and 6.1.84 are headings (adhikāra). Every rule of the sandhi stage already presumes संहिता,
 *    and each ekādeśa rule replaces both sounds by one. They carry the Anuvṛtti role, not Adhikara: an
 *    Adhikara role would gate every rule in the range on an activation that saṃhitā never needs.
 *  - 6.1.85 is applied where an ekādeśa merges two terms: the merged sound is treated as the end of the
 *    first and the start of the second (`droppedTerms`, `mergedIntoTermId`, 6.1.69 on हे फल).
 *  - 6.1.86 is not yet enforced as a general asiddhatva; ṣatva and तुक् rules read the current state.
 * They are registered so the section is complete and each is cited by number; `matches` is false.
 */

/** 6.1.72 संहितायाम् — the following rules (to 6.1.157) apply in close contact. */
object SamhitayamAdhikaraSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.72", text = "संहितायाम्",
    hindiExplanation = "यहाँ से आगे के कार्य संहिता (अत्यन्त सन्निकर्ष) में होते हैं।",
    type = SutraType.ADHIKARA, chapter = 6, pada = 1, optional = false, kramaValue = 610072,
    role = SutraRole.Anuvrtti, action = SutraAction.ADHIKARA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState) = DerivationChange(context, "6.1.72 heading.")
}

/** 6.1.84 एकः पूर्वपरयोः — in the following rules one substitute replaces both the earlier and the later sound. */
object EkahPurvaParayohAdhikaraSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.84", text = "एकः पूर्वपरयोः",
    hindiExplanation = "यहाँ से आगे पूर्व और पर दोनों के स्थान पर एक आदेश होता है।",
    type = SutraType.ADHIKARA, chapter = 6, pada = 1, optional = false, kramaValue = 610084,
    role = SutraRole.Anuvrtti, action = SutraAction.ADHIKARA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState) = DerivationChange(context, "6.1.84 heading.")
}

/** 6.1.85 अन्तादिवच्च — the single substitute counts as the end of the first and the start of the second. */
object AntadivaccaSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.85", text = "अन्तादिवच्च",
    hindiExplanation = "एकादेश पूर्व के अन्त और पर के आदि के समान माना जाता है।",
    type = SutraType.ATIDESHA, chapter = 6, pada = 1, optional = false, kramaValue = 610085,
    role = SutraRole.Atidesha, action = SutraAction.ATIDESHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState) = DerivationChange(context, "6.1.85 atideśa.")
}

/** 6.1.86 षत्वतुकोरसिद्धः — for ṣatva and तुक्, the ekādeśa is treated as not having taken place. */
object ShatvaTukorAsiddhahSutra : Sutra<DerivationState, DerivationChange>(
    number = "6.1.86", text = "षत्वतुकोरसिद्धः",
    hindiExplanation = "षत्व और तुक् के विषय में एकादेश असिद्ध होता है।",
    type = SutraType.PARIBHASHA, chapter = 6, pada = 1, optional = false, kramaValue = 610086,
    role = SutraRole.Paribhasha(), action = SutraAction.PARIBHASHA, scope = SutraScope.DERIVATION,
), DerivationSutra {
    override fun matches(context: DerivationState) = false
    override fun apply(context: DerivationState) = DerivationChange(context, "6.1.86 paribhāṣā.")
}
