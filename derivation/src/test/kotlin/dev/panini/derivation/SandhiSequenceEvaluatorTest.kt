package dev.panini.derivation

import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import dev.panini.core.Linga
import dev.panini.vyakaranam.lexicon.PratipadikaEntry
import dev.panini.vyakaranam.lexicon.StandardPratipadikaLexicon

class SandhiSequenceEvaluatorTest {
    private val evaluator = SandhiSequenceEvaluator()

    @Test fun `uoh indra and shakra failures use nominal source proofs`() {
        val analyzed = SandhiSequenceEvaluator(lexicon = DerivedVisargaLexicon.build(
            StandardPratipadikaLexicon.declaredPratipadikas()))
        for ((word, expected) in listOf("इन्द्रः" to "इन्द्रोवा", "शक्रः" to "शक्रोवा")) {
            assertEquals(word + "वा", evaluator.evaluate(listOf(word, "वा")).single().surface)
            val outcomes = analyzed.evaluate(listOf(word, "वा"))
            assertTrue(outcomes.isNotEmpty())
            assertEquals(setOf(expected), outcomes.map { it.surface }.toSet())
            for (outcome in outcomes) {
                val proof = assertNotNull(outcome.inputs.first().sourceDerivation)
                assertEquals(word, proof.final.surface)
                assertEquals(VisargaSource.ESTABLISHED_RU, outcome.inputs.first().visargaSource)
                assertTrue(proof.applications.any { it.sutra == "8.2.66" })
                assertTrue(outcome.steps.flatMap { it.applications }.any { it.sutra == "6.1.114" })
            }
        }
    }

    @Test fun `uoh rama hanuman ca uses attested lexical proof and prescribed nasal alternatives`() {
        val index = DerivedVisargaLexicon.build(StandardPratipadikaLexicon.declaredPratipadikas())
        val outcomes = SandhiSequenceEvaluator(lexicon = index)
            .evaluate(listOf("रामः", "हनुमान्", "च"), alternatives = true)
        val selected = outcomes.firstOrNull { it.surface.filterNot(Char::isWhitespace) == "रामोहनुमांश्च" }
        assertNotNull(selected)
        val proof = assertNotNull(selected.inputs.first().sourceDerivation)
        assertEquals("रामः", proof.final.surface)
        assertEquals(VisargaSource.ESTABLISHED_RU, selected.inputs.first().visargaSource)
        val rules = selected.steps.flatMap { it.applications }.map { it.sutra }
        assertTrue("8.3.7" in rules)
        assertTrue("8.3.4" in rules)
        assertTrue(selected.steps.all { it.final.stage == DerivationStage.FINAL })
    }

    @Test fun `commentary inputs remain errors with original spelling and position`() {
        for ((words, invalidIndex) in listOf(
            listOf("लाक्षारोचनात्", "(शकलकर्दमात्)", "ठक्") to 1,
            listOf("सा", "अस्मिन्", "पौर्णमासि", "इति", "(संज्ञायाम्)") to 4,
            listOf("पूजनात्", "पूजितम्", "अनुदात्तम्", "(काष्ठादिभ्यः)") to 3,
            listOf("न", "आमन्त्रिते", "समानाधिकरणे", "(सामान्यवचनम्)") to 3,
        )) {
            val failure = assertFailsWith<SandhiSequenceEvaluator.InvalidPadaInput> { evaluator.evaluate(words) }
            assertEquals(invalidIndex, failure.wordIndex)
            assertEquals(words[invalidIndex], failure.word)
            assertTrue(failure.cause is IllegalStateException)
        }
        val branchLimit = assertFailsWith<IllegalStateException> {
            evaluator.evaluate(listOf("माले", "इति"), alternatives = true, maxOutcomes = 1)
        }
        assertTrue(branchLimit.message.orEmpty().contains("limit exceeded"))
        val malformed = assertFailsWith<SandhiSequenceEvaluator.InvalidPadaInput> {
            evaluator.evaluate(listOf("ँ", "इति"))
        }
        assertTrue(malformed.cause is IllegalArgumentException)
    }

    @Test fun `two word default matches ordinary engine rendering`() {
        val engine = SandhiEngine()
        for ((left, right) in listOf("गो" to "इन्द्रः", "वाक्" to "ईशः", "माले" to "इति")) {
            assertEquals(engine.render(engine.join(left, right)), evaluator.evaluate(listOf(left, right)).single().surface)
        }
    }

    @Test fun `hiatus survives a third pada without becoming a phonological space`() {
        val result = evaluator.evaluatePadas(listOf(
            SandhiEngine.Pada(DerivationTerm("male", "माले", TermKind.PRATIPADIKA), samjnas = setOf(Samjna.PRAGRHYA)),
            SandhiEngine.Pada(DerivationTerm("iti", "इति", TermKind.PRATIPADIKA)),
            SandhiEngine.Pada(DerivationTerm("api", "अपि", TermKind.PRATIPADIKA)),
        )).single()
        assertEquals("माले इत्यपि", result.surface)
        assertEquals("male", result.padas.first().term.id)
        assertEquals("माले", result.padas.first().term.surface)
        assertTrue(Samjna.PRAGRHYA in result.padas.first().samjnas)
        assertEquals(2, result.steps.size)
        assertTrue(result.steps.last().initial.terms.none { it.id == "male" })
    }

    @Test fun `optional outcomes are reported separately from default`() {
        val alternatives = evaluator.evaluate(listOf("माले", "इति", "च"), alternatives = true)
        assertEquals(setOf("मालयितिच", "माल इतिच"), alternatives.map { it.surface }.toSet())
        assertTrue(evaluator.evaluate(listOf("माले", "इति", "च")).single().surface in alternatives.map { it.surface })
    }

    @Test fun `alternative limit fails explicitly and does not truncate`() {
        assertFailsWith<IllegalStateException> {
            evaluator.evaluate(listOf("माले", "इति"), alternatives = true, maxOutcomes = 1)
        }
    }

    @Test fun `declared lexical mode supplies actual rutva proof without changing baseline`() {
        val index = DerivedVisargaLexicon.build(StandardPratipadikaLexicon.declaredPratipadikas())
        val analyzed = SandhiSequenceEvaluator(lexicon = index)
        val words = listOf("प्रथमः", "अङ्कः")
        assertEquals("प्रथमःअङ्कः", evaluator.evaluate(words).single().surface)
        val outcomes = analyzed.evaluate(words)
        assertTrue(outcomes.isNotEmpty())
        assertEquals(setOf("प्रथमोऽङ्कः"), outcomes.map { it.surface }.toSet())
        for (outcome in outcomes) {
            val proof = assertNotNull(outcome.inputs.first().sourceDerivation)
            assertEquals("प्रथमः", proof.final.surface)
            assertTrue(proof.applications.any { it.sutra == "8.2.66" })
            assertTrue(outcome.steps.all { it.final.stage == DerivationStage.FINAL })
        }
        val unknown = listOf("अघोषितः", "अस्ति")
        assertEquals(evaluator.evaluate(unknown).map { it.surface }, analyzed.evaluate(unknown).map { it.surface })
    }

    @Test fun `case ambiguity retains all nominal proofs through later boundaries`() {
        val index = DerivedVisargaLexicon.build(listOf(PratipadikaEntry("पितृ", setOf(Linga.PUMS))))
        val analyzed = SandhiSequenceEvaluator(lexicon = index)
        val outcomes = analyzed.evaluate(listOf("पितुः", "अस्ति", "च"))
        assertEquals(index.candidates("पितुः").size, outcomes.size)
        assertEquals(setOf("पितुरस्तिच"), outcomes.map { it.surface }.toSet())
        assertTrue(outcomes.map { it.inputs.first().rupa.vibhakti }.toSet().size >= 2)
        assertTrue(outcomes.all { it.inputs.size == 3 && it.inputs.first().sourceDerivation != null })
        assertTrue(outcomes.all { outcome -> outcome.steps.none { result -> result.applications.any { it.sutra == "8.2.66" } } })
        assertFailsWith<IllegalStateException> { analyzed.evaluate(listOf("पितुः", "अस्ति"), maxOutcomes = 1) }
        val rightClosed = analyzed.evaluate(listOf("अत्र", "पितुः"))
        assertEquals(setOf("अत्रपितुः"), rightClosed.map { it.surface }.toSet())
        val later = analyzed.evaluate(listOf("अत्र", "पितुः", "अस्ति"))
        assertEquals(setOf("अत्रपितुरस्ति"), later.map { it.surface }.toSet())
        assertTrue(later.all { it.steps.last().initial.terms.first().completedPadaVisargaInput?.source == VisargaSource.R })
    }

    @Test fun `coalescence transfers an explicitly established ending source by token origin`() {
        val result = evaluator.evaluatePadas(listOf(
            SandhiEngine.Pada(DerivationTerm("first", "अत्र", TermKind.PRATIPADIKA)),
            SandhiEngine.Pada(DerivationTerm("second", "अन्तः", TermKind.PRATIPADIKA), visargaSource = VisargaSource.R),
            SandhiEngine.Pada(DerivationTerm("third", "अस्ति", TermKind.PRATIPADIKA)),
        )).single()
        assertEquals("अत्रान्तरस्ति", result.surface)
        assertEquals(VisargaSource.R, result.steps.last().initial.terms.first().completedPadaVisargaInput?.source)
        assertTrue(result.steps.last().applications.none { it.sutra == "8.2.66" })
    }

    @Test fun `forward nominal evidence preserves dual and plural readings without inventing visarga sources`() {
        val index = DerivedVisargaLexicon.build(listOf(PratipadikaEntry("इदम्", Linga.entries.toSet())))
        assertTrue(index.failures.isEmpty(), index.failures.toString())
        assertTrue(index.unresolvedSources.isEmpty(), index.unresolvedSources.toString())
        assertTrue(index.candidates("इमे").isEmpty())
        val candidates = index.nominalCandidates("इमे")
        assertTrue(candidates.any { it.request.vacana == dev.panini.core.Vacana.DVIVACANA })
        assertTrue(candidates.any { it.request.vacana == dev.panini.core.Vacana.BAHUVACANA })
        assertTrue(candidates.all { it.pada.visargaSource == null && it.pada.sourceDerivation === it.derivation })
        val outcomes = SandhiSequenceEvaluator(lexicon = index).evaluate(listOf("इमे", "अत्र", "च"))
        assertEquals(candidates.size, outcomes.size)
        assertEquals(setOf("इमे अत्रच", "इमेऽत्रच"), outcomes.map { it.surface }.toSet())
        for (outcome in outcomes) {
            val pada = outcome.inputs.first()
            assertNotNull(pada.sourceDerivation)
            assertNull(pada.visargaSource)
            assertEquals(pada.rupa.vacana == dev.panini.core.Vacana.DVIVACANA, Samjna.PRAGRHYA in pada.samjnas)
        }
        assertFailsWith<IllegalStateException> {
            SandhiSequenceEvaluator(lexicon = index).evaluate(listOf("इमे", "अत्र"), maxOutcomes = 1)
        }
    }
}
