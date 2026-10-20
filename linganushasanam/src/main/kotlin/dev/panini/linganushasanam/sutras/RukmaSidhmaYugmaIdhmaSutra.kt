package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 84: रुक्मसिध्मयुग्मेध्मगुल्माध्यात्मकुङ्कुमानि नपुंसके । */
object RukmaSidhmaYugmaIdhmaSutra : LinganushasanaSutra(
    number = "84",
    text = "रुक्मसिध्मयुग्मेध्मगुल्माध्यात्मकुङ्कुमानि नपुंसके",
    hindiExplanation = "सूत्र में पठित रुक्म आदि शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("रुक्म", "सिध्म", "युग्म", "इध्म", "गुल्म", "अध्यात्म", "कुङ्कुम")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

