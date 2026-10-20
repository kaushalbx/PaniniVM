package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 76: मानयानाभिधाननलिनपुलिनोद्यानशयनासनस्थानचन्दनालानसमान- भवनवसनसम्भावनविभा-वनविमानानि नपुंसके च । */
object ManaYanaAbhidhanaNalinaSutra : LinganushasanaSutra(
    number = "76",
    text = "मानयानाभिधाननलिनपुलिनोद्यानशयनासनस्थानचन्दनासमानभवनवसनसंभावनविभावनविमानानि नपुंसके च",
    hindiExplanation = "सूत्र में पठित मान आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("मान", "यान", "अभिधान", "नलिन", "पुलिन", "उद्यान", "शयन", "आसन", "स्थान", "चन्दन", "असमान", "भवन", "वसन", "सम्भावन", "विभावन", "विमान")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

