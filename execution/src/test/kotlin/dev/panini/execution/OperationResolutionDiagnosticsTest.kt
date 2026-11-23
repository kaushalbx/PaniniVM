package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/** A sentence that no operation of its dhātu accepts fails with a code and the clause it came from. */
class OperationResolutionDiagnosticsTest {
    @Test fun `participants no operation signature accepts are a located NO_MATCHING_OPERATION`() {
        val sentence = "सत्य + अम् एक + अम् च युज् + णिच् + लोट् + सिप् ।"
        val source = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ।\n$sentence"
        val failure = PaniniVM().evalScript(source).last() as ExecutionResult.Failure
        val diagnostic = assertNotNull(failure.diagnostic, failure.toString())
        assertEquals(OperationResolutionCode.NO_MATCHING_OPERATION, diagnostic.code)
        val span = assertNotNull(diagnostic.span)
        assertEquals(source.indexOf(sentence), span.start)
    }
}
