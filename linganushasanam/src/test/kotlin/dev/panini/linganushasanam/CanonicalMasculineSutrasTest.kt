package dev.panini.linganushasanam

import dev.panini.core.Linga
import dev.panini.linganushasanam.adhyaya2.ShopadhahSutra
import dev.panini.linganushasanam.adhyaya2.TopadhahSutra
import dev.panini.linganushasanam.adhyaya2.UkarantahSutra
import dev.panini.linganushasanam.adhyaya3.BhrtrAmitraChatraPutraSutra
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CanonicalMasculineSutrasTest {
    @Test fun `51 assigns short u stems and rejects its exclusive exceptions`() {
        for (stem in listOf("गुरु", "हेतु")) assertTrue(UkarantahSutra.matches(LingaRuleContext(stem)), stem)
        for (stem in listOf("धेनु", "श्मश्रु", "वस्तु")) assertFalse(UkarantahSutra.matches(LingaRuleContext(stem)), stem)
        assertEquals(Linga.PUMS, UkarantahSutra.apply(LingaRuleContext("गुरु")).let { it as LingaRuleResult.Matched }.linga)
    }

    @Test fun `64 and 93 inspect canonical upadha varnas rather than spelling suffixes`() {
        assertTrue(TopadhahSutra.matches(LingaRuleContext("घट")))
        assertFalse(TopadhahSutra.matches(LingaRuleContext("किरीट")))
        assertFalse(TopadhahSutra.matches(LingaRuleContext("घटी")))
        assertTrue(ShopadhahSutra.matches(LingaRuleContext("दोष")))
        assertFalse(ShopadhahSutra.matches(LingaRuleContext("किल्विष")))
        assertFalse(ShopadhahSutra.matches(LingaRuleContext("दोषी")))
    }

    @Test fun `158 supplies the listed masculine exception inside neuter section`() {
        assertTrue(BhrtrAmitraChatraPutraSutra.matches(LingaRuleContext("पुत्र")))
        assertTrue(BhrtrAmitraChatraPutraSutra.matches(LingaRuleContext("मन्त्र")))
        assertFalse(BhrtrAmitraChatraPutraSutra.matches(LingaRuleContext("पात्र")))
        assertEquals(LinganushasanaSection.NAPUMSAKALINGA, BhrtrAmitraChatraPutraSutra.section)
        assertEquals(Linga.PUMS, BhrtrAmitraChatraPutraSutra.targetLinga)
    }

    @Test fun `canonical sutras are registered by their text identities`() {
        for (sutra in listOf(UkarantahSutra, TopadhahSutra, ShopadhahSutra, BhrtrAmitraChatraPutraSutra)) {
            assertSame(sutra, LinganushasanamRegistry.require(sutra.number))
        }
    }
}
