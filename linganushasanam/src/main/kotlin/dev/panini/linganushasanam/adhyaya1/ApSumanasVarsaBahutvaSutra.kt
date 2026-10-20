package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 29: अप्सुमनस्समासिकतावर्षाणां बहुत्वञ्ज. */
object ApSumanasVarsaBahutvaSutra : LinganushasanaSutra(
    number = "29",
    text = "अप्सुमनस्समासिकतावर्षाणां बहुत्वञ्ज ।",
    hindiExplanation = "अप्, सुमनस्, समासिकता और वर्षा बहुवचन में स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 40,
) {
    private val members = setOf("अप्", "सुमनस्", "समासिकता", "वर्षा")

    override fun matches(context: LingaRuleContext): Boolean =
        context.vacana == Vacana.BAHUVACANA && context.pratipadika.phonologicallyIn(members)
}
