package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class AdvancedStemsTest {

    @Test
    fun `test guess method recognizes advanced directional pronominal and consonant stems`() {
        assertEquals(SubantaStemClass.PRONOMINAL_PERSONAL, SubantaStemClass.guess("युष्मद्"))
        assertEquals(SubantaStemClass.PRONOMINAL_PERSONAL, SubantaStemClass.guess("अस्मद्"))
        assertEquals(SubantaStemClass.PRONOMINAL_PERSONAL, SubantaStemClass.guess("तद्"))
        assertEquals(SubantaStemClass.PRONOMINAL_STEM, SubantaStemClass.guess("सर्व"))
        assertEquals(SubantaStemClass.PRONOMINAL_STEM, SubantaStemClass.guess("विश्व"))
        assertEquals(SubantaStemClass.ANCH_STEM, SubantaStemClass.guess("प्राञ्च्"))
        assertEquals(SubantaStemClass.J_STEM, SubantaStemClass.guess("भिषज्"))
        assertEquals(SubantaStemClass.SH_STEM, SubantaStemClass.guess("द्विष्"))
    }
}
