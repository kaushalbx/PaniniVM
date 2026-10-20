package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaRuleKind
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra

/** Liṅgānuśāsanam 60: प्राग्रश्मेरकारान्तः — scopes akāṛānta rules through raśmi. */
object PragRasmerAkarantaAdhikaraSutra : LinganushasanaSutra(
    number = "60",
    text = "प्राग्रश्मेरकारान्तः ।",
    hindiExplanation = "रश्मि-विषयक अगले अधिकार तक अकारान्त शब्दों का पुंलिङ्गाधिकार है।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    kind = LinganushasanaRuleKind.ADHIKARA,
) {
    override fun matches(context: LingaRuleContext): Boolean = false
}
