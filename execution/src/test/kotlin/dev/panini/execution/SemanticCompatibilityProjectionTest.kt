package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class SemanticCompatibilityProjectionTest {
    private val parser = PaniniParser()

    private fun invocation(source: String): Invocation = parser.parse(source).body as Invocation

    private fun assertTyped(code: String, operation: () -> Unit) {
        val failure = assertFailsWith<PvmSemanticException> { operation() }
        assertEquals(code, failure.diagnostic.code)
        assertNotNull(failure.diagnostic.span)
        assertNotNull(failure.diagnostic.offendingText.takeIf(String::isNotBlank))
    }

    @Test
    fun `legacy semantic projections preserve diagnostic identity and source span`() {
        assertTyped("COPULAR_PREDICATE_NUMBER_AGREEMENT") {
            CopularAgreement.validate(invocation(
                "एक + सुँ द्वि + ङसिँ न्यून + औ असँ + लट् + तिप् ।"))
        }
        assertTyped("LIST_DECLARATION_COPULA_AGREEMENT") {
            ListDeclarationLowering.expand(invocation(
                "एक + ङस् सूची + सुँ असँ + लट् + तस् ।"))
        }
        assertTyped("ORDINAL_OBJECT_DERIVED_HEAD") {
            OrdinalObjectLowering.expand(invocation(
                "सूची + ङस् प्रथम + अम् मूल्य + मतुप् + अम् ग्रहँ + श्ना + लोट् + सिप् ।"))
        }
        assertTyped("COLLECTION_DERIVED_ORDINAL_SELECTOR") {
            CollectionMemberMorphology.validate(invocation(
                "सूची + ङस् प्रथम + मतुप् + अम् उद् + हृ + लोट् + सिप् ।"))
        }
        assertTyped("COLLECTION_DERIVED_ORDINAL_SELECTOR") {
            PriorActionLowering.expand(invocation(
                "सूची + ङस् प्रथम + मतुप् + अम् उद् + हृ + ल्यप् मुद्र् + णिच् + लोट् + सिप् ।"))
        }
        assertTyped("PROCEDURE_RESULT_CARDINALITY") {
            ProcedureResultStatement.resolve(invocation(
                "फल + सुँ शून्य + सुँ मान + सुँ असँ + लट् + तिप् ।"))
        }
    }
}
