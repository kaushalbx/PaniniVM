package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya8.pada4.VavasaneSutra
import dev.panini.shiksha.*
import kotlin.test.*

class PauseSandhiTest {
    private val engine = SandhiEngine()

    @Test fun `commentary pause examples have voiced and voiceless alternatives`() {
        for ((word, outcomes) in listOf("वाक्" to setOf("अत्रवाक्", "अत्रवाग्"),
            "त्वक्" to setOf("अत्रत्वक्", "अत्रत्वग्"), "श्वलिड्" to setOf("अत्रश्वलिट्", "अत्रश्वलिड्"),
            "त्रिष्टुभ्" to setOf("अत्रत्रिष्टुप्", "अत्रत्रिष्टुब्"))) {
            val results = engine.joinAll("अत्र", word, SpeechBoundary.AVASANA)
            assertEquals(outcomes, results.map(engine::render).toSet())
            assertTrue(results.any { r -> r.applications.any { it.sutra == "8.4.56" } })
            assertTrue(results.any { r -> r.applications.none { it.sutra == "8.4.56" } })
        }
    }

    @Test fun `continuation default does not infer a pause from end of input`() {
        val results = engine.joinAll("अत्र", "तत्")
        assertEquals(setOf("अत्रतद्"), results.map(engine::render).toSet())
        assertTrue(results.all { it.final.context.finalBoundary == SpeechBoundary.CONTINUATION })
        assertTrue(results.all { r -> r.applications.none { it.sutra == "8.4.56" } })
    }

    @Test fun `voicing followed by pause devoicing restores spelling without a cycle`() {
        val result = engine.join("आपः", "तावत्", finalBoundary = SpeechBoundary.AVASANA)
        assertEquals("आपस्तावत्", engine.render(result))
        val trace = result.applications.map { it.sutra }
        assertTrue(trace.indexOf("8.2.39") < trace.indexOf("8.4.56"))
        assertEquals(1, trace.count { it == "8.4.56" })
        val voiced = engine.join("आपः", "तावत्", DerivationConfig(optionalRulePolicy = OptionalRulePolicy.SKIP_ALL),
            SpeechBoundary.AVASANA)
        assertEquals("आपस्तावद्", engine.render(voiced))
    }

    @Test fun `only final sequence boundary is avasana`() {
        val outcome = SandhiSequenceEvaluator().evaluate(listOf("अत्र", "तत्", "अत्र"),
            finalBoundary = SpeechBoundary.AVASANA).single()
        assertEquals("अत्रतदत्र", outcome.surface)
        assertEquals(listOf(SpeechBoundary.CONTINUATION, SpeechBoundary.AVASANA),
            outcome.steps.map { it.initial.context.finalBoundary })
        assertTrue(outcome.steps.all { r -> r.applications.none { it.sutra == "8.4.56" } })
    }

    @Test fun `pause replacement keeps annotations signs and empty affix identity`() {
        val source = DerivationTerm("word", "अँद्ऽ", TermKind.PRATIPADIKA, sourceSuffixUpadeshas = setOf("क्त"))
        val empty = DerivationTerm("suffix", "", TermKind.PRATYAYA)
        val state = DerivationState(listOf(source, empty), stage = DerivationStage.PADA_FORMED,
            context = DerivationalContext(finalBoundary = SpeechBoundary.AVASANA))
        val result = VavasaneSutra.apply(state).state
        assertEquals("अँत्ऽ", result.terms.first().surface)
        assertEquals(empty, result.terms.last())
        assertEquals(source.upadesha, result.terms.first().upadesha)
        assertEquals(source.sourceSuffixUpadeshas, result.terms.first().sourceSuffixUpadeshas)
        assertEquals(listOf(OrthographicSignPlacement(OrthographicSign.AVAGRAHA, 2)), result.terms.first().orthographicSigns)
        assertEquals(1, result.substitutions.single().sourceVarnaIndex)
    }

    @Test fun `car stops follow their own class with no dental fallback`() {
        for ((source, expected) in listOf(Vyanjana.KHA to Vyanjana.KA, Vyanjana.CHA to Vyanjana.CA,
            Vyanjana.TTHA to Vyanjana.TTA, Vyanjana.THA to Vyanjana.TA, Vyanjana.PHA to Vyanjana.PA)) {
            assertEquals(expected, source.carStopCounterpart())
        }
        for (source in listOf(Vyanjana.NA, Vyanjana.MA, Vyanjana.LA, Vyanjana.SHA, Svara.A)) {
            assertNull(source.carStopCounterpart())
        }
    }

    @Test fun `rutva visarga also distinguishes a known khar follower from pause`() {
        val continuation = engine.join("अत्र", "रामस्")
        assertEquals("अत्ररामर्", engine.render(continuation))
        assertTrue(continuation.applications.none { it.sutra == "8.3.15" })
        val paused = engine.join("अत्र", "रामस्", finalBoundary = SpeechBoundary.AVASANA)
        assertEquals("अत्ररामः", engine.render(paused))
        assertTrue(paused.applications.any { it.sutra == "8.3.15" })
        val khar = engine.join("रामस्", "करोति")
        assertTrue(khar.applications.any { it.sutra == "8.3.15" })
        assertEquals(SpeechBoundary.CONTINUATION, khar.initial.context.finalBoundary)
    }
}
