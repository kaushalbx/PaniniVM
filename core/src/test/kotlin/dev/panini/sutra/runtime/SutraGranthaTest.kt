package dev.panini.sutra.runtime

import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SutraGranthaTest {
    @Test
    fun `valid grantha lowers to executable sutra program`() {
        val sutra = sutra("public")
        val grantha = SutraGrantha(
            id = GranthaId("ganita"),
            sutras = listOf(sutra),
            samjnas = listOf(SamjnaDeclaration("सङ्ख्या")),
            exports = setOf(sutra.id),
        )

        val lowered = assertIs<SutraGranthaLowering.Success<TestAvastha>>(
            SutraGranthaCompiler.lower(grantha),
        )

        assertEquals("ganita", lowered.program.id)
        assertEquals(listOf(sutra), lowered.program.sutras)
    }

    @Test
    fun `grantha reports declaration boundary errors together`() {
        val existing = sutra("existing")
        val grantha = SutraGrantha(
            id = GranthaId("invalid"),
            sutras = listOf(existing),
            imports = listOf(
                GranthaImport(GranthaId("one"), "shared"),
                GranthaImport(GranthaId("two"), "shared"),
            ),
            adhikaras = listOf(
                AdhikaraDeclaration(
                    SutraId("missing-adhikara"),
                    setOf(SutraId("missing-member")),
                ),
            ),
            samjnas = listOf(
                SamjnaDeclaration("फलम्"),
                SamjnaDeclaration("फलम्"),
            ),
            exports = setOf(SutraId("missing-export")),
        )

        val invalid = assertIs<SutraGranthaLowering.Invalid>(
            SutraGranthaCompiler.lower(grantha),
        )

        assertEquals(
            setOf(
                SutraGranthaDiagnosticCode.DUPLICATE_IMPORT_ALIAS,
                SutraGranthaDiagnosticCode.DUPLICATE_SAMJNA,
                SutraGranthaDiagnosticCode.MISSING_ADHIKARA_SUTRA,
                SutraGranthaDiagnosticCode.MISSING_ADHIKARA_MEMBER,
                SutraGranthaDiagnosticCode.MISSING_EXPORT,
            ),
            invalid.diagnostics.mapTo(mutableSetOf()) { it.code },
        )
    }

    private fun sutra(id: String): RuntimeSutra<TestAvastha> = RuntimeSutra(
        id = SutraId(id),
        source = SutraSource.Program("test", id, id),
        role = SutraRole.Vidhi,
        artha = SutraArtha("test"),
        evaluator = { _, _ -> SutraNirnaya.Applicable(emptyList()) },
    )

    private data object TestAvastha : SutraAvastha
}
