package dev.panini.sutra.runtime

import dev.panini.sutra.SutraRole
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SutraBlueprintGranthaTextCodecTest {
    @Test
    fun `segmented blueprint grantha round trips through canonical source`() {
        val first = blueprint("first")
        val second = blueprint(
            "second",
            setOf(SutraRelation.DependsOn(first.id)),
        )
        val grantha = SutraBlueprintGrantha(
            id = GranthaId("application"),
            sutras = listOf(first, second),
            imports = listOf(GranthaImport(GranthaId("library"), "lib")),
            adhikaras = listOf(AdhikaraDeclaration(first.id, setOf(second.id))),
            samjnas = listOf(SamjnaDeclaration("फलम्", "Result value")),
            exports = setOf(second.id),
        )

        val encoded = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(grantha),
        )
        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(encoded.text),
        )
        val encodedValue = assertIs<SutraArthaTextDecoding.Success>(
            SutraArthaTextCodec.decode(encoded.text),
        ).value as SutraArthaValue.Record
        val nestedSutras = encodedValue.fields["sutras"] as SutraArthaValue.Sequence

        assertEquals(grantha, decoded.grantha)
        nestedSutras.values.forEach { assertIs<SutraArthaValue.Record>(it) }
        assertEquals(
            encoded.text,
            assertIs<SutraBlueprintGranthaTextEncoding.Success>(
                SutraBlueprintGranthaTextCodec.encode(decoded.grantha),
            ).text,
        )
    }

    @Test
    fun `legacy text-wrapped nested blueprints remain readable`() {
        val blueprint = blueprint("legacy")
        val blueprintText = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        ).text
        val legacySource = SutraArthaTextCodec.encode(
            SutraArthaValue.Record(
                mapOf(
                    "id" to SutraArthaValue.Text("legacy"),
                    "imports" to SutraArthaValue.Sequence(emptyList()),
                    "adhikaras" to SutraArthaValue.Sequence(emptyList()),
                    "samjnas" to SutraArthaValue.Sequence(emptyList()),
                    "exports" to SutraArthaValue.Sequence(
                        listOf(SutraArthaValue.Text("legacy")),
                    ),
                    "sutras" to SutraArthaValue.Sequence(
                        listOf(SutraArthaValue.Text(blueprintText)),
                    ),
                ),
            ),
        )

        val decoded = assertIs<SutraBlueprintGranthaTextDecoding.Success>(
            SutraBlueprintGranthaTextCodec.decode(legacySource),
        )

        assertEquals(listOf(blueprint), decoded.grantha.sutras)
    }

    @Test
    fun `non-portable nested sutra identifies its package position`() {
        val grantha = SutraBlueprintGrantha(
            GranthaId("host-bound"),
            listOf(
                SutraBlueprint(
                    SutraId("adhikara"),
                    SutraSource.Program("host-bound", "1", "adhikara"),
                    SutraRole.Adhikara(10),
                    SutraArtha("adhikara"),
                ),
            ),
        )

        val invalid = assertIs<SutraBlueprintGranthaTextEncoding.Invalid>(
            SutraBlueprintGranthaTextCodec.encode(grantha),
        )

        assertEquals(
            SutraBlueprintGranthaTextDiagnosticCode.INVALID_BLUEPRINT,
            invalid.diagnostics.single().code,
        )
    }

    @Test
    fun `decoded package is validated before it is accepted`() {
        val invalidGrantha = SutraBlueprintGrantha(
            GranthaId("invalid"),
            listOf(blueprint("present")),
            exports = setOf(SutraId("missing")),
        )
        val encoded = assertIs<SutraBlueprintGranthaTextEncoding.Success>(
            SutraBlueprintGranthaTextCodec.encode(invalidGrantha),
        )

        val invalid = assertIs<SutraBlueprintGranthaTextDecoding.Invalid>(
            SutraBlueprintGranthaTextCodec.decode(encoded.text),
        )

        assertEquals(
            SutraBlueprintGranthaTextDiagnosticCode.INVALID_GRANTHA,
            invalid.diagnostics.single().code,
        )
    }

    private fun blueprint(
        id: String,
        relations: Set<SutraRelation> = emptySet(),
    ) = SutraBlueprint(
        SutraId(id),
        SutraSource.Program("test", id, id),
        SutraRole.Vidhi,
        SutraArtha("test"),
        relations,
    )
}
