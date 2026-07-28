package dev.panini.sutra.runtime

import dev.panini.execution.SanskritValue
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SutraBlueprintTest {
    @Test
    fun `runtime sutra exports evaluator-free blueprint`() {
        val runtime = runtimeSutra()

        val blueprint = runtime.toBlueprint()

        assertEquals(runtime.id, blueprint.id)
        assertEquals(runtime.artha, blueprint.artha)
        assertEquals(runtime.relations, blueprint.relations)
        assertEquals(runtime.governance, blueprint.governance)
    }

    @Test
    fun `semantic specialization does not mutate source blueprint`() {
        val original = runtimeSutra().toBlueprint()

        val specialized = original.specializedAs(
            newId = SutraId("generated"),
            semanticFields = mapOf(
                "dhatu" to SutraArthaValue.Text("दृश्"),
                "generated" to SutraArthaValue.Truth(true),
            ),
        )

        assertEquals(SutraArthaValue.Text("युज्"), original.artha.fields["dhatu"])
        assertFalse("generated" in original.artha.fields)
        assertEquals(SutraId("generated"), specialized.id)
        assertEquals(SutraArthaValue.Text("दृश्"), specialized.artha.fields["dhatu"])
        assertEquals(
            SanskritValue.Shabda("दृश्"),
            specialized.artha.fields["dhatu"]?.let {
                with(SutraIntrospector) { it.toSanskritValue() }
            },
        )
    }

    @Test
    fun `blueprint validator rejects self dependency and inconsistent blocks`() {
        val blueprint = runtimeSutra().toBlueprint().copy(
            relations = setOf(
                SutraRelation.DependsOn(SutraId("source")),
                SutraRelation.Blocks(SutraId("blocked-by-relation")),
            ),
            governance = SutraGovernance(
                blocks = setOf("blocked-by-governance"),
            ),
        )

        val diagnostics = SutraBlueprintValidator.validate(blueprint)

        assertEquals(
            setOf(
                SutraBlueprintDiagnosticCode.SELF_DEPENDENCY,
                SutraBlueprintDiagnosticCode.BLOCK_RELATION_MISMATCH,
            ),
            diagnostics.mapTo(mutableSetOf()) { it.code },
        )
        assertIs<SanskritValue.Suchi>(SutraIntrospector.describe(blueprint))
    }

    private fun runtimeSutra(): RuntimeSutra<TestAvastha> = RuntimeSutra(
        id = SutraId("source"),
        source = SutraSource.Program("test", "source", "source"),
        role = SutraRole.Vidhi,
        artha = SutraArtha(
            "kriya",
            mapOf("dhatu" to SutraArthaValue.Text("युज्")),
        ),
        evaluator = { _, _ -> SutraNirnaya.Applicable(emptyList()) },
    )

    private data object TestAvastha : SutraAvastha
}
