package dev.panini.execution

import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class TypedParticipantAnalysisTest {
    private fun padas(phrase: String): List<Pada> = PaniniParser()
        .parse("$phrase मुद्र् + णिच् + लोट् + सिप् ।").grammaticalVakyas().single().padas

    @Test fun `type nouns distinguish first number word and list`() {
        val phrases = listOf(
            "प्रथम + टाप् + अम् सङ्ख्या + अम्" to PrakriyaValueType.SANKHYA,
            "प्रथम + अम् शब्द + अम्" to PrakriyaValueType.SHABDA,
            "प्रथम + टाप् + अम् सूची + अम्" to PrakriyaValueType.SUCHI)
        for ((phrase, type) in phrases) {
            val analysis = TypedParticipantAnalysis.analyze(padas(phrase)).single()
            val reference = assertNotNull(analysis.unique, "$phrase: $analysis")
            assertEquals(TypedParticipantSlot(type, 1), reference.slot)
            assertTrue(reference.knownGenderAgreement, phrase)
            assertNull(analysis.diagnostic)
        }
    }

    @Test fun `agreement not word order selects the type head`() {
        val words = padas("प्रथम + टाप् + अम् सङ्ख्या + अम् शब्द + टा")
        for (scope in listOf(words, words.reversed())) {
            val reference = assertNotNull(TypedParticipantAnalysis.analyze(scope).single().unique)
            assertEquals(PrakriyaValueType.SANKHYA, reference.slot.type)
            assertEquals("सङ्ख्या", (scope[reference.headIndex] as SubantaPada).pratipadika.sourceText)
        }
    }

    @Test fun `gender case and number mismatches do not establish inputs`() {
        for (phrase in listOf("प्रथम + अम् सङ्ख्या + अम्",
            "प्रथम + टाप् + सुँ सङ्ख्या + अम्", "प्रथम + टाप् + अम् सङ्ख्या + शस्",
            "द्वि + तीय + अम् सङ्ख्या + अम्")) {
            val result = TypedParticipantAnalysis.analyze(padas(phrase)).single()
            assertNull(result.unique, phrase)
            assertEquals(TypedParticipantDiagnostic.NO_AGREEING_TYPE_HEAD, result.diagnostic, phrase)
        }
    }

    @Test fun `ambiguity retains distinct equal looking occurrences`() {
        val result = TypedParticipantAnalysis.analyze(padas("प्रथम + टाप् + अम् सङ्ख्या + अम् सङ्ख्या + अम्")).single()
        assertNull(result.unique)
        assertEquals(TypedParticipantDiagnostic.AMBIGUOUS_TYPE_HEADS, result.diagnostic)
        assertEquals(listOf(1, 2), result.candidates.map { it.headIndex })
    }

    @Test fun `established collection member roles cannot become procedure participants`() {
        val scope = padas("सूची + ङस् प्रथम + टाप् + अम् सङ्ख्या + अम्")
        assertTrue(TypedParticipantAnalysis.analyze(scope, protectedIndices = setOf(1, 2)).isEmpty())
    }

    @Test fun `derived type nouns do not masquerade as input types`() {
        assertTrue(TypedParticipantAnalysis.analyze(padas("प्रथम + अम् शब्द + त्व + अम्")).isEmpty())
    }

    @Test fun `productive ordinals preserve a type relative position`() {
        val result = TypedParticipantAnalysis.analyze(padas("द्वि + तीय + अम् शब्द + अम्")).single()
        assertEquals(TypedParticipantSlot(PrakriyaValueType.SHABDA, 2), assertNotNull(result.unique).slot)
    }

    @Test fun `productive feminine ordinal agrees without changing numeric meaning`() {
        for ((stem, number, surface) in listOf(Triple("प्रथ् + अमच्", 1L, "प्रथमाम्"),
            Triple("द्वि + तीय", 2L, "द्वितीयाम्"), Triple("त्रि + तीय", 3L, "तृतीयाम्"))) {
            val scope = padas("$stem + टाप् + अम् सङ्ख्या + अम्")
            val candidate = assertNotNull(TypedParticipantAnalysis.analyze(scope).single().unique)
            assertEquals(TypedParticipantSlot(PrakriyaValueType.SANKHYA, number), candidate.slot)
            assertTrue(candidate.knownGenderAgreement)
            assertEquals(surface, PvmUktiSadhaka().sadhayaSankhyaPurana(scope.first() as SankhyaPuranaPada))
        }
    }

    @Test fun `a recognized feminine affix still needs a licensing derivation`() {
        val scope = padas("द्वि + तीय + ङीष् + अम् सङ्ख्या + अम्")
        val failure = assertFailsWith<IllegalArgumentException> { TypedParticipantAnalysis.analyze(scope) }
        assertTrue(failure.message.orEmpty().contains("licenses"), failure.message)
    }

    @Test fun `numeric meaning cannot silently replace the source ordinal affix`() {
        for (source in listOf("द्वि + तम + अम् शब्द + अम्",
            "एक + अमच् + अम् शब्द + अम्",
            "द्वि + तम + टाप् + अम् सङ्ख्या + अम्",
            "एक + अमच् + टाप् + अम् सङ्ख्या + अम्")) {
            val failure = assertFailsWith<IllegalArgumentException> { TypedParticipantAnalysis.analyze(padas(source)) }
            assertTrue(failure.message.orEmpty().contains("ordinal derivation"), failure.message)
        }
    }

    @Test fun `one type head cannot erase ordinal occurrence ambiguity`() {
        val scope = padas("प्रथम + टाप् + अम् द्वितीय + टाप् + अम् सङ्ख्या + अम्")
        val analysis = TypedParticipantAnalysis.analyze(scope)
        assertEquals(listOf(1L, 2L), analysis.map { it.candidates.single().slot.ordinal })
        assertTrue(analysis.all { it.unique == null && it.diagnostic == TypedParticipantDiagnostic.SHARED_TYPE_HEAD })
        assertEquals(listOf(2, 2), analysis.map { it.candidates.single().headIndex })
    }
}
