package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.SubantaPada
import dev.panini.vyakaranam.ast.Ukti
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NaturalSemanticNormalizerTest {
    private val parser = PaniniParser()

    @Test
    fun `normalizes existential truth from morphology`() {
        val operation = NaturalSemanticNormalizer.normalize(
            invocation("अवस्था + सुँ न असँ + लट् + तिप् ।"),
        )

        assertEquals(
            NaturalSemanticNormalizer.Operation.TruthTest("अवस्था", negated = true),
            operation,
        )
    }

    @Test
    fun `normalizes bhu existential truth from morphology`() {
        val operation = NaturalSemanticNormalizer.normalize(
            invocation("अवस्था + सुँ न भू + लट् + तिप् ।"),
        )

        assertEquals(
            NaturalSemanticNormalizer.Operation.TruthTest("अवस्था", negated = true),
            operation,
        )
    }

    @Test
    fun `normalizes a named reported outcome condition`() {
        assertEquals(
            NaturalSemanticNormalizer.Operation.ReportedOutcomeTest("विजय", negated = true),
            NaturalSemanticNormalizer.normalize(
                invocation("विजय + सुँ न भू + लट् + तिप् ।"),
            ),
        )
    }

    @Test
    fun `normalizes exclusion absolutive without inspecting rendered text`() {
        val operation = assertIs<NaturalSemanticNormalizer.Operation.RangeChoice>(
            NaturalSemanticNormalizer.normalize(
                invocation("क्रम + अम् वृज् + णिच् + क्त्वा चिञ् + श्नु + लोट् + सिप् ।"),
            ),
        )

        assertEquals("क्रम", operation.exclusionName)
        assertEquals(true, operation.usesExclusionAbsolutive)
    }

    @Test
    fun `ablative collection alone does not imply exclusion`() {
        val operation = assertIs<NaturalSemanticNormalizer.Operation.RangeChoice>(
            NaturalSemanticNormalizer.normalize(
                invocation("क्रम + ङसिँ सङ्ख्या + अम् चिञ् + श्नु + लोट् + सिप् ।"),
            ),
        )

        assertEquals(null, operation.exclusionName)
        assertFalse(operation.usesExclusionAbsolutive)
    }

    @Test
    fun `recognizes only the explicit phala result anaphor`() {
        assertTrue(NaturalSemanticNormalizer.isPriorResult(subanta("फल + अम्")))
        assertFalse(NaturalSemanticNormalizer.isPriorResult(subanta("फलित + अम्")))
    }

    @Test
    fun `normalizes argumentless display as discourse-result anaphora`() {
        assertEquals(
            NaturalSemanticNormalizer.Operation.DisplayPriorResult,
            NaturalSemanticNormalizer.normalize(invocation("मुद्र् + लोट् + सिप् ।")),
        )
    }

    @Test
    fun `normalizes ordinal procedure arithmetic into semantic operands`() {
        assertEquals(
            NaturalSemanticNormalizer.Operation.ProcedureParameterArithmetic(
                NaturalSemanticNormalizer.ArithmeticKind.ADD,
                listOf(
                    NaturalSemanticNormalizer.ProcedureOperand.Parameter(1),
                    NaturalSemanticNormalizer.ProcedureOperand.Parameter(2),
                ),
            ),
            NaturalSemanticNormalizer.normalize(
                invocation("प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ।"),
            ),
        )
    }

    @Test
    fun `ordinal semantics use typed purana or lexical AST identity`() {
        val ordinal = invocation(
            "प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ।",
        ).vakya.padas.first()

        assertEquals(1L, PuranaPratyayaResolver.ordinalValue(ordinal))
    }

    @Test
    fun `normalizes collection parameter summation by lexical identity`() {
        assertEquals(
            NaturalSemanticNormalizer.Operation.CollectionParameterSum,
            NaturalSemanticNormalizer.normalize(
                invocation("समवाय + अम् युज् + णिच् + लोट् + सिप् ।"),
            ),
        )
    }

    @Test
    fun `collection parameter compatibility slot retains derivation and object case`() {
        for (phrase in listOf("समवाय + मतुप् + अम्", "समवाय + तरप् + अम्",
            "समवाय + टा", "समवाय + ङस्", "समवाय + शस्",
            "समवाय + अम् एक + अम् च")) {
            kotlin.test.assertNotEquals(NaturalSemanticNormalizer.Operation.CollectionParameterSum,
                NaturalSemanticNormalizer.normalize(invocation("$phrase युज् + णिच् + लोट् + सिप् ।")), phrase)
        }
    }

    private fun invocation(source: String): Invocation =
        assertIs(assertIs<Ukti>(parser.parse(source)).body)

    private fun subanta(source: String): SubantaPada =
        assertIs(invocation("$source असँ + लट् + तिप् ।").vakya.padas.first())
}
