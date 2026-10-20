package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 155: यात्रामात्राभस्त्रादंष्ट्रावरत्राः स्त्रियामेव । */
object YatraMatraBhastraDamstraVastraSutra : LinganushasanaSutra(
    number = "155", text = "यात्रामात्राभस्त्रादंष्ट्रावरत्राः स्त्रियामेव", hindiExplanation = "यात्रा, मात्रा, भस्त्रा, दंष्ट्रा और वरत्रा केवल स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.NAPUMSAKALINGA, targetLingas = setOf(Linga.STRI), priority = 50,
) {
    private val members = setOf("यात्रा", "मात्रा", "भस्त्रा", "दंष्ट्रा", "वरत्रा")
    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

