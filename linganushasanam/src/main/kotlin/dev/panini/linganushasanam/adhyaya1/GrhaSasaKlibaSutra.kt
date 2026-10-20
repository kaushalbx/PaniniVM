package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyEquals

/** Liṅgānuśāsanam 22: गृहशशाभ्यां क्लीबे. */
object GrhaSasaKlibaSutra : LinganushasanaSutra(
    number = "22",
    text = "गृहशशाभ्यां क्लीबे ।",
    hindiExplanation = "गृहस्थूणा और शशोर्णा जैसे गृह अथवा शश पूर्वपद वाले प्रयोग नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.NAPUMSAKA,
    priority = 40,
) {
    override fun matches(context: LingaRuleContext): Boolean {
        if (context.padas.size != 2) return false
        val purvapada = context.padas[0]
        val uttarapada = context.padas[1]
        return purvapada.phonologicallyEquals("गृह") && uttarapada.phonologicallyEquals("स्थूणा") ||
            purvapada.phonologicallyEquals("शश") && uttarapada.phonologicallyEquals("ऊर्णा")
    }
}
