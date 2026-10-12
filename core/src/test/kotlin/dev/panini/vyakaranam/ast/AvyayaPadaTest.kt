package dev.panini.vyakaranam.ast

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AvyayaPadaTest {
    @Test
    fun `nonfinite derivations expose typed krt identity without changing source notation`() {
        val root = DhatuPrakriti("ignored", "कृ")
        val derivation = AvyayaKridantaDerivation(emptyList(), root, " क्त्वा ")
        assertEquals(dev.panini.core.KrtAffix.KTVA, derivation.krtAffix)
        assertEquals(" क्त्वा ", derivation.pratyaya)
        assertEquals(dev.panini.core.KrtAffix.LYAP, derivation.copy(pratyaya = "ल्यप्").krtAffix)
        assertEquals(dev.panini.core.KrtAffix.TUMUN, derivation.copy(pratyaya = "तुमुँन्").krtAffix)
        assertNull(derivation.copy(pratyaya = "unknown").krtAffix)
    }


    @Test
    fun `classifies nishedha particles`() {
        assertEquals(AvyayaFunction.NISHEDHA, AvyayaPada("न", "न").function)
        assertEquals(AvyayaFunction.NISHEDHA, AvyayaPada("मा", "मा").function)
    }

    @Test
    fun `classifies the quotative marker`() {
        assertEquals(AvyayaFunction.QUOTATIVE, AvyayaPada("इति", "इति").function)
    }

    @Test
    fun `classifies repetition marker surfaces`() {
        assertEquals(AvyayaFunction.REPETITION, AvyayaPada("पुनः", "पुनः").function)
        assertEquals(AvyayaFunction.REPETITION, AvyayaPada("पुनर्", "पुनर्").function)
    }

    @Test
    fun `leaves unrelated avyayas unclassified`() {
        assertNull(AvyayaPada("च", "च").function)
    }
}
