package dev.panini.sutra.runtime

import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SutraBlueprintGranthaValidatorTest {
    @Test
    fun `valid blueprint grantha receives stable dependency order`() {
        val first = blueprint("first")
        val second = blueprint(
            "second",
            setOf(SutraRelation.DependsOn(first.id)),
        )
        val independent = blueprint("independent")
        val validation = SutraBlueprintGranthaValidator.validate(
            SutraBlueprintGrantha(
                id = GranthaId("valid"),
                sutras = listOf(second, independent, first),
                exports = setOf(second.id),
            ),
        )

        assertTrue(validation.isValid)
        assertEquals(
            listOf(independent.id, first.id, second.id),
            validation.orderedSutras.map { it.id },
        )
    }

    @Test
    fun `invalid blueprint grantha reports package and relation errors together`() {
        val existing = blueprint(
            "existing",
            relations = setOf(
                SutraRelation.DependsOn(SutraId("missing-dependency")),
                SutraRelation.Blocks(SutraId("missing-block")),
                SutraRelation.PhalaPravaha(
                    SutraId("missing-source"),
                    SutraId("missing-target"),
                ),
            ),
            governance = SutraGovernance(blocks = setOf("missing-block")),
        )
        val validation = SutraBlueprintGranthaValidator.validate(
            SutraBlueprintGrantha(
                id = GranthaId("invalid"),
                sutras = listOf(existing, existing),
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
            ),
        )

        assertEquals(
            setOf(
                SutraBlueprintGranthaDiagnosticCode.DUPLICATE_IMPORT_ALIAS,
                SutraBlueprintGranthaDiagnosticCode.DUPLICATE_SAMJNA,
                SutraBlueprintGranthaDiagnosticCode.DUPLICATE_SUTRA_ID,
                SutraBlueprintGranthaDiagnosticCode.MISSING_ADHIKARA_SUTRA,
                SutraBlueprintGranthaDiagnosticCode.MISSING_ADHIKARA_MEMBER,
                SutraBlueprintGranthaDiagnosticCode.MISSING_EXPORT,
                SutraBlueprintGranthaDiagnosticCode.MISSING_DEPENDENCY,
                SutraBlueprintGranthaDiagnosticCode.MISSING_BLOCK_TARGET,
                SutraBlueprintGranthaDiagnosticCode.MISSING_FLOW_SOURCE,
                SutraBlueprintGranthaDiagnosticCode.MISSING_FLOW_TARGET,
            ),
            validation.diagnostics.mapTo(mutableSetOf()) { it.code },
        )
    }

    @Test
    fun `dependency cycles are rejected before domain compilation`() {
        val first = blueprint(
            "first",
            setOf(SutraRelation.DependsOn(SutraId("second"))),
        )
        val second = blueprint(
            "second",
            setOf(SutraRelation.DependsOn(first.id)),
        )

        val validation = SutraBlueprintGranthaValidator.validate(
            SutraBlueprintGrantha(GranthaId("cyclic"), listOf(first, second)),
        )

        assertTrue(
            validation.diagnostics.any {
                it.code == SutraBlueprintGranthaDiagnosticCode.DEPENDENCY_CYCLE
            },
        )
    }

    private fun blueprint(
        id: String,
        relations: Set<SutraRelation> = emptySet(),
        governance: SutraGovernance = SutraGovernance(),
    ) = SutraBlueprint(
        id = SutraId(id),
        source = SutraSource.Program("test", id, id),
        role = SutraRole.Vidhi,
        artha = SutraArtha("test"),
        relations = relations,
        governance = governance,
    )
}
