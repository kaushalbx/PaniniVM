package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 28: आशीर्धूः पूर्गीर्द्वारः । */
object AsisDhurPurGirDvarSutra : LinganushasanaSutra(
    number = "28",
    text = "आशीर्धूः पूर्गीर्द्वारः ।",
    hindiExplanation = "आशिस्, धुर्, पुर्, गीर् और द्वार् स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    private val members = setOf("आशिस्", "धुर्", "पुर्", "गीर्", "द्वार्")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

