package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.PaniniVM
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.OutputKind
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TatahResultPipelineTest {
    private val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")

    @Test
    fun `tatah supplies the preceding result as a missing karman`() {
        val bound = bind(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् " +
                "ततः मुद्र् + णिच् + लोट् + सिप् ।",
        )

        val source = bound.ukti.invocations.first()
        val target = bound.ukti.invocations.last()
        val piped = assertIs<ExecutionExpression.Reference>(target.bindings[Karaka.KARMAN])

        assertEquals(source.id, piped.name)
    }

    @Test
    fun `an explicit karman takes precedence over tatah piping`() {
        val bound = bind(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् " +
                "ततः संदेश + अम् मुद्र् + णिच् + लोट् + सिप् ।",
        )

        assertIs<ExecutionExpression.Pada>(bound.ukti.invocations.last().bindings[Karaka.KARMAN])
    }

    @Test
    fun `tatah preserves the typed result through execution`() {
        val result = PaniniVM().eval(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् " +
                "ततः मुद्र् + णिच् + लोट् + सिप् ।",
        )

        assertEquals("त्रीणि", assertIs<ExecutionResult.Success>(result).value)
    }

    @Test
    fun `tatah joins the result of either conditional branch`() {
        val result = PaniniVM().eval(
            "यदि द्वि + अम् एक + अम् च विद् + लोट् + सिप् " +
                "तर्हि लघु अन्यथा गुरु " +
                "ततः मुद्र् + णिच् + लोट् + सिप् ।",
        )

        val success = assertIs<ExecutionResult.Success>(result)
        assertEquals("लघु", success.value)
        assertEquals(OutputKind.CONSOLE, success.outputKind)
    }

    private fun bind(text: String): ExecutionBindingResult.Bound {
        DhatuPathaRegistration.ensureRegistered()
        return assertIs(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", text),
                conversation,
            ),
        )
    }
}
