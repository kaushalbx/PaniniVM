package dev.panini.sutra.runtime

import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SutraProgramValidatorTest {
    @Test
    fun `machine applies dependencies before dependents while preserving stable order`() {
        val first = sutra("first")
        val second = sutra(
            "second",
            setOf(SutraRelation.DependsOn(SutraId("first"))),
        )
        val independent = sutra("independent")
        val program = SutraProgram(
            "stable-order",
            listOf(second, independent, first),
        )

        val result = assertIs<SutraMachineResult.Success<TestAvastha>>(
            SutraMachine(TestEffectInterpreter).process(program, TestAvastha()),
        )

        assertEquals(listOf("independent", "first", "second"), result.state.applied)
        assertEquals(
            listOf("independent", "first", "second"),
            result.trace.map { it.sutraId.value },
        )
    }

    @Test
    fun `missing dependency fails validation before applying effects`() {
        val program = SutraProgram(
            "missing",
            listOf(
                sutra(
                    "dependent",
                    setOf(SutraRelation.DependsOn(SutraId("absent"))),
                ),
            ),
        )

        val result = assertIs<SutraMachineResult.Failure<TestAvastha>>(
            SutraMachine(TestEffectInterpreter).process(program, TestAvastha()),
        )

        assertTrue(result.state.applied.isEmpty())
        assertTrue("missing sūtra absent" in result.message)
    }

    @Test
    fun `dependency cycle fails validation before applying effects`() {
        val program = SutraProgram(
            "cycle",
            listOf(
                sutra("a", setOf(SutraRelation.DependsOn(SutraId("b")))),
                sutra("b", setOf(SutraRelation.DependsOn(SutraId("a")))),
            ),
        )

        val validation = SutraProgramValidator.validate(program)
        assertEquals(
            SutraProgramDiagnosticCode.DEPENDENCY_CYCLE,
            validation.diagnostics.single().code,
        )
        val result = assertIs<SutraMachineResult.Failure<TestAvastha>>(
            SutraMachine(TestEffectInterpreter).process(program, TestAvastha()),
        )
        assertTrue(result.state.applied.isEmpty())
    }

    private fun sutra(
        id: String,
        relations: Set<SutraRelation> = emptySet(),
    ): RuntimeSutra<TestAvastha> = RuntimeSutra(
        id = SutraId(id),
        source = SutraSource.Program("test", id, id),
        role = SutraRole.Vidhi,
        artha = SutraArtha("test"),
        evaluator = { runtime, _ ->
            SutraNirnaya.Applicable(listOf(TestEffect(runtime.id.value)))
        },
        relations = relations,
    )

    private data class TestAvastha(
        val applied: List<String> = emptyList(),
    ) : SutraAvastha

    private data class TestEffect(
        val value: String,
    ) : SutraEffect<TestAvastha>

    private object TestEffectInterpreter : SutraEffectInterpreter<TestAvastha> {
        override fun apply(
            effect: SutraEffect<TestAvastha>,
            state: TestAvastha,
        ): SutraEffectApplication<TestAvastha> {
            val testEffect = assertIs<TestEffect>(effect)
            return SutraEffectApplication.Applied(
                state.copy(applied = state.applied + testEffect.value),
                "Applied ${testEffect.value}.",
            )
        }
    }
}
