package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.*
import kotlin.test.*

class NominalModifierAttachmentTest {
    private fun noun(stem: String, ending: String) = SubantaPada(stem, MulaPratipadika(stem, stem), SupPratyaya(ending, ending))

    @Test fun `attachment uses atomic case and number slots not position`() {
        val modifier = noun("प्रथम", "अम्")
        val matching = noun("फल", "अम्")
        val otherCase = noun("फल", "सुँ")
        for (heads in listOf(listOf(matching, otherCase), listOf(otherCase, matching))) {
            val result = NominalModifierAttachment.analyze(modifier, heads)
            assertEquals(heads.indexOf(matching), assertNotNull(result.unique).headIndex)
            assertTrue(result.unique!!.knownGenderAgreement)
            assertNull(result.diagnostic)
        }
        assertNotNull(NominalModifierAttachment.analyze(noun("प्रथम", "unknown"), listOf(matching)).diagnostic)
    }

    @Test fun `equal-looking head occurrences remain ambiguous without structural evidence`() {
        val head = noun("फल", "अम्")
        val result = NominalModifierAttachment.analyze(noun("प्रथम", "अम्"), listOf(head, head.copy()))
        assertNull(result.unique)
        assertEquals(listOf(0, 1), result.candidates.map { it.headIndex })
        assertEquals(NominalAttachmentDiagnostic.AMBIGUOUS_HEADS, result.diagnostic)
    }

    @Test fun `known gender mismatch rejects a candidate but unknown gender stays explicit`() {
        assertTrue(NominalModifierAttachment.analyze(noun("प्रथमा", "अम्"), listOf(noun("फल", "अम्"))).candidates.isEmpty())
        val candidate = NominalModifierAttachment.analyze(noun("प्रथम", "अम्"), listOf(noun("अज्ञात", "अम्"))).unique
        assertFalse(assertNotNull(candidate).knownGenderAgreement)
    }

    @Test fun `syncretic morphology retains every shared slot`() {
        val result = NominalModifierAttachment.analyze(noun("प्रथम", "भ्याम्"), listOf(noun("मान", "भ्याम्")))
        assertEquals(3, assertNotNull(result.unique).slots.size)
    }
}
