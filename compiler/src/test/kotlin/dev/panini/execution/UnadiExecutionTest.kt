package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class UnadiExecutionTest {
    @Test
    fun testUnadiKaruDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("कृ + उणादि(उण्) + सुँ ।")
        assertEquals("कारुः ।", result)
    }

    @Test
    fun testUnadiVayuDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("वा + उणादि(उण्) + सुँ ।")
        assertEquals("वायुः ।", result)
    }

    @Test
    fun testUnadiPumasDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("पुम् + उणादि(असुन्) + सुँ ।")
        assertEquals("पुमः ।", result)
    }

    @Test
    fun testUnadiKarmanDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("कृ + उणादि(कनिन्) + सुँ ।")
        assertEquals("कर्मा ।", result)
    }

    @Test
    fun testUnadiSaruDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("सृ + उणादि(उण्) + सुँ ।")
        assertEquals("सरुः ।", result)
    }

    @Test
    fun testUnadiKuruDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("कृ + उणादि(कुः) + सुँ ।")
        assertEquals("कुरुः ।", result)
    }

    @Test
    fun testUnadiGuruDerivation() {
        val sadhaka = PvmUktiSadhaka()
        val result = sadhaka.sadhayaLine("गॄ + उणादि(कुः) + सुँ ।")
        assertEquals("गुरुः ।", result)
    }
}
