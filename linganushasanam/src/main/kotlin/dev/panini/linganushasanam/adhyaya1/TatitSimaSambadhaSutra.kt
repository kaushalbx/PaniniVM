package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 31: तटिसीमासम्बध्याः । */
object TatitSimaSambadhaSutra : LinganushasanaSutra(
    number = "31",
    text = "तटिसीमासम्बध्याः ।",
    hindiExplanation = "तटित्, सीमा और सम्बाधा स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    private val members = setOf("तटित्", "सीमा", "सम्बाधा")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

