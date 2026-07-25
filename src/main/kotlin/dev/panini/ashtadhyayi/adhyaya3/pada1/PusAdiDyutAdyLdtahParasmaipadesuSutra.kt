package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.55 पुषादिद्युताद्य्लृदिल्भ्यो परस्मैपदेषु अङ्.
 * Prescribes aṅ aorist vikaraṇa for puṣādi, dyutādi roots in Parasmaipada Luṅ.
 */
object PusAdiDyutAdyLdtahParasmaipadesuSutra : Sutra<String, String>(
    number = "3.1.55", text = "पुषादिद्युताद्य्लृदिल्भ्यो परस्मैपदेषु अङ्",
    hindiExplanation = "पुषादि, द्युतादि तथा ऌकारित् धातुओं से परस्मैपद लुङ् में 'अङ्' प्रत्यय होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310055,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean =
        context in setOf("पुष्", "द्युत्", "गुध्", "श्वि", "शुच्")
    override fun apply(context: String): String = "अङ्"
}
