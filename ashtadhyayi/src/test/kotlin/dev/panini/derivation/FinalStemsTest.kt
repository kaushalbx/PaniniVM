package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class FinalStemsTest {

    @Test
    fun `test guess method recognizes final s-stem masculine r-consonant and h-stems`() {
        assertEquals(SubantaStemClass.S_STEM_MASCULINE, SubantaStemClass.guess("चन्द्रमस्"))
        assertEquals(SubantaStemClass.S_STEM_MASCULINE, SubantaStemClass.guess("अङ्गिरस्"))
        assertEquals(SubantaStemClass.R_CONSONANT_STEM, SubantaStemClass.guess("गिर्"))
        assertEquals(SubantaStemClass.R_CONSONANT_STEM, SubantaStemClass.guess("पुर्"))
        assertEquals(SubantaStemClass.H_STEM, SubantaStemClass.guess("लिह्"))
        assertEquals(SubantaStemClass.H_STEM, SubantaStemClass.guess("मुह्"))
    }
}
