package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Repeat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PvmFrequencyAstTest {
    @Test
    fun `script parsing preserves fixed repetition in the shared AST`() {
        val sentence = assertIs<PvmScriptStatement.Sentence>(
            PvmScript.parse("त्रि + कृत्वः एक + अम् मुद्र् + लोट् + सिप् ।").single(),
        )

        val repeat = assertIs<Repeat>(sentence.program)
        assertEquals(3, repeat.count)
        assertIs<Invocation>(repeat.body)
    }
}
