package dev.panini.linganushasanam.adhyaya3

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 157: यात्रामात्राभस्त्रादंष्ट्रावस्त्राः स्त्रियामेव । */
object YatraMatraBhastraDamstraVastraSutra : LinganushasanaSutra(
    number = "157", text = "यात्रामात्राभस्त्रादंष्ट्रावस्त्राः स्त्रियामेव ।", hindiExplanation = "यात्रा, मात्रा, भस्त्रा, दंष्ट्रा और वस्त्रा केवल स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLinga = Linga.STRI, priority = 50,
) {
    private val members = setOf("यात्रा", "मात्रा", "भस्त्रा", "दंष्ट्रा", "वस्त्रा")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

