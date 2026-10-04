package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals

class TaddhitaVikaraTest {
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
