package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.core.Vacana
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 106: दाराक्षतलजासूनां बहुत्वं च । */
object DaraAksataLajaAsuBahutvaSutra : LinganushasanaSutra(
    number = "106", text = "दाराक्षतलजासूनां बहुत्वं च",
    hindiExplanation = "दार, अक्षत, लजा और असु बहुवचन में पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA, targetLingas = setOf(Linga.PUMS), priority = 40,
) {
    private val members = setOf("दार", "अक्षत", "लजा", "असु")
    override fun matches(context: LingaRuleContext): Boolean =
        context.vacana == Vacana.BAHUVACANA && context.pratipadika.phonologicallyIn(members)
}
