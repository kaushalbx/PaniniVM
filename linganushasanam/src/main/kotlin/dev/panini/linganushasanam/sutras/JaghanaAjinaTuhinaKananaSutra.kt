package dev.panini.linganushasanam.sutras

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 75: जघनाजिनतुहिनकाननवनवृजिनविपिनवेतनशासनसोपानमिथुन- श्मशानरत्ननिम्नचिह्नानि नपुंसके । */
object JaghanaAjinaTuhinaKananaSutra : LinganushasanaSutra(
    number = "75",
    text = "जघनाकिनतुहिनकाननवनवृजिनविपिनवेतनशासनसोपानमिथुनश्मशानरत्ननिम्नचिह्नानि नपुंसके",
    hindiExplanation = "सूत्र में पठित जघन आदि शब्द नपुंसकलिङ्ग होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLingas = setOf(Linga.NAPUMSAKA),
    priority = 50,
) {
    private val members = setOf("जघन", "अकिन", "तुहिन", "कानन", "वन", "वृजिन", "विपिन", "वेतन", "शासन", "सोपान", "मिथुन", "श्मशान", "रत्न", "निम्न", "चिह्न")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

