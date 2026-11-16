package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada3.InahShahSutra
import dev.panini.ashtadhyayi.adhyaya8.pada3.SoPadadauSutra
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.shiksha.Samjna
import kotlin.test.*

class SamasaVisargaTest {
    private val engine = SandhiEngine()
    private fun samasa(left: String, right: String, uttarapada: Boolean = false) =
        engine.joinSamasaMembers(left, right, uttarapada).final.surface

    @Test fun `8_3_45 gives sh in a compound for is and us`() {
        assertEquals("सर्पिष्कुण्डिका", samasa("सर्पिस्", "कुण्डिका"))
        assertEquals("धनुष्कपालम्", samasa("धनुस्", "कपालम्"))
        assertEquals("सर्पिष्कलश", samasa("सर्पिस्", "कलश"))
        // In an uttarapada the rule does not hold (सर्पिष्कुण्डिका itself is a कस्कादि word, 8.3.48).
        assertNotEquals("सर्पिष्कलश", samasa("सर्पिस्", "कलश", uttarapada = true))
        // Outside a compound: not this rule.
        assertNotEquals("सर्पिष्कलश", engine.join("सर्पिस्", "कलश").final.surface)
    }

    @Test fun `8_3_46 gives s after a before the listed uttarapadas`() {
        assertEquals("अयस्कुम्भ", samasa("अयस्", "कुम्भ"))
        assertEquals("अयस्पात्र", samasa("अयस्", "पात्र"))
        // Not a listed word.
        assertNotEquals("अयस्कटाह", samasa("अयस्", "कटाह"))
    }

    @Test fun `8_3_47 gives s for adhas and shiras before pada`() {
        assertEquals("अधस्पद", samasa("अधस्", "पद"))
        assertEquals("शिरस्पद", samasa("शिरस्", "पद"))
    }

    @Test fun `8_3_48 follows the kaskadi gana`() {
        assertEquals("कस्कः", engine.join("कः", "कः").final.surface)
        assertEquals("भ्रातुष्पुत्रः", engine.join("भ्रातुः", "पुत्रः").final.surface)
    }

    private fun affixState(stem: String, affix: String, surface: String) = DerivationState(listOf(
        DerivationTerm("stem", stem, TermKind.PRATIPADIKA, upadesha = stem),
        DerivationTerm("affix", surface, TermKind.PRATYAYA, upadesha = affix),
    ), stage = DerivationStage.PADA_FORMED, samjnas = setOf(SamjnaAssignment("stem", Samjna.PADA)))

    @Test fun `8_3_38 and 8_3_39 before the paasha kalpa ka kaamya affixes`() {
        val payas = affixState("पयः", "काम्यच्", "काम्य")
        assertTrue(SoPadadauSutra.matches(payas) && !InahShahSutra.matches(payas))
        assertEquals("पयस्काम्य", SoPadadauSutra.apply(payas).state.surface)
        val sarpis = affixState("सर्पिः", "पाशप्", "पाश")
        assertTrue(InahShahSutra.matches(sarpis) && !SoPadadauSutra.matches(sarpis))
        assertEquals("सर्पिष्पाश", InahShahSutra.apply(sarpis).state.surface)
        // A pada-initial ku/pu is not this rule.
        assertFalse(SoPadadauSutra.matches(affixState("पयः", "कृत", "कृत")))
    }
}
