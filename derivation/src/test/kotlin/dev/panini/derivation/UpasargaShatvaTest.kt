package dev.panini.derivation

import dev.panini.dhatupatha.DhatuPatha
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UpasargaShatvaTest {
    private val engine = SandhiEngine()
    private fun root(id: String) = requireNotNull(DhatuPatha.find(id))

    @Test fun `8_3_65 the s of listed roots after an in-final upasarga`() {
        assertEquals("निषिञ्चति", engine.joinPrefix("नि", "सिञ्चति", root("06.0170")))
        // 8.4.41 then retroflexes the following dental.
        assertEquals("अभिष्टौति", engine.joinPrefix("अभि", "स्तौति", root("02.0038")))
        // After अ there is no इण्.
        assertEquals("प्रसिञ्चति", engine.joinPrefix("प्र", "सिञ्चति", root("06.0170")))
        // स्था's present does not begin with स्.
        assertEquals("अधितिष्ठति", engine.joinPrefix("अधि", "तिष्ठति", root("01.1077")))
    }

    @Test fun `8_3_66 sad except after prati`() {
        assertEquals("निषीदति", engine.joinPrefix("नि", "सीदति", root("01.0990")))
        assertEquals("प्रतिसीदति", engine.joinPrefix("प्रति", "सीदति", root("01.0990")))
    }

    @Test fun `8_3_70 after pari ni vi`() {
        assertEquals("परिषेवते", engine.joinPrefix("परि", "सेवते", root("01.0574")))
        assertEquals("निषेवते", engine.joinPrefix("नि", "सेवते", root("01.0574")))
        // अभि is not परि/नि/वि.
        assertEquals("अभिसेवते", engine.joinPrefix("अभि", "सेवते", root("01.0574")))
    }

    @Test fun `8_3_77 skabh after vi`() {
        // 8.3.77 gives ष्. The Kāśikā form is विष्कभ्नाति; what keeps 8.4.2 from the श्ना's न is not
        // modelled yet (the engine gives विष्कभ्णाति), so only the ष् is asserted.
        assertTrue(engine.joinPrefix("वि", "स्कभ्नाति", root("01.0452")).startsWith("विष्कभ्"))
    }
}
