package dev.panini.linganushasanam.adhyaya1

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 30: स्रक्त्वग्ज्योग्वाग्यवागूनौस्फिचः ।(क्वचित् स्रक् स्थाने स्रुक् अथवा शुक, स्फिच स्थाने स्फिज इति पाठः) */
object SrajTvacJyokVacYavaguNauSphicSutra : LinganushasanaSutra(
    number = "30",
    text = "स्रक्त्वग्ज्योग्वाग्यवागूनौस्फिचः ।(क्वचित् स्रक् स्थाने स्रुक् अथवा शुक, स्फिच स्थाने स्फिज इति पाठः)",
    hindiExplanation = "सूत्र में पठित स्रज् आदि शब्द तथा स्वीकृत पाठान्तर स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLinga = Linga.STRI,
    priority = 50,
) {
    private val members = setOf("स्रज्", "त्वच्", "ज्योक्", "वाच्", "यवागू", "नौ", "स्फिच्", "स्फिज्")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

