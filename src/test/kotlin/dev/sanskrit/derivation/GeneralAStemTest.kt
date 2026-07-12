package dev.sanskrit.derivation

import kotlin.test.Test
import kotlin.test.assertEquals

class GeneralAStemTest {
    @Test
    fun `a-stem derivation does not require retroflexion when its environment is absent`() {
        val engine = SubantaEngine()

        assertEquals("देवेन", engine.derive(SubantaDerivationRequest("देव", Vibhakti.TRTIYA, Vacana.EKAVACANA)).final.surface)
        assertEquals("देवानाम्", engine.derive(SubantaDerivationRequest("देव", Vibhakti.SASTHI, Vacana.BAHUVACANA)).final.surface)
    }

    @Test
    fun `complete paradigm generalises from rama to deva`() {
        val forms = SubantaEngine().deriveSupportedParadigm("देव").surfaces

        assertEquals(21, forms.size)
        assertEquals("देवः", forms[SupAffix.SU])
        assertEquals("देवाः", forms[SupAffix.JAS])
        assertEquals("देवेषु", forms[SupAffix.SUP])
    }
}
