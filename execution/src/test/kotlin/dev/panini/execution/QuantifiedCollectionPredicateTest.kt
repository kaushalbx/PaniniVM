package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlin.test.assertFails

class QuantifiedCollectionPredicateTest {
    private val declaration = "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।"

    @Test
    fun `some member predicate returns true and short circuits`() {
        val results = PaniniVM().evalScript(
            "$declaration\nसूची + ङस् कश्चित् सदस्य + सुँ द्वि + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।",
        )

        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        val result = assertIs<ExecutionResult.Success>(results.last())
        assertEquals(true, assertIs<SanskritValue.Satya>(result.typedValue).boolean)
        assertTrue(result.trace.last().contains("3 of 3"), result.trace.toString())
    }

    @Test
    fun `some member predicate returns false when no member matches`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n$anySource",
        )

        assertEquals(
            false,
            assertIs<SanskritValue.Satya>(assertIs<ExecutionResult.Success>(results.last()).typedValue).boolean,
        )
    }

    @Test
    fun `every member predicate distinguishes partial and complete matches`() {
        val partial = PaniniVM().evalScript(
            "$declaration\nसूची + ङस् प्रत्येकः सदस्य + सुँ एक + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।",
        ).last()
        val complete = PaniniVM().evalScript(
            "$declaration\nसूची + ङस् प्रत्येकः सदस्य + सुँ शून्य + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।",
        ).last()

        assertEquals(false, assertIs<SanskritValue.Satya>(assertIs<ExecutionResult.Success>(partial).typedValue).boolean)
        assertEquals(true, assertIs<SanskritValue.Satya>(assertIs<ExecutionResult.Success>(complete).typedValue).boolean)
    }

    @Test
    fun `quantified predicate composes as a conditional condition`() {
        val results = PaniniVM().evalScript(
            "$declaration\nयदि सूची + ङस् कश्चित् सदस्य + सुँ द्वि + ङसिँ अधिक + सुँ असँ + लट् + तिप् " +
                "तर्हि नव अन्यथा शून्य ।",
        )

        assertEquals("नव", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test
    fun `empty lists and groups use existential and universal identities`() {
        for (collection in listOf(
            SanskritValue.Suchi(emptyList()),
            SanskritValue.Gana(emptyList()),
        )) {
            assertEquals(false, evaluate(anySource, collection).boolean)
            assertEquals(true, evaluate(allSource, collection).boolean)
        }
    }

    @Test
    fun `group members retain typed numeric predicate behavior`() {
        val group = SanskritValue.Gana(listOf(
            SanskritValue.Sankhya(1, "एक"),
            SanskritValue.Sankhya(2, "द्वि"),
            SanskritValue.Sankhya(3, "त्रि"),
        ))

        assertEquals(true, evaluate(anySource, group).boolean)
        val allResult = evaluate(allSource, group)
        assertEquals(true, allResult.boolean)
    }

    @Test
    fun `typed predicate mismatch propagates as a failure`() {
        val vm = PaniniVM()
        val scope = vm.defaultScope.copy(environment = ValueEnvironment(mapOf(
            "सूची" to SanskritValue.Gana(listOf(SanskritValue.Shabda("त्रि"))),
        )))

        val result = vm.evalScript(anySource, scope = scope).last()

        assertIs<ExecutionResult.Failure>(result)
    }

    @Test
    fun `non truth member predicates fail explicitly`() {
        val source = "$declaration\nसूची + ङस् कश्चित् सदस्य + सुँ सदस्य + अम् गण् + लोट् + सिप् ।"

        val result = PaniniVM().evalScript(source).last()

        assertIs<ExecutionResult.Failure>(result)
        assertTrue(result.message.contains("truth value"), result.message)
    }

    @Test
    fun `quantifier morphology validates collection and member cases`() {
        assertFails {
            dev.panini.vyakaranam.parser.PaniniParser().parse(
                "सूची + अम् कश्चित् सदस्य + सुँ द्वि + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।",
            )
        }
        assertFails {
            dev.panini.vyakaranam.parser.PaniniParser().parse(
                "सूची + ङस् कश्चित् सदस्य + अम् द्वि + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।",
            )
        }
    }

    private val anySource =
        "सूची + ङस् कश्चित् सदस्य + सुँ द्वि + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।"
    private val allSource =
        "सूची + ङस् प्रत्येकः सदस्य + सुँ शून्य + ङसिँ अधिक + सुँ असँ + लट् + तिप् ।"

    private fun evaluate(source: String, collection: SanskritValue): SanskritValue.Satya {
        val vm = PaniniVM()
        val scope = vm.defaultScope.copy(environment = ValueEnvironment(mapOf("सूची" to collection)))
        val result = vm.evalScript(source, scope = scope).last()
        return assertIs(assertIs<ExecutionResult.Success>(result).typedValue)
    }
}
