package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 106: दाराक्षतलाजासूनां बहुत्वञ्ज. */
object DaraAksataLajaAsuBahutvaSutra : LinganushasanaSutra(
    number = "106", text = "दाराक्षतलाजासूनां बहुत्वञ्ज ।",
    hindiExplanation = "दार, अक्षत, लाज और असु बहुवचन में पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLinga = Linga.PUMS, priority = 40,
) {
    private val members = setOf("दार", "अक्षत", "लाज", "असु")
    override fun matches(context: LingaRuleContext): Boolean =
        context.vacana == Vacana.BAHUVACANA && context.pratipadika.phonologicallyIn(members)
}
