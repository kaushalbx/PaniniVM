package dev.panini.execution

import dev.panini.actions.control.LoopGovernanceModifier
import dev.panini.actions.numeric.AdditionAction
import dev.panini.sutra.SutraRole
import dev.panini.sutra.runtime.GranthaId
import dev.panini.sutra.runtime.SutraArthaValue
import dev.panini.sutra.runtime.SutraBlueprintGrantha
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextCodec
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextDecoding
import dev.panini.sutra.runtime.SutraBlueprintGranthaTextEncoding
import dev.panini.sutra.runtime.SutraId
import dev.panini.sutra.runtime.SutraRelation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class LoopGovernanceModifierTest {
    @Test
    fun `loop governance modifier creates kriya-visheshana blueprint with adhikara role`() {
        val targetId = SutraId("dhatu-action.numeric.add")
        val blueprint = LoopGovernanceModifier.createLoopBlueprint(targetId, 5)

        assertEquals(SutraId("kriya-visheshana.loop.dhatu-action.numeric.add"), blueprint.id)
        assertEquals(SutraRole.Vidhi, blueprint.role)
        assertEquals("kriya_visheshana", blueprint.artha.kind)
        assertEquals(SutraArthaValue.Symbol("loop"), blueprint.artha.fields["type"])
        assertEquals(SutraArthaValue.Number(5L), blueprint.artha.fields["count"])
        assertTrue(blueprint.relations.contains(SutraRelation.PhalaPravaha(blueprint.id, targetId)))

        val grantha = SutraBlueprintGrantha(
            id = GranthaId("loop-governance-test"),
            sutras = listOf(blueprint, AdditionAction.blueprint),
            exports = setOf(blueprint.id, targetId),
        )
        val encoding = SutraBlueprintGranthaTextCodec.encode(grantha)
        if (encoding is SutraBlueprintGranthaTextEncoding.Invalid) {
            error("Encoding invalid: " + encoding.diagnostics.joinToString { "${it.code}: ${it.message}" })
        }
        val source = (encoding as SutraBlueprintGranthaTextEncoding.Success).text
        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(source),
        ).grantha

        assertEquals(grantha.id, decoded.id)
        val decodedBlueprint = decoded.sutras.single { it.id == blueprint.id }
        assertEquals(blueprint.id, decodedBlueprint.id)
        assertEquals("kriya_visheshana", decodedBlueprint.artha.kind)
    }

    @Test
    fun `loop governance modifier executes structured loop iterations`() {
        var count = 0
        val results = LoopGovernanceModifier.executeStructured(
            condition = {
                count++
                ExecutionResult.Success(
                    value = if (count <= 3) "सत्यम्" else "असत्यम्",
                    operation = "परीक्षणम्",
                    trace = emptyList(),
                    typedValue = SanskritValue.Satya(count <= 3),
                )
            },
            body = {
                listOf(
                    ExecutionResult.Success(
                        value = "अनुष्ठानम् $count",
                        operation = "आवृत्तिः",
                        trace = listOf("Executed iteration $count"),
                    ),
                )
            },
        )

        assertEquals(7, results.size)
        assertTrue(results.all { it is ExecutionResult.Success })
    }
}
