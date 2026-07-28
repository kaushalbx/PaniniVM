package dev.panini.execution.sutra

import dev.panini.core.Karaka
import dev.panini.execution.AmbiguousKarakaBinding
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SanskritValue
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals

class ProgramSutraArthaCodecTest {
    @Test
    fun `ambiguous karaka bindings round trip with every candidate`() {
        val binding = AmbiguousKarakaBinding(
            expression = ExecutionExpression.Pada("फल"),
            candidates = linkedSetOf(Karaka.KARTR, Karaka.KARMAN),
        )

        assertEquals(
            binding,
            ProgramSutraArthaCodec.decodeAmbiguousBinding(
                ProgramSutraArthaCodec.encodeAmbiguousBinding(binding),
            ),
        )
    }

    @Test
    fun `execution expressions round trip without losing typed values`() {
        val expression = ExecutionExpression.Coordination(
            listOf(
                ExecutionExpression.Pada(
                    prakriti = "दश",
                    samjnas = setOf(Samjna.SANKHYA, Samjna.SHABDA),
                    value = SanskritValue.Sankhya(10, "दश"),
                ),
                ExecutionExpression.Reference("पूर्वफल"),
            ),
        )

        assertEquals(
            expression,
            ProgramSutraArthaCodec.decodeExpression(
                ProgramSutraArthaCodec.encodeExpression(expression),
            ),
        )
    }

    @Test
    fun `all Sanskrit value shapes round trip through sutra artha`() {
        val values = listOf(
            SanskritValue.Sankhya(12, "द्वादश"),
            SanskritValue.Rational(1, 2, "अर्धम्"),
            SanskritValue.Shabda("शब्दः", setOf(Samjna.SHABDA, Samjna.Rudhi("विशेषः"))),
            SanskritValue.Gana(listOf(SanskritValue.Shabda("अ"), SanskritValue.Shabda("इ"))),
            SanskritValue.Suchi(listOf(SanskritValue.Sankhya(1, "एकम्"))),
            SanskritValue.Satya(true),
        )

        values.forEach { value ->
            assertEquals(
                value,
                ProgramSutraArthaCodec.decodeValue(
                    ProgramSutraArthaCodec.encodeValue(value),
                ),
            )
        }
    }
}
