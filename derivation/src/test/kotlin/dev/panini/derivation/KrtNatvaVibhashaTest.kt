package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada4.HalashCejupadhatSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.NerVibhashaSutra
import dev.panini.ashtadhyayi.adhyaya8.pada4.VaNinsaNiksaNindamSutra
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** 8.4.30, 8.4.31, 8.4.33 optional ṇatva, and 8.4.2/8.4.14 at an upasarga boundary. */
class KrtNatvaVibhashaTest {
    private fun lyut(upasarga: String, root: String, sanadi: List<String> = emptyList()) =
        KrdantaEngine().derive(KrdantaDerivationRequest(root, Samjna.LYUT, upasarga = upasarga, sanadiPratyayas = sanadi)).final

    @Test fun `8_4_31 offers natva after a hal-initial ic-upadha root`() {
        val final = lyut("प्र", "कुपँ")
        assertEquals("प्रकोपन", final.surface)
        assertTrue(HalashCejupadhatSutra.matches(final))
        assertEquals("प्रकोपण", HalashCejupadhatSutra.applyAll(final).first().state.surface)
    }

    @Test fun `8_4_30 offers natva after nic`() {
        val final = lyut("प्र", "या", listOf("णिच्"))
        assertEquals("प्रयापन", final.surface)
        assertTrue(NerVibhashaSutra.matches(final))
        assertEquals("प्रयापण", NerVibhashaSutra.applyAll(final).first().state.surface)
    }

    @Test fun `8_4_29 stays obligatory for other roots`() {
        assertEquals("प्रयाण", lyut("प्र", "या").surface)
        assertFalse(HalashCejupadhatSutra.matches(lyut("प्र", "या")))
    }

    @Test fun `8_4_33 makes the root nasal optional for ninda`() {
        val final = lyut("प्र", "णिदिँ")
        assertEquals("प्रनिन्दन", final.surface)
        assertEquals("प्रणिन्दन", VaNinsaNiksaNindamSutra.applyAll(final).first().state.surface)
    }

    @Test fun `root nasal after upasarga only by 8_4_14`() {
        assertEquals("प्रनन्दन", lyut("प्र", "टुनदिँ").surface)
        assertEquals("प्रणमन", lyut("प्र", "णमँ").surface)
        assertEquals("परिणयन", lyut("परि", "णीञ्").surface)
    }
}
