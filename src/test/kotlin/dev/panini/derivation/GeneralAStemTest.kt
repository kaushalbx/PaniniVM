package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class GeneralAStemTest {
    @Test
    fun `complete paradigm generalises from rama to deva`() {
        val forms = SubantaEngine().deriveSupportedParadigm("देव").surfaces

        assertEquals(21, forms.size)
        assertEquals("देवः", forms[SupAffix.SU])
        assertEquals("देवाः", forms[SupAffix.JAS])
        assertEquals("देवेन", forms[SupAffix.TA])
        assertEquals("देवानाम्", forms[SupAffix.AM_6])
        assertEquals("देवेषु", forms[SupAffix.SUP])
    }
}
