package dev.panini.execution.binding

import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.Polarity
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.VakyaPrayojana
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ExecutionQualificationIntentTest {

    private val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")

    @Test
    fun `derives prayer intent from courtesy qualification`() {
        val bound = bind("कृपया एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।")

        assertEquals(VakyaPrayojana.PRARTHANA, bound.ukti.prayojana)
        assertEquals(Polarity.POSITIVE, bound.ukti.polarity)
    }

    @Test
    fun `derives prohibition intent from negation qualification`() {
        val bound = bind("मा एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।")

        assertEquals(VakyaPrayojana.NISHEDHA, bound.ukti.prayojana)
        assertEquals(Polarity.NEGATIVE, bound.ukti.polarity)
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
