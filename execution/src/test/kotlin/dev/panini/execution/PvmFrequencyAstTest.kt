package dev.panini.execution

import dev.panini.vyakaranam.ast.Invocation
import dev.panini.vyakaranam.ast.Repeat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PvmFrequencyAstTest {
    @Test
    fun `leading frequency scopes over its connected event chain`() {
        val sentence = assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(
            "त्रि + कृत्वसुच् एक + अम् मुद्र् + णिच् + लोट् + सिप् ततः द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।"
        ).single())
        val repeat = assertIs<Repeat>(sentence.program)
        assertEquals(3, repeat.count)
        assertEquals(2, assertIs<dev.panini.vyakaranam.ast.Sequence>(repeat.body).statements.size)
    }
    @Test
    fun `nested frequency remains inside distributive clause`() {
        val sentence = assertIs<PvmScriptStatement.Sentence>(PvmScript.parse(
            "सूची + ङस् सदस्य + अम् प्रति द्वि + कृत्वसुच् सदस्य + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        ).single())
        val loop = assertIs<dev.panini.vyakaranam.ast.ForEach>(sentence.program)
        assertEquals(2, assertIs<Repeat>(loop.body).count)
    }
    @Test
    fun `script parsing preserves fixed repetition in the shared AST`() {
        val sentence = assertIs<PvmScriptStatement.Sentence>(
            PvmScript.parse("त्रि + कृत्वसुच् एक + अम् मुद्र् + लोट् + सिप् ।").single(),
        )

        val repeat = assertIs<Repeat>(sentence.program)
        assertEquals(3, repeat.count)
        assertIs<Invocation>(repeat.body)
    }
}
