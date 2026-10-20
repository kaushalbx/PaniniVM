package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 32: चूल्लिवेणिखार्यश्च । (क्वचित्पुस्तके चुल्लि इति पाठः ।) */
object CulliVeniKhariSutra : LinganushasanaSutra(
    number = "32",
    text = "चुल्लिवेणिखार्यश्च",
    hindiExplanation = "चुल्लि, वेणि और खारि स्त्रीलिङ्ग होते हैं।",
    section = LinganushasanaSection.STRILINGA,
    targetLingas = setOf(Linga.STRI),
    priority = 50,
) {
    private val members = setOf("चुल्लि", "वेणि", "खारि")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

