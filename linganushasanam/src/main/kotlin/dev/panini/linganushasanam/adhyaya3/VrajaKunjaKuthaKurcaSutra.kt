package dev.panini.linganushasanam.adhyaya3
import dev.panini.core.Linga
import dev.panini.linganushasanam.*
/** Liṅgānuśāsanam 181: व्रजकुञ्जकुथकूर्चप्रस्थदर्पार्भार्धर्चदर्भपुच्छाः । */
object VrajaKunjaKuthaKurcaSutra : LinganushasanaSutra(
    "181", "व्रजकुञ्जकुथकूर्चप्रस्थदर्पार्भार्धर्चदर्भपुच्छाः ।", "सूत्रोक्त शब्द पुंलिङ्ग और नपुंसकलिङ्ग दोनों में होते हैं।", LinganushasanaSection.PUMNAPUMSAKALINGA, Linga.PUMS, 50,
) { override val targetLingas = setOf(Linga.PUMS, Linga.NAPUMSAKA); private val members = setOf("व्रज", "कुञ्ज", "कुथ", "कूर्च", "प्रस्थ", "दर्प", "अर्भ", "अर्धर्च", "दर्भ", "पुच्छ"); override fun matches(context: LingaRuleContext) = context.pratipadika.phonologicallyIn(members) }
