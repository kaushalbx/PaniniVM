package dev.panini.execution.sutra

import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraSource
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProgramBlueprintCompilerTest {
    @Test
    fun `compiler reports missing semantic fields without creating runtime sutra`() {
        val blueprint = SutraBlueprint(
            id = SutraId("invalid"),
            source = SutraSource.Program("test", "invalid", "invalid"),
            role = SutraRole.Vidhi,
            artha = SutraArtha("kriya"),
        )

        val invalid = assertIs<ProgramBlueprintCompilation.Invalid>(
            ProgramBlueprintCompiler.compile(
                blueprint,
                ProgramBlueprintContext("प्रयोक्ता", "यन्त्रम्", "invalid"),
            ),
        )

        assertTrue(
            invalid.diagnostics.count {
                it.code == ProgramBlueprintDiagnosticCode.MISSING_FIELD
            } >= 2,
        )
    }

    @Test
    fun `compiler rejects non-kriya blueprint`() {
        val blueprint = SutraBlueprint(
            id = SutraId("grammar"),
            source = SutraSource.Program("test", "grammar", "grammar"),
            role = SutraRole.Samjna,
            artha = SutraArtha("vyakarana"),
        )

        val invalid = assertIs<ProgramBlueprintCompilation.Invalid>(
            ProgramBlueprintCompiler.compile(
                blueprint,
                ProgramBlueprintContext("प्रयोक्ता", "यन्त्रम्", "invalid"),
            ),
        )

        assertTrue(
            invalid.diagnostics.any {
                it.code == ProgramBlueprintDiagnosticCode.UNSUPPORTED_ARTHA
            },
        )
    }
}
