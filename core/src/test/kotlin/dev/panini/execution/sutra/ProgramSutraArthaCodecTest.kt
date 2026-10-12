package dev.panini.execution.sutra

import dev.panini.core.SupAffix
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.ListMemberType
import dev.panini.execution.SanskritValue
import dev.panini.sutra.runtime.SutraArthaTextCodec
import dev.panini.sutra.runtime.SutraArthaTextDecoding
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertFailsWith

class ProgramSutraArthaCodecTest {
    @Test fun `ambiguous bindings preserve typed slot evidence through semantic serialization`() {
        val binding = dev.panini.execution.AmbiguousKarakaBinding(
            ExecutionExpression.TypedOperand(SanskritValue.Sankhya(2, "द्वि"), SupAffix.BHYAM_3, "मान"),
            setOf(dev.panini.core.Karaka.KARANA, dev.panini.core.Karaka.APADANA),
            mapOf(dev.panini.core.Karaka.KARANA to SupAffix.BHYAM_3,
                dev.panini.core.Karaka.APADANA to SupAffix.BHYAM_5))
        val encoded = ProgramSutraArthaCodec.encodeAmbiguousBinding(binding)
        val text = SutraArthaTextCodec.encode(encoded)
        val decoded = assertIs<SutraArthaTextDecoding.Success>(SutraArthaTextCodec.decode(text))
        assertEquals(binding, ProgramSutraArthaCodec.decodeAmbiguousBinding(decoded.value))
        assertEquals(binding.copy(supSlots = emptyMap()), ProgramSutraArthaCodec.decodeAmbiguousBinding(
            dev.panini.sutra.runtime.SutraArthaValue.Record(encoded.fields - "supSlots")))
        assertFailsWith<IllegalArgumentException> {
            binding.copy(supSlots = mapOf(dev.panini.core.Karaka.KARMAN to SupAffix.AM))
        }
    }

    @Test fun `typed reference identity must not be blank`() {
        assertFailsWith<IllegalArgumentException> {
            ExecutionExpression.TypedOperand(SanskritValue.Shabda("क्रम"), SupAffix.AM, " ")
        }
    }

    private fun roundTrip(expression: ExecutionExpression): ExecutionExpression {
        val text = SutraArthaTextCodec.encode(ProgramSutraArthaCodec.encodeExpression(expression))
        val decoded = assertIs<SutraArthaTextDecoding.Success>(SutraArthaTextCodec.decode(text))
        return ProgramSutraArthaCodec.decodeExpression(decoded.value)
    }

    @Test fun `named operands preserve provenance independently of payload and case`() {
        for (value in listOf(
            SanskritValue.Suchi(emptyList(), ListMemberType.NUMBER),
            SanskritValue.Sankhya(1, "एक"),
            SanskritValue.Shabda("सूची"),
        )) {
            for (sup in listOf(SupAffix.AM, SupAffix.TA)) {
                val operand = ExecutionExpression.TypedOperand(value, sup, referenceName = "क्रम")
                assertEquals(operand, roundTrip(operand))
            }
        }
    }

    @Test fun `literal operands do not acquire reference provenance`() {
        val literal = ExecutionExpression.TypedOperand(SanskritValue.Shabda("क्रम"), SupAffix.AM)
        assertEquals(literal, roundTrip(literal))
        val fields = ProgramSutraArthaCodec.encodeExpression(literal).fields
        assertEquals(false, "referenceName" in fields)
    }
}
