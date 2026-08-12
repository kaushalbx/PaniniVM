package dev.panini.plugin

import dev.panini.execution.PvmScript
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class PvmScriptSyntaxTest {
    @Test
    fun `number game procedure is valid as a multi-sentence pvm script`() {
        val source = File("projects/sankhya-anumana-krida/mukhya.pvm").readText()

        assertTrue(PvmScript.parse(source).isNotEmpty())
    }
}
