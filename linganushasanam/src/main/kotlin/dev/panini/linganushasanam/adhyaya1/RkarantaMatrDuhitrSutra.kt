package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 3: ऋकारान्ता मातृदुहितृयातृस्वसृननान्दरः. */
object RkarantaMatrDuhitrSutra : LinganushasanaSutra(
    number = "3",
    text = "ऋकारान्ता मातृदुहितृयातृस्वसृननान्दरः । (यातृ स्थाने पोतृ इति क्वचित्)",
    hindiExplanation = "मातृ, दुहितृ, यातृ/पोतृ, स्वसृ और ननान्दृ आदि ऋकारान्त शब्द स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    private val members = setOf("मातृ", "दुहितृ", "यातृ", "पोतृ", "स्वसृ", "ननान्दृ")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}
