package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.dhatupatha.DhatuPatha
import dev.panini.dhatupatha.DhatuPathaRegistration
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CollectionOperationsTest {
    @Test
    fun `natural membership uses typed structural equality and unpacks groups`() {
        val operation = dev.panini.actions.collection.ListContainsAction.op()
        val number = SanskritValue.Sankhya(2, "द्वि")
        val record = SanskritValue.Rupa("बिन्दु", mapOf("मान" to number))
        val cases = listOf(
            Triple(SanskritValue.Suchi(listOf(number)), number.copy(word = "द्वे"), true),
            Triple(SanskritValue.Gana(listOf(number)), number.copy(word = "द्वे"), true),
            Triple(SanskritValue.Suchi(listOf(number)), SanskritValue.Shabda("द्वि"), false),
            Triple(SanskritValue.Suchi(listOf(record)), record.copy(fields = mapOf("मान" to SanskritValue.Sankhya(3, "त्रि"))), false),
        )
        cases.forEach { (collection, query, expected) ->
            val context = ExecutionContext(bindings = mapOf(
                Karaka.ADHIKARANA to ExecutionExpression.Pada("सूची", value = collection),
                Karaka.KARTR to ExecutionExpression.Pada("वस्तु", value = query),
            ))
            val result = assertIs<ExecutionResult.Success>(operation.action.execute(context, operation))
            assertEquals(expected, assertIs<SanskritValue.Satya>(result.typedValue).boolean)
        }
    }

    @Test
    fun `natural membership rejects a scalar location`() {
        val operation = dev.panini.actions.collection.ListContainsAction.op()
        val context = ExecutionContext(bindings = mapOf(
            Karaka.ADHIKARANA to ExecutionExpression.sankhya(2, "द्वि"),
            Karaka.KARTR to ExecutionExpression.sankhya(2, "द्वि"),
        ))
        assertIs<ExecutionResult.Failure>(operation.action.execute(context, operation))
    }

    @Test
    fun `slice clips the minimum supported lower bound without overflow`() {
        DhatuPathaRegistration.ensureRegistered()
        val operation = DhatuPatha.all.first { it.upadesha == "ग्रहँ" }.operations.first { it.name == "सूचीविभागः" }
        val list = SanskritValue.Suchi(listOf(SanskritValue.Sankhya(7, "सप्त")))
        val context = ExecutionContext(bindings = mapOf(
            Karaka.KARMAN to ExecutionExpression.Pada("अंश"),
            Karaka.SAMBANDHA to ExecutionExpression.TypedOperand(list, dev.panini.core.SupAffix.NGAS),
            Karaka.APADANA to ExecutionExpression.sankhya(Int.MIN_VALUE.toLong(), "न्यूनतम"),
            Karaka.ADHIKARANA to ExecutionExpression.sankhya(1, "एक"),
        ))
        assertEquals(list, assertIs<ExecutionResult.Success>(operation.action.execute(context, operation)).typedValue)
    }

    @Test
    fun `indexed retrieval rejects coordinated and mixed index operands`() {
        DhatuPathaRegistration.ensureRegistered()
        val operation = DhatuPatha.all.first { it.upadesha == "ग्रहँ" }.operations.first { it.name == "सूचीस्थानम्" }
        val source = ExecutionExpression.TypedOperand(
            SanskritValue.Suchi(listOf(SanskritValue.Sankhya(10, "दश"))), dev.panini.core.SupAffix.NGASI,
        )
        for (extra in listOf(ExecutionExpression.sankhya(2, "द्वि"), ExecutionExpression.Pada("अज्ञात"))) {
            val context = ExecutionContext(bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("मूल्य"),
                Karaka.APADANA to source,
                Karaka.ADHIKARANA to ExecutionExpression.Coordination(ExecutionExpression.sankhya(1, "एक"), extra),
            ))
            assertIs<ExecutionResult.Failure>(operation.action.execute(context, operation))
        }
    }

    @Test
    fun `natural indexed retrieval unwraps gana and rejects scalar sources`() {
        DhatuPathaRegistration.ensureRegistered()
        val operation = DhatuPatha.all.first { it.upadesha == "ग्रहँ" }.operations.first { it.name == "सूचीस्थानम्" }
        val context = ExecutionContext(bindings = mapOf(
            Karaka.KARMAN to ExecutionExpression.Pada("मूल्य"),
            Karaka.APADANA to ExecutionExpression.TypedOperand(
                SanskritValue.Gana(listOf(SanskritValue.Sankhya(10, "दश"), SanskritValue.Sankhya(20, "विंशति"))),
                dev.panini.core.SupAffix.NGASI,
            ),
            Karaka.ADHIKARANA to ExecutionExpression.sankhya(2, "द्वि"),
        ))
        val result = assertIs<ExecutionResult.Success>(operation.action.execute(context, operation))
        assertEquals(20L, assertIs<SanskritValue.Sankhya>(result.typedValue).value)
        val scalar = context.copy(bindings = context.bindings + (
            Karaka.APADANA to ExecutionExpression.sankhya(10, "दश")
        ))
        assertIs<ExecutionResult.Failure>(operation.action.execute(scalar, operation))
    }

    @Test
    fun `natural extraction rejects scalar and empty genitive wholes`() {
        DhatuPathaRegistration.ensureRegistered()
        val operation = DhatuPatha.all.first { it.upadesha == "हृञ्" }.operations.first { it.name == "सूच्युद्धरणम्" }
        for (value in listOf(SanskritValue.Sankhya(2, "द्वि"), SanskritValue.Suchi(emptyList()))) {
            val context = ExecutionContext(bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("अन्तिम"),
                Karaka.SAMBANDHA to ExecutionExpression.TypedOperand(value, dev.panini.core.SupAffix.NGAS),
            ))
            assertIs<ExecutionResult.Failure>(operation.action.execute(context, operation))
        }
    }

    @Test
    fun `HrDhatu executes ListPopAction to pop last element`() {
        DhatuPathaRegistration.ensureRegistered()
        val hr = DhatuPatha.all.first { it.upadesha == "हृञ्" }
        // Find list pop operation
        val popOp = hr.operations.first { it.name == "सूच्युद्धरणम्" || it.action.name == "सूच्युद्धरणम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि")
                )
            )
        )

        val result = popOp.action.execute(context, popOp)
        assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals("त्रि", result.value) // Popped last element
        assertIs<SanskritValue.Sankhya>(result.typedValue)
        assertEquals(3L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `GanDhatu executes ListLengthAction to count size`() {
        DhatuPathaRegistration.ensureRegistered()
        val gan = DhatuPatha.all.first { it.upadesha == "गण" }
        // Find list length operation
        val lengthOp = gan.operations.first { it.name == "सूच्याकारः" || it.action.name == "सूच्याकारः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10L, "दश"),
                    ExecutionExpression.sankhya(20L, "विंशति")
                )
            )
        )

        // Setup mock renderer for 2
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            if (value == 2L) "द्वि" else value.toString()
        }

        val result = lengthOp.action.execute(context, lengthOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("द्वि", result.value) // Count is 2
        assertIs<SanskritValue.Sankhya>(result.typedValue)
        assertEquals(2L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `YuDhatu executes ListMapAction to map list elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val yu = DhatuPatha.all.first { it.upadesha == "यु" }
        // Find list map operation
        val mapOp = yu.operations.first { it.name == "सूचीसंयोजनम्" || it.action.name == "सूचीसंयोजनम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि")
                ),
                Karaka.KARANA to ExecutionExpression.Pada("एधँ") // exact target upadeśa
            )
        )

        // Setup mock renderer
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                6L -> "षट्"
                else -> value.toString()
            }
        }

        val result = mapOp.action.execute(context, mapOp)
        assertIs<ExecutionResult.Success>(result)
        
        // Mapped list: [1*2, 2*2, 3*2] -> [2, 4, 6] -> "द्वि चतुर् षट्"
        assertEquals("[द्वि, चतुर्, षट्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(3, typed.items.size)
        assertEquals(2L, (typed.items[0] as SanskritValue.Sankhya).value)
        assertEquals(4L, (typed.items[1] as SanskritValue.Sankhya).value)
        assertEquals(6L, (typed.items[2] as SanskritValue.Sankhya).value)
    }

    @Test
    fun `VrjDhatu executes ListFilterAction to filter list elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrj = DhatuPatha.all.first { it.upadesha == "वृजीँ" }
        val filterOp = vrj.operations.first { it.name == "सूचीशोधनम्" || it.action.name == "सूचीशोधनम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि"),
                    ExecutionExpression.sankhya(4L, "चतुर्")
                ),
                Karaka.KARANA to ExecutionExpression.Pada("यु") // exact target upadeśa; literal signature selects evenness
            )
        )

        // Setup mock renderer
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                else -> value.toString()
            }
        }

        val result = filterOp.action.execute(context, filterOp)
        assertIs<ExecutionResult.Success>(result)
        
        // Filtered list (even numbers only): [2, 4] -> "द्वि चतुर्"
        assertEquals("[द्वि, चतुर्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(2, typed.items.size)
        assertEquals(2L, (typed.items[0] as SanskritValue.Sankhya).value)
        assertEquals(4L, (typed.items[1] as SanskritValue.Sankhya).value)
    }

    @Test
    fun `SrjDhatu executes ListConcatAction to concatenate two lists`() {
        DhatuPathaRegistration.ensureRegistered()
        val srj = DhatuPatha.all.first { it.id == "06.0150" }
        val concatOp = srj.operations.first { it.name == "सूचीसंयोगः" || it.action.name == "सूचीसंयोगः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि")
                ),
                Karaka.SAMPRADANA to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(3L, "त्रि"),
                    ExecutionExpression.sankhya(4L, "चतुर्")
                )
            )
        )

        // Setup mock renderer
        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                1L -> "एक"
                2L -> "द्वि"
                3L -> "त्रि"
                4L -> "चतुर्"
                else -> value.toString()
            }
        }

        val result = concatOp.action.execute(context, concatOp)
        assertIs<ExecutionResult.Success>(result)

        // Combined: [1, 2, 3, 4] -> "एक द्वि त्रि चतुर्"
        assertEquals("[एक, द्वि, त्रि, चतुर्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(4, typed.items.size)
        assertEquals(1L, (typed.items[0] as SanskritValue.Sankhya).value)
        assertEquals(2L, (typed.items[1] as SanskritValue.Sankhya).value)
        assertEquals(3L, (typed.items[2] as SanskritValue.Sankhya).value)
        assertEquals(4L, (typed.items[3] as SanskritValue.Sankhya).value)
    }

    @Test
    fun `SthaDhatu executes ListIndexAction to retrieve element by index`() {
        DhatuPathaRegistration.ensureRegistered()
        val stha = DhatuPatha.all.first { it.id == "01.9901" }
        val indexOp = stha.operations.first { it.name == "सूचीस्थानम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10L, "दश"),
                    ExecutionExpression.sankhya(20L, "विंशति"),
                    ExecutionExpression.sankhya(30L, "त्रिंशत्")
                ),
                Karaka.KARANA to ExecutionExpression.sankhya(2L, "द्वि")
            )
        )

        val result = indexOp.action.execute(context, indexOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("विंशति", result.value)
        assertIs<SanskritValue.Sankhya>(result.typedValue)
        assertEquals(20L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `GrahDhatu retrieves a value from an ablative collection at a locative position`() {
        DhatuPathaRegistration.ensureRegistered()
        val grah = DhatuPatha.all.first { it.upadesha == "ग्रहँ" }
        val indexOp = grah.operations.first { it.name == "सूचीस्थानम्" }
        val list = SanskritValue.Suchi(
            listOf(
                SanskritValue.Sankhya(10L, "दश"),
                SanskritValue.Sankhya(20L, "विंशति"),
            ),
        )
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Pada("मूल्य"),
                Karaka.APADANA to ExecutionExpression.TypedOperand(list, dev.panini.core.SupAffix.NGASI),
                Karaka.ADHIKARANA to ExecutionExpression.sankhya(2L, "द्वि"),
            ),
        )

        val result = indexOp.action.execute(context, indexOp)

        assertIs<ExecutionResult.Success>(result)
        assertEquals(20L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `GrahDhatu gathers accusative objects when prefixed by sam`() {
        DhatuPathaRegistration.ensureRegistered()
        val grah = DhatuPatha.all.first { it.upadesha == "ग्रहँ" }
        val collect = grah.operations.single { it.name == "सूचीसङ्ग्रहः" }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि"),
                ),
            ),
        )

        val result = assertIs<ExecutionResult.Success>(collect.action.execute(context, collect))
        val list = assertIs<SanskritValue.Suchi>(result.typedValue)
        assertEquals(listOf(1L, 2L), list.items.map { (it as SanskritValue.Sankhya).value })
    }

    @Test
    fun `BhajDhatu executes ListSliceAction to slice elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val bhaj = DhatuPatha.all.first { it.id == "01.1153" }
        val sliceOp = bhaj.operations.first { it.name == "सूचीविभागः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(10L, "दश"),
                    ExecutionExpression.sankhya(20L, "विंशति"),
                    ExecutionExpression.sankhya(30L, "त्रिंशत्"),
                    ExecutionExpression.sankhya(40L, "चत्वारिंशत्")
                ),
                Karaka.KARANA to ExecutionExpression.sankhya(2L, "द्वि"),
                Karaka.SAMPRADANA to ExecutionExpression.sankhya(3L, "त्रि")
            )
        )

        val result = sliceOp.action.execute(context, sliceOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("[विंशति, त्रिंशत्]", result.value)
        val typed = result.typedValue
        assertIs<SanskritValue.Suchi>(typed)
        assertEquals(2, typed.items.size)
    }

    @Test
    fun `VrtDhatu executes ListReverseAction to reverse elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrt = DhatuPatha.all.first { it.id == "01.9910" }
        val reverseOp = vrt.operations.first { it.name == "सूचीविलोमः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(1L, "एक"),
                    ExecutionExpression.sankhya(2L, "द्वि")
                )
            )
        )

        val result = reverseOp.action.execute(context, reverseOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("[द्वि, एक]", result.value)
    }

    @Test
    fun `KshipDhatu executes ListFoldAction to aggregate elements`() {
        DhatuPathaRegistration.ensureRegistered()
        val kship = DhatuPatha.all.first { it.id == "06.0005" }
        val foldOp = kship.operations.first { it.name == "सूचीसङ्क्षेपः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(2L, "द्वि"),
                    ExecutionExpression.sankhya(3L, "त्रि")
                ),
                Karaka.KARANA to ExecutionExpression.Pada("गण"),
                Karaka.SAMPRADANA to ExecutionExpression.sankhya(5L, "पञ्च")
            )
        )

        SankhyaResultRenderer.defaultRenderer = SankhyaResultRenderer { value ->
            when (value) {
                10L -> "दश"
                30L -> "त्रिंशत्"
                else -> value.toString()
            }
        }

        val result = foldOp.action.execute(context, foldOp)
        assertIs<ExecutionResult.Success>(result, result.toString())
        assertEquals(30L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `JnaDhatu executes IfAction to branch execution`() {
        DhatuPathaRegistration.ensureRegistered()
        val jna = DhatuPatha.all.first { it.id == "09.0043" }
        val ifOp = jna.operations.first { it.name == "निर्णयः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.APADANA to ExecutionExpression.Pada("सत्यम्", setOf(Samjna.SHABDA), SanskritValue.Satya(true)),
                Karaka.KARANA to ExecutionExpression.Pada("यु"), // exact upadeśa; coordinated signature selects addition
                Karaka.SAMPRADANA to ExecutionExpression.Pada("वृजीँ"), // exact upadeśa for subtraction
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    ExecutionExpression.sankhya(5L, "पञ्च"),
                    ExecutionExpression.sankhya(10L, "दश")
                )
            )
        )

        val result = ifOp.action.execute(context, ifOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals(15L, (result.typedValue as SanskritValue.Sankhya).value)
    }

    @Test
    fun `IfAction rejects an untyped word that merely spells truth`() {
        DhatuPathaRegistration.ensureRegistered()
        val ifOp = DhatuPatha.all.first { it.id == "09.0043" }
            .operations.first { it.name == "निर्णयः" }
        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.APADANA to ExecutionExpression.Pada(
                    "सत्यम्",
                    setOf(Samjna.SHABDA),
                    SanskritValue.Shabda("सत्यम्"),
                ),
                Karaka.KARANA to ExecutionExpression.Pada("यु"),
            ),
        )

        val result = assertIs<ExecutionResult.Failure>(ifOp.action.execute(context, ifOp))

        assertEquals(ExecutionError.INVALID_VALUE, result.error)
        assertTrue(result.message.contains("typed truth value"))
    }

    @Test
    fun `TanDhatu executes ListFlattenAction to flatten nested lists`() {
        DhatuPathaRegistration.ensureRegistered()
        val tan = DhatuPatha.all.first { it.id == "08.0001" }
        val flattenOp = tan.operations.first { it.name == "सूचीप्रसारणम्" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.Coordination(
                            listOf(
                                ExecutionExpression.sankhya(1L, "एक"),
                                ExecutionExpression.sankhya(2L, "द्वि")
                            )
                        ),
                        ExecutionExpression.sankhya(3L, "त्रि")
                    )
                )
            )
        )

        val result = flattenOp.action.execute(context, flattenOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals("[एक, द्वि, त्रि]", result.value)
    }

    @Test
    fun `AsDhatu executes ListContainsAction to check existence`() {
        DhatuPathaRegistration.ensureRegistered()
        val asDhatu = DhatuPatha.all.first { it.id == "02.0060" }
        val containsOp = asDhatu.operations.first { it.name == "सूच्यस्तित्वम्" }

        // Test contains true
        val contextTrue = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.sankhya(1L, "एक"),
                        ExecutionExpression.sankhya(2L, "द्वि")
                    )
                ),
                Karaka.KARANA to ExecutionExpression.sankhya(2L, "द्वि")
            )
        )
        val resultTrue = containsOp.action.execute(contextTrue, containsOp)
        assertIs<ExecutionResult.Success>(resultTrue)
        assertEquals("सत्यम्", resultTrue.value)

        // Test contains false
        val contextFalse = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.sankhya(1L, "एक"),
                        ExecutionExpression.sankhya(2L, "द्वि")
                    )
                ),
                Karaka.KARANA to ExecutionExpression.sankhya(3L, "त्रि")
            )
        )
        val resultFalse = containsOp.action.execute(contextFalse, containsOp)
        assertIs<ExecutionResult.Success>(resultFalse)
        assertEquals("असत्यम्", resultFalse.value)
    }

    @Test
    fun `VrtDhatu executes ForEachAction iteration loop`() {
        DhatuPathaRegistration.ensureRegistered()
        val vrt = DhatuPatha.all.first { it.id == "01.9910" }
        val foreachOp = vrt.operations.first { it.name == "प्रत्येकवृत्तिः" }

        val context = ExecutionContext(
            bindings = mapOf(
                Karaka.KARMAN to ExecutionExpression.Coordination(
                    listOf(
                        ExecutionExpression.sankhya(1L, "एक"),
                        ExecutionExpression.sankhya(2L, "द्वि"),
                        ExecutionExpression.sankhya(3L, "त्रि")
                    )
                ),
                Karaka.KARANA to ExecutionExpression.Pada("यु"), // exact body upadeśa; coordinated signature selects addition
                Karaka.SAMPRADANA to ExecutionExpression.sankhya(10L, "दश") // initial state
            )
        )

        val result = foreachOp.action.execute(context, foreachOp)
        assertIs<ExecutionResult.Success>(result)
        assertEquals(16L, (result.typedValue as SanskritValue.Sankhya).value)
    }
}
