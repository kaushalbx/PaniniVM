package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 140: सीरार्थौदनाः पुंसि । */
object SiraArthaOdanaPumsiSutra : LinganushasanaSutra(
    number = "140", text = "सीरार्थौदनाः पुंसि ।", hindiExplanation = "सीर, अर्थ और ओदन पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 50,
) {
    private val members = setOf("सीर", "अर्थ", "ओदन")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

