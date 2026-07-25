package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class NewStemsDerivationTest {

    @Test
    fun `test guess method recognizes newly added stems`() {
        assertEquals(SubantaStemClass.I_STEM_FEMININE, SubantaStemClass.guess("गति"))
        assertEquals(SubantaStemClass.I_STEM_FEMININE, SubantaStemClass.guess("शान्ति"))
        assertEquals(SubantaStemClass.U_STEM_FEMININE, SubantaStemClass.guess("रेणु"))
        assertEquals(SubantaStemClass.UU_STEM_FEMININE, SubantaStemClass.guess("वधू"))
        assertEquals(SubantaStemClass.R_STEM_FEMININE, SubantaStemClass.guess("मातृ"))
        assertEquals(SubantaStemClass.A_STEM_NEUTER, SubantaStemClass.guess("पुष्प"))
        assertEquals(SubantaStemClass.A_STEM_NEUTER, SubantaStemClass.guess("मित्र"))
    }
}
