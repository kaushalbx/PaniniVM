package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class SpecializedStemsTest {

    @Test
    fun `test guess method recognizes specialized diphthong irregular pathin and numeral stems`() {
        assertEquals(SubantaStemClass.DIPHTHONG_STEM, SubantaStemClass.guess("गो"))
        assertEquals(SubantaStemClass.DIPHTHONG_STEM, SubantaStemClass.guess("द्यौ"))
        assertEquals(SubantaStemClass.PATHIN_STEM, SubantaStemClass.guess("पथिन्"))
        assertEquals(SubantaStemClass.IRREGULAR_N_STEM, SubantaStemClass.guess("अहन्"))
        assertEquals(SubantaStemClass.IRREGULAR_N_STEM, SubantaStemClass.guess("श्वन्"))
        assertEquals(SubantaStemClass.NUMERAL_CARDINAL, SubantaStemClass.guess("पञ्चन्"))
        assertEquals(SubantaStemClass.NUMERAL_CARDINAL, SubantaStemClass.guess("चतुर्"))
    }
}
