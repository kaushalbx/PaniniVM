package dev.panini.ashtadhyayi.adhyaya1.pada1

import dev.panini.sutra.InterpretivePrinciple
import dev.panini.sutra.InterpretivePrincipleArtha
import dev.panini.sutra.ParibhashaVidhayakaSutra
import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 1.1.69 अणुदित्सवर्णस्य चाप्रत्ययः.
 * Meta-rule: aṇ vowels and u-it consonants (ku, chu, ṭu, tu, pu) represent all their homogeneous (savarṇa) variants.
 */
object AnuditSavarnasyaCapratyayahSutra : Sutra<String, Boolean>(
    number = "1.1.69", text = "अणुदित्सवर्णस्य चाप्रत्ययः",
    hindiExplanation = "प्रत्यय से भिन्न अण् (अ, इ, उ, ऋ, ऌ, ए, ओ, ऐ, औ, ह, य, व, र, ल) तथा उदित् (कु, चु, टु, तु, पु) अपने सवर्ण वर्णों के ग्राहक होते हैं।",
    type = SutraType.PARIBHASHA, chapter = 1, pada = 1, optional = false, kramaValue = 110069,
    role = SutraRole.Paribhasha(), action = SutraAction.PARIBHASHA, scope = SutraScope.VARNA,
    inputs = setOf(SutraInput.VARNA, SutraInput.PRATYAHARA),
    adhikara = emptySet(),
), ParibhashaVidhayakaSutra {
    override val artha = InterpretivePrincipleArtha(
        InterpretivePrinciple.SAVARNA_INCLUSION_EXCEPT_AFFIX,
    )

    override fun matches(context: String): Boolean =
        context.endsWith("ु") || context in setOf("अ", "इ", "उ", "ऋ", "ऌ", "ए", "ओ", "ऐ", "औ")

    override fun apply(context: String): Boolean = true
}
