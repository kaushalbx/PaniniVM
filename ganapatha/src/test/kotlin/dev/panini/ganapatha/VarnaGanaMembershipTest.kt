package dev.panini.ganapatha

import dev.panini.shiksha.toVarnas
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VarnaGanaMembershipTest {
    @Test
    fun `rajadantadi retains listed forms alongside explicit stem identities`() {
        assertEquals(57, RajadantadiGana.members.size)
        assertEquals("राजदन्तः", RajadantadiGana.members.first().text)
        assertEquals("वृद्धिगुणौ", RajadantadiGana.members.last().text)
        for (member in RajadantadiGana.members) {
            assertTrue(RajadantadiGana.contains(member.varnas), member.text)
            assertTrue(RajadantadiGana.contains(requireNotNull(member.upadeshaVarnas)), member.text)
            assertTrue(RajadantadiGana.contains(member.text), member.text)
        }
    }

    @Test
    fun `inflected endings do not determine stem identity by prefix`() {
        for (stem in listOf("राजदन्त", "स्नातकराजन्", "भार्यापति", "पुत्रपशु", "मधुसर्पिस्", "गुणवृद्धि")) {
            assertTrue(RajadantadiGana.contains(stem.toVarnas()), stem)
        }
        for (prefix in listOf("", "राजद", "स्नातक", "भार्या", "मधुसर्पि", "गुणवृद्ध")) {
            assertFalse(RajadantadiGana.contains(prefix.toVarnas()), prefix)
        }
    }
}
