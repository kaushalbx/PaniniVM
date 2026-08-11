package dev.panini.execution

import dev.panini.core.Linga
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.PratipadikaLexicon
import kotlin.test.Test
import kotlin.test.assertTrue

class PvmNumeralGenderAgreementTest {
    private val lexicon = object : PratipadikaLexicon {
        private val entries = mapOf(
            "अश्व" to PratipadikaEntry("अश्व", setOf(Linga.PUMS)),
            "नदी" to PratipadikaEntry("नदी", setOf(Linga.STRI)),
        )

        override fun findPratipadika(text: String): PratipadikaEntry? = entries[text]
    }

    @Test
    fun `standalone numeral retains neutral program-value rendering`() {
        val rendered = PvmUktiSadhaka(pratipadikaLexicon = lexicon)
            .sadhayaLine("द्वि + औट् च मुद्र् + णिच् + लोट् + सिप् ।")

        assertTrue(rendered.startsWith("द्वे "), rendered)
    }

    @Test
    fun `numeral receives masculine gender from adjacent counted noun`() {
        val rendered = PvmUktiSadhaka(pratipadikaLexicon = lexicon)
            .sadhayaLine("द्वि + औट् अश्व + औट् च मुद्र् + णिच् + लोट् + सिप् ।")

        assertTrue(rendered.startsWith("द्वौ "), rendered)
    }

    @Test
    fun `numeral receives feminine gender from adjacent counted noun`() {
        val rendered = PvmUktiSadhaka(pratipadikaLexicon = lexicon)
            .sadhayaLine("द्वि + औट् नदी + औट् च मुद्र् + णिच् + लोट् + सिप् ।")

        assertTrue(rendered.startsWith("द्वे "), rendered)
    }
}
