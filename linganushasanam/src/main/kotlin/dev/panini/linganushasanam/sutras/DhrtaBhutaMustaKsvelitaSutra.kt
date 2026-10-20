package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 177: घृतभूतमुस्तक्ष्वेलितैरावतपुस्तकबुस्तलोहिताः । */
object DhrtaBhutaMustaKsvelitaSutra : LinganushasanaSutra(
    "177", "घृतभूतमुस्तक्ष्वेलितैरावतपुस्तकबुस्तलोहिताः", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA), 50,
) { private val members = setOf("घृत", "भूत", "मुस्त", "क्ष्वेलित", "ऐरावत", "पुस्तक", "बुस्त", "लोहित"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
