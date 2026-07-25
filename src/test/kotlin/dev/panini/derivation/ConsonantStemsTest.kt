package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class ConsonantStemsTest {

    @Test
    fun `test guess method recognizes consonant stems`() {
        assertEquals(SubantaStemClass.IN_STEM_MASCULINE, SubantaStemClass.guess("धनिन्"))
        assertEquals(SubantaStemClass.IN_STEM_MASCULINE, SubantaStemClass.guess("योगिन्"))
        assertEquals(SubantaStemClass.MATUP_STEM, SubantaStemClass.guess("भगवत्"))
        assertEquals(SubantaStemClass.MATUP_STEM, SubantaStemClass.guess("धीमत्"))
        assertEquals(SubantaStemClass.T_STEM, SubantaStemClass.guess("मरुत्"))
        assertEquals(SubantaStemClass.D_STEM, SubantaStemClass.guess("सम्पद्"))
        assertEquals(SubantaStemClass.C_STEM, SubantaStemClass.guess("वाच्"))
    }
}
