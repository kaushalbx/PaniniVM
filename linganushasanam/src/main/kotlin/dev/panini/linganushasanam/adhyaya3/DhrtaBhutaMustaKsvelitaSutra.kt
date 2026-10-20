package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 179: धृतभूतमुस्तक्ष्वेलितैरावतपुस्तबुस्तलोहिताः । */
object DhrtaBhutaMustaKsvelitaSutra : LinganushasanaSutra(
    "179", "धृतभूतमुस्तक्ष्वेलितैरावतपुस्तबुस्तलोहिताः ।", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, Linga.PUMS, 50,
) { override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA); private val members = setOf("धृत", "भूत", "मुस्त", "क्ष्वेलित", "ऐरावत", "पुस्त", "बुस्त", "लोहित"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
