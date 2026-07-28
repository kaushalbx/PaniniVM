package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class PvmScriptTest {
    @Test
    fun `parses a segmented while body without control shortcuts`() {
        val statements = PvmScript.parse(
            """
            एक + अम् गणना + ङे दा + लोट् + सिप् ।
            यावत् गणना + अम् शून्य + अम् च विद् + णिच् + लोट् + सिप्
            तावत् गणना + अम् एक + औट् च वि + युज् + णिच् + लोट् + सिप्
            ततः फल + अम् गणना + ङे दा + लोट् + सिप् ।
            वृत् + यङ् + लोट् + थास् ।
            """.trimIndent(),
        )

        assertEquals(2, statements.size)
        val loop = assertIs<PvmScriptStatement.While>(statements[1])
        assertEquals(2, loop.body.size)
        assertEquals("गणना + अम् शून्य + अम् च विद् + णिच् + लोट् + सिप् ।", loop.condition)
        assertEquals("वृत् + यङ् + लोट् + थास् ।", loop.invocation.text)
    }

    @Test
    fun `rejects a loop without a tavat body`() {
        assertFailsWith<IllegalArgumentException> {
            PvmScript.parse("यावत् गणना + अम् शून्य + अम् च विद् + णिच् + लोट् + सिप् ।")
        }
    }

    @Test
    fun `rejects a segmented loop without vrt plus yang invocation`() {
        assertFailsWith<IllegalArgumentException> {
            PvmScript.parse(
                """
                यावत् गणना + अम् शून्य + अम् च विद् + णिच् + लोट् + सिप्
                तावत् गणना + अम् एक + औट् च वि + युज् + णिच् + लोट् + सिप् ।
                """.trimIndent(),
            )
        }
    }
}
