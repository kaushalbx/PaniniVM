package dev.panini.vyakaranam.ast

import dev.panini.core.SupAffix
import kotlin.test.*

class PrakriyaQualifierTest {
    @Test fun `procedure qualifier recognition requires the full nominative singular slot`() {
        for (affix in SupAffix.entries) {
            val nominal = SubantaPada("नित्य", MulaPratipadika("नित्य", "नित्य"),
                SupPratyaya(affix.upadesha, affix.upadesha))
            assertEquals(if (affix == SupAffix.SU) PrakriyaDefinitionQualifier.NITYA else null,
                nominal.prakriyaDefinitionQualifier(), affix.name)
        }
    }

    @Test fun `recognized feminine suffix preserves qualifier but unrelated derivation does not`() {
        val nominal = SubantaPada("नित्य", MulaPratipadika("नित्य", "नित्य"), SupPratyaya("सुँ", "सुँ"))
        fun classify(vikara: PratipadikaVikara) = nominal.copy(pratipadika =
            (nominal.pratipadika as MulaPratipadika).copy(vikaras = listOf(vikara))).prakriyaDefinitionQualifier()
        assertEquals(PrakriyaDefinitionQualifier.NITYA, classify(StriVikara("टाप्", "टाप्")))
        assertNull(classify(StriVikara("unknown", "unknown")))
        assertNull(classify(TaddhitaVikara("तरप्", "तरप्")))
    }
}
