package dev.panini.ashtadhyayi.adhyaya3.pada1

import dev.panini.sutra.Sutra
import dev.panini.sutra.SutraAction
import dev.panini.sutra.SutraInput
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraScope
import dev.panini.sutra.SutraType

/**
 * Sūtra 3.1.28 गुप्धूपविच्छिपणिपणिभ्य आयः.
 * Prescribes āya pratyaya for gup, dhūp, vicch, paṇ, pan roots.
 */
object GupDhoopVichchhiPaniPanibhyOyaSutra : Sutra<String, String>(
    number = "3.1.28", text = "गुप्धूपविच्छिपणिपणिभ्य आयः",
    hindiExplanation = "गुप्, धूप, विच्छ्, पण् तथा पन् धातुओं से 'आय' प्रत्यय स्वार्थ में होता है।",
    type = SutraType.NITYA, chapter = 3, pada = 1, optional = false, kramaValue = 310028,
    role = SutraRole.Vidhi, action = SutraAction.PRATYAYA_SELECTION, scope = SutraScope.DHATU,
    inputs = setOf(SutraInput.DHATU),
    adhikara = emptySet(),
) {
    override fun matches(context: String): Boolean = context in setOf("गुप्", "धूप", "विच्छ्", "पण्", "पन्")
    override fun apply(context: String): String = "आय"
}
