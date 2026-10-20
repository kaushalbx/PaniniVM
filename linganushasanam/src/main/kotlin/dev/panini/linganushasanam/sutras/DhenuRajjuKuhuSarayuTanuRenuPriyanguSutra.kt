package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 52: धेनुरज्जुकुहुसरयुतनुरेणुप्रियङ्गवः स्त्रियाम् । */
object DhenuRajjuKuhuSarayuTanuRenuPriyanguSutra : LinganushasanaSutra(
    number = "52",
    text = "धेनुरज्जुकुहूसरयुतनुरेणुप्रियङ्गवः स्त्रियाम् ।",
    hindiExplanation = "धेनु, रज्जु, कुहु, सरयु, तनु, रेणु और प्रियङ्गु स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("धेनु", "रज्जु", "कुहु", "सरयु", "तनु", "रेणु", "प्रियङ्गु")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

