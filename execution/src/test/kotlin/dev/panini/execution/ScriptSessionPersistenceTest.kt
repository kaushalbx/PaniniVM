package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class ScriptSessionPersistenceTest {
    @Test
    fun `anonymous script discourse remains in memory instead of rewriting persistent history`() {
        val directory = kotlin.io.path.createTempDirectory("pvm-transient-script-").toFile()
        try {
            val vm = PaniniVM(storageDir = directory)

            vm.evalScript(
                """
                एक + अम् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।
                द्वि + अम् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।
                """.trimIndent(),
            )

            assertEquals(emptyList(), vm.listSessions())
        } finally {
            directory.deleteRecursively()
        }
    }

    @Test
    fun `explicitly named script discourse remains persistent`() {
        val directory = kotlin.io.path.createTempDirectory("pvm-persistent-script-").toFile()
        try {
            val vm = PaniniVM(storageDir = directory)

            vm.evalScript(
                "एक + अम् सङ्ख्या + ङि स्था + णिच् + लोट् + सिप् ।",
                sessionKey = "named-script",
            )

            assertEquals(listOf("named-script"), vm.listSessions())
        } finally {
            directory.deleteRecursively()
        }
    }
}
