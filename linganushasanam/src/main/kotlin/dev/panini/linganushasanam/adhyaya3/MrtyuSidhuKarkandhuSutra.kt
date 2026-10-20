package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 175, including transmitted variants मन्यु and सिन्धु. */
object MrtyuSidhuKarkandhuSutra : LinganushasanaSutra(
    "175", "मृत्युशीधुकर्कन्धुकिष्कुकण्डुरेणवः । (मृत्यु स्थाने मन्यु, किष्कु स्थाने सिन्धु इति केचित्)", "सूत्रोक्त शब्द और पाठान्तर स्त्रीलिङ्ग तथा पुंलिङ्ग दोनों में होते हैं।",
    LinganushasanaSection.STRIPUMSALINGA, Linga.STRI, 50,
) { override val targetLingas = setOf(Linga.STRI, Linga.PUMS); private val members = setOf("मृत्यु", "मन्यु", "शीधु", "कर्कन्धु", "किष्कु", "सिन्धु", "कण्डु", "रेणु"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
