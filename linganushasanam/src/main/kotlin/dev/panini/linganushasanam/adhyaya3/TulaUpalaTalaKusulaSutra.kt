package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 144: तूलोपलतालकुसूलतरलकम्बलदेवलवृषलाः पुंसि । */
object TulaUpalaTalaKusulaSutra : LinganushasanaSutra(
    number = "144", text = "तूलोपलतालकुसूलतरलकम्बलदेवलवृषलाः पुंसि ।", hindiExplanation = "तूल, उपल, ताल, कुसूल, तरल, कम्बल, देवल और वृषल पुंलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.PUMS, priority = 50,
) {
    private val members = setOf("तूल", "उपल", "ताल", "कुसूल", "तरल", "कम्बल", "देवल", "वृषल")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

