package dev.panini.plugin.run

import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import java.io.File

class PvmRunConfigurationFixtureTest : BasePlatformTestCase() {
    fun testRelativeScriptPathResolvesAgainstProjectAndValidates() {
        val scriptFile = File(requireNotNull(project.basePath), "relative.pvm")
        assertTrue(scriptFile.parentFile.mkdirs() || scriptFile.parentFile.isDirectory)
        scriptFile.writeText("पञ्चन् + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        val configuration = configuration("relative.pvm")

        configuration.checkConfiguration()

        assertEquals(scriptFile.absoluteFile, configuration.resolvedScriptFile()?.absoluteFile)
    }

    fun testMissingScriptAndWrongExtensionAreRejectedBeforeLaunch() {
        assertFailsWith<RuntimeConfigurationError> { configuration("").checkConfiguration() }
        assertFailsWith<RuntimeConfigurationError> {
            configuration("missing.pvm").checkConfiguration()
        }

        val text = myFixture.addFileToProject("not-a-script.txt", "plain text")
        val error = assertFailsWith<RuntimeConfigurationError> {
            configuration(text.virtualFile.path).checkConfiguration()
        }
        assertTrue(error.localizedMessage.orEmpty().contains(".pvm"))
    }

    private fun configuration(path: String): PvmRunConfiguration {
        val type = PvmRunConfigurationType()
        return PvmRunConfiguration(
            project,
            PvmRunConfigurationFactory(type),
            "test",
        ).also { it.scriptPath = path }
    }
}
