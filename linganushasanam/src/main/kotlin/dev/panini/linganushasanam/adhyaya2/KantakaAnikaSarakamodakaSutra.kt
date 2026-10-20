package dev.panini.linganushasanam.adhyaya2

import dev.panini.core.Linga
import dev.panini.linganushasanam.LingaRuleContext
import dev.panini.linganushasanam.LinganushasanaSection
import dev.panini.linganushasanam.LinganushasanaSutra
import dev.panini.linganushasanam.phonologicallyIn

/** Liṅgānuśāsanam 63: कण्टकानीकसरकमोदकचसकमस्तकपुस्तकतटाकनिष्कशुष्क- वर्चस्कपिनाकभाण्डकपिण्डककटकदण्डकपिटकतालकफल- ककल्कपुलाकानि नपुंसके च । */
object KantakaAnikaSarakamodakaSutra : LinganushasanaSutra(
    number = "63",
    text = "कण्टकानीकसरकमोदकचसकमस्तकपुस्तकतटाकनिष्कशुष्क- वर्चस्कपिनाकभाण्डकपिण्डककटकदण्डकपिटकतालकफल- ककल्कपुलाकानि नपुंसके च ।",
    hindiExplanation = "सूत्र में पठित कण्टक आदि शब्द पुंलिङ्ग तथा नपुंसकलिङ्ग दोनों में प्रयुक्त होते हैं।",
    section = LinganushasanaSection.PUMLINGA,
    targetLinga = Linga.PUMS,
    priority = 50,
) {
    override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA)
    private val members = setOf("कण्टक", "अनीक", "सरक", "मोदक", "चसक", "मस्तक", "पुस्तक", "तटाक", "निष्क", "शुष्क", "वर्चस्क", "पिनाक", "भाण्डक", "पिण्डक", "कटक", "दण्डक", "पिटक", "तालक", "फलक", "कल्क", "पुलाक")

    override fun matches(context: LingaRuleContext): Boolean = context.pratipadika.phonologicallyIn(members)
}

