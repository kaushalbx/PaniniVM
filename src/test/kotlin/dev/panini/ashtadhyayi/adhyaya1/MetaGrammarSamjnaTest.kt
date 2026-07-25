package dev.panini.ashtadhyayi.adhyaya1

import dev.panini.ashtadhyayi.adhyaya1.pada1.AloAntyatPurvaUpadhaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.AnuditSavarnasyaCapratyayahSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SnatSatSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.SvamRupamSabdasyasabdasamjnaSutra
import dev.panini.ashtadhyayi.adhyaya1.pada1.TarapTamapGhahSutra
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MetaGrammarSamjnaTest {

    @Test
    fun testTarapTamapGhahSutra() {
        assertTrue(TarapTamapGhahSutra.matches("तरप्"))
        assertTrue(TarapTamapGhahSutra.matches("तमप्"))
        assertEquals("घ", TarapTamapGhahSutra.apply("तरप्"))
    }

    @Test
    fun testSnatSatSutra() {
        assertTrue(SnatSatSutra.matches("षष्"))
        assertTrue(SnatSatSutra.matches("पञ्चन्"))
        assertEquals("षट्", SnatSatSutra.apply("षष्"))
    }

    @Test
    fun testAloAntyatPurvaUpadhaSutra() {
        assertTrue(AloAntyatPurvaUpadhaSutra.matches("राम"))
        assertEquals('ा', AloAntyatPurvaUpadhaSutra.apply("राम"))
    }

    @Test
    fun testSvamRupamSabdasyasabdasamjnaSutra() {
        assertTrue(SvamRupamSabdasyasabdasamjnaSutra.matches("अग्नि"))
        assertEquals("अग्नि", SvamRupamSabdasyasabdasamjnaSutra.apply("अग्नि"))
    }

    @Test
    fun testAnuditSavarnasyaCapratyayahSutra() {
        assertTrue(AnuditSavarnasyaCapratyayahSutra.matches("कु"))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.matches("अ"))
        assertTrue(AnuditSavarnasyaCapratyayahSutra.apply("कु"))
    }
}
