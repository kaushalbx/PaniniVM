package dev.panini.linganushasanam.sutras
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 175: मृत्युसीधुकर्कन्धुकिष्कुकण्डुरेणवः । */
object MrtyuSidhuKarkandhuSutra : LinganushasanaSutra(
    "173", "मृत्युसीधुकर्कन्धुकिष्कुकण्डुरेणवः", "सूत्रोक्त शब्द और पाठान्तर स्त्रीलिङ्ग तथा पुंलिङ्ग दोनों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, targetLingas = setOf(Linga.STRI, Linga.PUMS), 50,
) { private val members = setOf("मृत्यु", "सीधु", "कर्कन्धु", "किष्कु", "कण्डु", "रेणु"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
