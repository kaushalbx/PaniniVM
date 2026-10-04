package dev.panini.execution.binding

import dev.panini.vyakaranam.ast.TingantaPada
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DhatuCacheGanaResolutionTest {
    @Test
    fun `explicit divadi vikarana selects executable lexical entry`() {
        val tinganta = PaniniParser()
            .parse("अक्ष + अम् दिव् + श्यन् + लोट् + सिप् ।")
            .grammaticalVakyas()
            .single()
            .padas
            .filterIsInstance<TingantaPada>()
            .single()

        assertEquals("04.9901", DhatuCache.resolve(tinganta)?.id)
        assertIs<dev.panini.execution.ExecutionResult.Success>(
            dev.panini.execution.PaniniVM().eval("अक्ष + अम् दिव् + श्यन् + लोट् + सिप् ।"),
        )
    }
}
