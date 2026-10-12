package dev.panini.vyakaranam.parser

import dev.panini.core.KrtAffix
import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class TypedKrtNotationTest {
    @Test fun `canonical tumun spelling retains nonfinite derivation`() {
        val pada = assertIs<AvyayaPada>(PaniniParser().parse("युज् + तुमुँन् ।")
            .grammaticalVakyas().single().padas.single())
        assertEquals(KrtAffix.TUMUN, assertIs<AvyayaKridantaDerivation>(pada.derivation).krtAffix)
    }

    @Test fun `canonical ktavatu spelling retains krt structure`() {
        for (ending in listOf("क्तवतुँ")) {
            val pada = assertIs<SubantaPada>(PaniniParser().parse("युज् + $ending + ङस् ।")
                .grammaticalVakyas().single().padas.single())
            val stem = assertIs<KridantaPratipadika>(pada.pratipadika)
            assertEquals(KrtAffix.KTAVATU, stem.krtAffix)
            assertEquals(ending, stem.krtPratyaya)
        }
    }

    @Test fun `unmarked ktavatu is not an alternative affix upadesha`() {
        assertNull(KrtAffix.fromUpadesha("क्तवतु"))
        val parsed = PaniniParser().parseOrNull("युज् + क्तवतु + ङस् ।")
        val stems = parsed?.grammaticalVakyas()?.flatMap { it.padas }
            ?.filterIsInstance<SubantaPada>()?.map { it.pratipadika }.orEmpty()
        assertTrue(stems.none { it is KridantaPratipadika && it.krtAffix == KrtAffix.KTAVATU })
    }
}
