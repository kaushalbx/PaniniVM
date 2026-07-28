package dev.panini.sutra.runtime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class SutraArthaTextCodecTest {
    @Test
    fun `every semantic value shape round trips through canonical text`() {
        val value = SutraArthaValue.Record(
            linkedMapOf(
                "text" to SutraArthaValue.Text("सूत्रम्\n\"अर्थः\""),
                "symbol" to SutraArthaValue.Symbol("कर्म"),
                "number" to SutraArthaValue.Number(-42),
                "truth" to SutraArthaValue.Truth(true),
                "reference" to SutraArthaValue.SutraReference(SutraId("१.१.१")),
                "sequence" to SutraArthaValue.Sequence(
                    listOf(
                        SutraArthaValue.Truth(false),
                        SutraArthaValue.Record(
                            mapOf("slash" to SutraArthaValue.Text("\\")),
                        ),
                    ),
                ),
            ),
        )

        val encoded = SutraArthaTextCodec.encode(value)
        val decoded = assertIs<SutraArthaTextDecoding.Success>(
            SutraArthaTextCodec.decode(encoded),
        )

        assertEquals(value, decoded.value)
    }

    @Test
    fun `record output is deterministic regardless of map insertion order`() {
        val first = SutraArthaValue.Record(
            linkedMapOf(
                "z" to SutraArthaValue.Number(2),
                "a" to SutraArthaValue.Number(1),
            ),
        )
        val second = SutraArthaValue.Record(
            linkedMapOf(
                "a" to SutraArthaValue.Number(1),
                "z" to SutraArthaValue.Number(2),
            ),
        )

        assertEquals(
            "{\"a\":n1,\"z\":n2}",
            SutraArthaTextCodec.encode(first),
        )
        assertEquals(
            SutraArthaTextCodec.encode(first),
            SutraArthaTextCodec.encode(second),
        )
    }

    @Test
    fun `reader accepts insignificant whitespace`() {
        val decoded = assertIs<SutraArthaTextDecoding.Success>(
            SutraArthaTextCodec.decode(
                """
                {
                  "kind": s"kriya",
                  "values": [ n1, bfalse ]
                }
                """.trimIndent(),
            ),
        )

        assertEquals(
            SutraArthaValue.Record(
                mapOf(
                    "kind" to SutraArthaValue.Symbol("kriya"),
                    "values" to SutraArthaValue.Sequence(
                        listOf(SutraArthaValue.Number(1), SutraArthaValue.Truth(false)),
                    ),
                ),
            ),
            decoded.value,
        )
    }

    @Test
    fun `malformed source reports position and diagnostic category`() {
        val invalidEscape = assertIs<SutraArthaTextDecoding.Invalid>(
            SutraArthaTextCodec.decode("""t"\q""""),
        )
        val duplicateField = assertIs<SutraArthaTextDecoding.Invalid>(
            SutraArthaTextCodec.decode("""{"a":n1,"a":n2}"""),
        )
        val trailing = assertIs<SutraArthaTextDecoding.Invalid>(
            SutraArthaTextCodec.decode("n1 extra"),
        )

        assertEquals(
            SutraArthaTextDiagnosticCode.INVALID_ESCAPE,
            invalidEscape.diagnostics.single().code,
        )
        assertTrue(invalidEscape.diagnostics.single().position > 0)
        assertEquals(
            SutraArthaTextDiagnosticCode.DUPLICATE_FIELD,
            duplicateField.diagnostics.single().code,
        )
        assertEquals(
            SutraArthaTextDiagnosticCode.TRAILING_INPUT,
            trailing.diagnostics.single().code,
        )
    }
}
