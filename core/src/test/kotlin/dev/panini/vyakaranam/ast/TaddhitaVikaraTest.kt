package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TaddhitaVikaraTest {
    @Test
    fun `classifies possessive and apatya affix spellings`() {
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
        assertNull(TaddhitaVikara("तल्", "तल्").pratyayaClass)
    }
}
