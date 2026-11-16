package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TukAndDvitvaSandhiTest {
    private val evaluator = SandhiSequenceEvaluator()
    private fun default(vararg words: String) = evaluator.evaluate(words.toList()).single().surface
    private fun all(vararg words: String) = evaluator.evaluate(words.toList(), alternatives = true).map { it.surface }.toSet()

    @Test fun `6_1_73 inserts tuk after a short vowel before cha across padas`() {
        assertEquals("तवच्छाया", default("तव", "छाया"))
    }

    @Test fun `6_1_76 makes tuk optional after a pada-final long vowel`() {
        assertTrue(setOf("लक्ष्मीच्छाया", "लक्ष्मीछाया").all { it in all("लक्ष्मी", "छाया") })
    }

    @Test fun `8_4_43 keeps tu unretroflexed before ssa`() {
        assertEquals("सन्षष्ठः", default("सन्", "षष्ठः"))
        assertEquals("तट्टीका", default("तत्", "टीका"))
    }

    @Test fun `6_1_74 makes tuk obligatory after ang`() {
        val ang = SandhiEngine.Pada(DerivationTerm("ang", "आ", TermKind.PRATIPADIKA, upadesha = "आङ्"))
        val cha = SandhiEngine.Pada(DerivationTerm("cha", "छादयति", TermKind.PRATIPADIKA))
        assertEquals(setOf("आच्छादयति"), evaluator.evaluatePadas(listOf(ang, cha), alternatives = true).map { it.surface }.toSet())
    }

    @Test fun `8_4_61 replaces the s of stha after ud`() {
        val ud = SandhiEngine.Pada(DerivationTerm("ud", "उद्", TermKind.PRATIPADIKA, upadesha = "उद्"))
        val stha = SandhiEngine.Pada(DerivationTerm("stha", "स्थानम्", TermKind.PRATIPADIKA, upadesha = "ष्ठा"))
        val forms = evaluator.evaluatePadas(listOf(ud, stha), alternatives = true).map { it.surface }.toSet()
        assertTrue("उत्थानम्" in forms, forms.toString())
        assertTrue(forms.none { "त्स्था" in it }, forms.toString())
    }

    @Test fun `8_4_47 doubles a yar after a vowel only as an alternative`() {
        assertEquals("दध्यत्र", default("दधि", "अत्र"))
        val forms = all("दधि", "अत्र")
        // ध् doubles because the join made इ into य; अत्र's own त् was settled with that word.
        assertTrue("दद्ध्यत्र" in forms, forms.toString())
        assertTrue("दद्ध्यत्त्र" !in forms, forms.toString())
    }

    @Test fun `8_4_57 optionally nasalises a final an at a pause`() {
        val pause = evaluator.evaluate(listOf("अत्र", "दधि"), alternatives = true, finalBoundary = SpeechBoundary.AVASANA).map { it.surface }.toSet()
        assertTrue("अत्रदधि" in pause && "अत्रदधिँ" in pause, pause.toString())
        assertEquals("अत्रदधि", evaluator.evaluate(listOf("अत्र", "दधि"), finalBoundary = SpeechBoundary.AVASANA).single().surface)
    }

    @Test fun `8_4_46 doubles a yar after r only as an alternative`() {
        assertEquals("कर्तरिच", default("कर्तरि", "च"))
        // Inside a completed pada the doubling is not re-offered at a join (कर्तरि keeps its spelling) …
        assertTrue("कर्त्तरिच" !in all("कर्तरि", "च"))
        // … but a yar after र् across the junction is: पुनर् + गच्छति.
        assertTrue(all("पुनर्", "गच्छति").any { "र्ग्ग" in it }, all("पुनर्", "गच्छति").toString())
        // 8.4.49: a śar before a vowel is not doubled.
        assertTrue(all("वर्षः", "च").none { "र्ष्ष" in it })
    }
}
