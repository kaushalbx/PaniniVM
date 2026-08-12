package dev.panini.plugin.run

import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmCliProcessLauncherTest {
    @Test
    fun `command keeps Windows paths with spaces as individual arguments`() {
        val launcher = PvmCliProcessLauncher(
            javaHome = "C:\\Program Files\\Java\\jdk",
            classpathProvider = { "C:\\Panini VM\\plugin.jar;C:\\Panini VM\\lib.jar" },
        )
        val script = File("C:\\Sanskrit Programs\\interactive addition.pvm")

        assertEquals(
            listOf(
                File("C:\\Program Files\\Java\\jdk", "bin/java").absolutePath,
                "-Dfile.encoding=UTF-8",
                "-cp",
                "C:\\Panini VM\\plugin.jar;C:\\Panini VM\\lib.jar",
                "dev.panini.MainKt",
                "--eval",
                script.absolutePath,
            ),
            launcher.command(script),
        )
    }

    @Test
    fun `isolated process accepts interactive input and returns CLI exit code`() {
        val launcher = PvmCliProcessLauncher(
            classpathProvider = { requireNotNull(System.getProperty("panini.idea.test.classpath")) },
        )
        val process = launcher.start(
            File("cli/examples/interactive_addition.pvm").absoluteFile,
            File(System.getProperty("user.dir")),
        )

        try {
            process.outputStream.use { it.write("10\n20\n".toByteArray(Charsets.UTF_8)) }
            assertTrue(process.waitFor(30, TimeUnit.SECONDS))
            val output = process.inputStream.readBytes().toString(Charsets.UTF_8)

            assertEquals(0, process.exitValue())
            assertTrue(output.contains("Enter value for प्रथम (number):"))
            assertTrue(output.contains("त्रिंशत्"))
        } finally {
            if (process.isAlive) process.destroyForcibly()
        }
    }
}
