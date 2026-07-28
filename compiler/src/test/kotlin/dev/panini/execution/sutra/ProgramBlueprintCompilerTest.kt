package dev.panini.execution.sutra

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.ActionDependency
import dev.panini.execution.DhatuInvocation
import dev.panini.execution.ExecutableUkti
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.VakyaPrayojana
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.SutraArtha
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprint
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraNirnaya
import dev.panini.sutra.runtime.SutraSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class ProgramBlueprintCompilerTest {
    @BeforeTest
    fun initializeDhatus() {
        DhatuPathaRegistration.ensureRegistered()
    }

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

    @Test
    fun `compiler rejects malformed ambiguity evidence`() {
        val blueprint = SutraBlueprint(
            id = SutraId("invalid-evidence"),
            source = SutraSource.Program("test", "invalid-evidence", "invalid"),
            role = SutraRole.Vidhi,
            artha = SutraArtha(
                "kriya",
                mapOf(
                    "dhatu" to SutraArthaValue.Text("missing"),
                    "karakas" to SutraArthaValue.Record(emptyMap()),
                    "ambiguousKarakas" to SutraArthaValue.Text("not-a-sequence"),
                    "karakaEvidence" to SutraArthaValue.Sequence(
                        listOf(SutraArthaValue.Symbol("not-text")),
                    ),
                ),
            ),
        )

        val invalid = assertIs<ProgramBlueprintCompilation.Invalid>(
            ProgramBlueprintCompiler.compile(
                blueprint,
                ProgramBlueprintContext("प्रयोक्ता", "यन्त्रम्", "invalid"),
            ),
        )

        assertTrue(
            invalid.diagnostics.count {
                it.code == ProgramBlueprintDiagnosticCode.INVALID_FIELD
            } >= 2,
        )
    }

    @Test
    fun `compiler reconstructs ambiguity and rule evidence`() {
        val ambiguity = AmbiguousKarakaBinding(
            ExecutionExpression.Pada("फलम्"),
            linkedSetOf(Karaka.KARTR, Karaka.KARMAN),
        )
        val blueprint = SutraBlueprint(
            id = SutraId("evidenced"),
            source = SutraSource.Program("test", "evidenced", "युज्"),
            role = SutraRole.Vidhi,
            artha = SutraArtha(
                "kriya",
                mapOf(
                    "dhatu" to SutraArthaValue.Text("युज्"),
                    "upadesha" to SutraArthaValue.Text("युजिँर्"),
                    "karakas" to SutraArthaValue.Record(emptyMap()),
                    "metadata" to SutraArthaValue.Record(
                        mapOf("origin" to SutraArthaValue.Text("व्याकरणम्")),
                    ),
                    "ambiguousKarakas" to SutraArthaValue.Sequence(
                        listOf(ProgramSutraArthaCodec.encodeAmbiguousBinding(ambiguity)),
                    ),
                    "karakaEvidence" to SutraArthaValue.Sequence(
                        listOf(SutraArthaValue.Text("1.4.49 कर्तुरीप्सिततमं कर्म")),
                    ),
                ),
            ),
        )

        val compiled = assertIs<ProgramBlueprintCompilation.Success>(
            ProgramBlueprintCompiler.compile(
                blueprint,
                ProgramBlueprintContext("प्रयोक्ता", "यन्त्रम्", "युज्"),
            ),
        )
        val decision = assertIs<SutraNirnaya.Applicable<ProgramAvastha>>(
            compiled.sutra.evaluator.evaluate(
                compiled.sutra,
                ProgramAvastha(dev.panini.execution.ValueEnvironment()),
            ),
        )
        val invocation = assertIs<InvokeDhatuEffect>(decision.effects.single()).invocation

        assertEquals(mapOf("origin" to "व्याकरणम्"), invocation.metadata)
        assertEquals(listOf(ambiguity), invocation.ambiguousBindings)
        assertEquals(listOf("1.4.49 कर्तुरीप्सिततमं कर्म"), invocation.karakaTrace)
    }

    @Test
    fun `blueprint grantha compilation returns diagnostics without throwing`() {
        val invocation = DhatuInvocation(
            id = "self-dependent",
            dhatu = DhatuPatha.all.first { it.sourceSurface == "युज्" },
            bindings = emptyMap(),
        )
        val ukti = ExecutableUkti(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "युज्",
            prayojana = VakyaPrayojana.VIDHANA,
            invocations = listOf(invocation),
            dependencies = setOf(ActionDependency(invocation.id, invocation.id)),
        )

        val invalid = assertIs<ProgramGranthaCompilation.Invalid>(
            ExecutableUktiSutraCompiler.compileGranthaResult(ukti),
        )

        assertTrue(
            invalid.diagnostics.any {
                it.code == ProgramBlueprintDiagnosticCode.INVALID_BLUEPRINT
            },
        )
    }
}
