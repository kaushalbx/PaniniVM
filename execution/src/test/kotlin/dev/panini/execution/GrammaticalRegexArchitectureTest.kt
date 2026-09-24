package dev.panini.execution

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class GrammaticalRegexArchitectureTest {
    @Test
    fun `grammatical interpretation remains AST based`() {
        val workingDirectory = File(System.getProperty("user.dir"))
        val repository = if (workingDirectory.name == "execution") workingDirectory.parentFile else workingDirectory
        val module = File(repository, "execution")
        val grammarSensitiveFiles = listOf(
            "PrakriyaSignature.kt",
            "DirectResultAssignment.kt",
            "PrakriyaScriptValidator.kt",
            "ItiDeclaration.kt",
        ).map { File(module, "src/main/kotlin/dev/panini/execution/$it") } +
            File(repository, "compiler/src/main/kotlin/dev/panini/compiler/CompilerFrontend.kt")

        grammarSensitiveFiles.forEach { file ->
            val source = file.readText()
            assertFalse("Regex(" in source || ".toRegex(" in source, file.path)
        }
    }
}
