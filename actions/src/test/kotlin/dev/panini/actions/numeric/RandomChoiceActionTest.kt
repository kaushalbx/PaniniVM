package dev.panini.actions.numeric

import dev.panini.core.Karaka
import dev.panini.execution.ExecutionContext
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ExecutionResult
import dev.panini.execution.SanskritValue
import dev.panini.execution.SankhyaResultRenderer
import dev.panini.execution.op
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RandomChoiceActionTest {
    @Test
    fun `active range excludes supplied list values`() {
        val excluded = (1L..47L).map { SanskritValue.Sankhya(it, it.toString()) }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada(
                    prakriti = "क्रम",
                    value = SanskritValue.Suchi(excluded),
                ),
            ),
            variables = mapOf(
                "सीमा" to SanskritValue.Range(
                    SanskritValue.Sankhya(1, "एक"),
                    SanskritValue.Sankhya(48, "अष्टाचत्वारिंशत्"),
                ),
            ),
            sankhyaRenderer = SankhyaResultRenderer(Long::toString),
        )
        val operation = RandomChoiceAction.op { returns(Samjna.SANKHYA) }

        val result = assertIs<ExecutionResult.Success>(RandomChoiceAction.execute(context, operation))

        assertEquals(48L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
    }
}
