package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.5 गुप्तिज्किद्भ्यः सन्.
 * Prescribes san pratyaya for gup, tij, and kit roots.
 */
object GupTijKitsadbhyahSanSutra : Sutra<String, String>(
    number = "3.1.5", text = "गुप्तिज्किद्भ्यः सन्",
    hindiExplanation = "गुप्, तिज् तथा किद् धातुओं से स्वार्थ में 'सन्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310005,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("गुप्", "तिज्", "किद्", "गुपूँ", "तिजँ", "किइँ")
    override fun apply(context: String): String = "सन्"
}
