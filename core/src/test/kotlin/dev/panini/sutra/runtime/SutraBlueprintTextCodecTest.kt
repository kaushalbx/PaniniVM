package dev.panini.sutra.runtime

import dev.panini.sutra.ParibhashaScope
import dev.panini.sutra.SutraGovernance
import dev.panini.sutra.SutraPriority
import dev.panini.sutra.SutraRole
import dev.panini.sutra.SutraVisibility
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SutraBlueprintTextCodecTest {
    @Test
    fun `portable blueprint round trips through canonical text`() {
        val blueprint = SutraBlueprint(
            id = SutraId("generated-rule"),
            source = SutraSource.Program("ganita", "1:1", "युज्"),
            role = SutraRole.Paribhasha(ParibhashaScope.GENERAL),
            artha = SutraArtha(
                "kriya",
                mapOf(
                    "dhatu" to SutraArthaValue.Text("युज्"),
                    "karakas" to SutraArthaValue.Record(
                        mapOf("KARMAN" to SutraArthaValue.Sequence(emptyList())),
                    ),
                ),
            ),
            relations = linkedSetOf(
                SutraRelation.PhalaPravaha(SutraId("first"), SutraId("generated-rule")),
                SutraRelation.DependsOn(SutraId("first")),
                SutraRelation.Blocks(SutraId("blocked")),
            ),
            governance = SutraGovernance(
                optional = true,
                priority = SutraPriority.APAVADA,
                blocks = setOf("blocked"),
                visibility = SutraVisibility.ASIDDHAVAT,
            ),
        )

        val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
            SutraBlueprintTextCodec.encode(blueprint),
        )
        val decoded = assertIs<SutraBlueprintTextDecoding.Success>(
            SutraBlueprintTextCodec.decode(encoded.text),
        )

        assertEquals(blueprint, decoded.blueprint)
        assertEquals(
            encoded.text,
            assertIs<SutraBlueprintTextEncoding.Success>(
                SutraBlueprintTextCodec.encode(decoded.blueprint),
            ).text,
        )
    }

    @Test
    fun `all portable sources and roles round trip`() {
        val sources = listOf(
            SutraSource.Ashtadhyayi("1.1.1", "वृद्धिरादैच्"),
            SutraSource.Vakya("उक्ति", 2, "युज्"),
            SutraSource.Program("grantha", "3:4", "rule"),
        )
        val roles = listOf(
            SutraRole.Samjna,
            SutraRole.Vidhi,
            SutraRole.Nishedha,
            SutraRole.Niyama,
            SutraRole.Atidesha,
            SutraRole.Anuvrtti,
            SutraRole.Apavada,
            SutraRole.Vibhasha,
            SutraRole.Paribhasha(ParibhashaScope.VOWEL_SUBSTITUTION),
        )

        sources.forEachIndexed { index, source ->
            roles.forEach { role ->
                val blueprint = SutraBlueprint(
                    SutraId("rule-$index-${role::class.simpleName}"),
                    source,
                    role,
                    SutraArtha("test"),
                )
                val encoded = assertIs<SutraBlueprintTextEncoding.Success>(
                    SutraBlueprintTextCodec.encode(blueprint),
                )
                assertEquals(
                    blueprint,
                    assertIs<SutraBlueprintTextDecoding.Success>(
                        SutraBlueprintTextCodec.decode(encoded.text),
                    ).blueprint,
                )
            }
        }
    }

    @Test
    fun `host predicate role is rejected explicitly`() {
        val blueprint = SutraBlueprint(
            SutraId("host-bound"),
            SutraSource.Program("test", "1", "adhikara"),
            SutraRole.Adhikara(endKrama = 10),
            SutraArtha("adhikara"),
        )

        val invalid = assertIs<SutraBlueprintTextEncoding.Invalid>(
            SutraBlueprintTextCodec.encode(blueprint),
        )

        assertEquals(
            SutraBlueprintTextDiagnosticCode.UNSUPPORTED_ROLE,
            invalid.diagnostics.single().code,
        )
    }

    @Test
    fun `malformed blueprint source distinguishes syntax from schema errors`() {
        val syntax = assertIs<SutraBlueprintTextDecoding.Invalid>(
            SutraBlueprintTextCodec.decode("""{"id":t"\q"}"""),
        )
        val schema = assertIs<SutraBlueprintTextDecoding.Invalid>(
            SutraBlueprintTextCodec.decode("""{"id":t"only"}"""),
        )

        assertEquals(
            SutraBlueprintTextDiagnosticCode.INVALID_ARTHA_TEXT,
            syntax.diagnostics.single().code,
        )
        assertEquals(
            SutraBlueprintTextDiagnosticCode.INVALID_SCHEMA,
            schema.diagnostics.single().code,
        )
    }
}
