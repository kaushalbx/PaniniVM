package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals

class TaddhitaVikaraTest {
    @Test fun `taddhita identity distinguishes recognized affixes from their semantic families`() {
        for (identity in dev.panini.core.TaddhitaAffix.entries) {
            assertEquals(identity, TaddhitaVikara("display only", identity.upadesha).affix)
        }
        val comparative = TaddhitaVikara("display only", "तरप्")
        assertEquals(dev.panini.core.TaddhitaAffix.TARAP, comparative.affix)
        kotlin.test.assertNull(comparative.pratyayaClass)
        kotlin.test.assertNull(TaddhitaVikara("display only", "unknown").affix)
        kotlin.test.assertNull(TaddhitaVikara("display only", "unknown").pratyayaClass)
        assertEquals(dev.panini.core.TaddhitaAffix.MATUP, TaddhitaVikara("legacy", "मत्").affix)
    }

    @Test
    fun `morphological key is structural and can expose the untaddhita domain`() {
        val derived = MulaPratipadika(
            sourceText = "भ्रामकः पाठः",
            text = "गुण",
            vikaras = listOf(TaddhitaVikara("मतुप्", "मतुप्")),
        )

        assertEquals("गुण + मतुप्", derived.morphologicalKey())
        assertEquals("गुण", derived.morphologicalKey(includeTaddhita = false))
    }

    @Test
    fun `classifies possessive apatya and bhava affix spellings`() {
        listOf("मतुप्", "वतुप्", "मत्", "वत्").forEach { pratyaya ->
            assertEquals(
                TaddhitaPratyayaClass.POSSESSIVE,
                TaddhitaVikara(pratyaya, pratyaya).pratyayaClass,
            )
        }
        listOf("अण्", "इञ्").forEach { pratyaya ->
            assertEquals(
                TaddhitaPratyayaClass.APATYA,
                TaddhitaVikara(pratyaya, pratyaya).pratyayaClass,
            )
        }
        listOf("त्व", "तल्").forEach { pratyaya ->
            assertEquals(
                TaddhitaPratyayaClass.BHAVA,
                TaddhitaVikara(pratyaya, pratyaya).pratyayaClass,
            )
        }
    }
}
