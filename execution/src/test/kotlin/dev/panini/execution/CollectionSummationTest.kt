package dev.panini.execution

import kotlin.test.*

class CollectionSummationTest {
    @Test
    fun `whole member relation is not shadowed by a same named scalar`() {
        val scalar = SanskritValue.Sankhya(9, "नव")
        fun bind(sentence: String) = assertIs<ExecutionBindingResult.Bound>(
            dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", sentence),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                environment = ValueEnvironment(mapOf("सङ्ख्या" to scalar))))
            .ukti.invocations.single()
        for (phrase in listOf("सूची + ङस् सङ्ख्या + शस्", "सङ्ख्या + शस् सूची + ङस्")) {
            val invocation = bind("$phrase युज् + णिच् + लोट् + सिप् ।")
            assertEquals(CollectionMemberSelection.NUMBERS,
                assertIs<ExecutionExpression.Pada>(invocation.bindings[dev.panini.core.Karaka.KARMAN]).memberSelection)
        }
        val ordinary = bind("सङ्ख्या + अम् मुद्र् + णिच् + लोट् + सिप् ।")
        assertEquals(scalar,
            assertIs<ExecutionExpression.TypedOperand>(ordinary.bindings[dev.panini.core.Karaka.KARMAN]).value)
    }

    @Test
    fun `ordinary typed list parameter supplies the genitive whole`() {
        val results = PaniniVM().evalScript(
            "योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "मान + सुँ सूची + सुँ इति मान + सुँ ।\n" +
                "सङ्ख्या + सुँ इति परिणाम + सुँ ।\n" +
                "मान + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ॥\n" +
                "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + अम् योजन + टा डुकृञ् + उ + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals(6L, assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
    }

    @Test
    fun `nonfinite member sum supplies the omitted display object`() {
        val results = PaniniVM().evalScript(
            "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् सङ्ख्या + शस् युज् + णिच् + क्त्वा मुद्र् + णिच् + लोट् + सिप् ।",
        )
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals("षट्", assertIs<ExecutionResult.Success>(results.last()).value)
    }

    @Test
    fun `member selection survives blueprint codec round trip`() {
        val expression = ExecutionExpression.Pada("सङ्ख्या", memberSelection = CollectionMemberSelection.NUMBERS)
        assertEquals(expression, dev.panini.execution.sutra.ProgramSutraArthaCodec.decodeExpression(
            dev.panini.execution.sutra.ProgramSutraArthaCodec.encodeExpression(expression)))
    }

    @Test
    fun `number member sum rejects a word list and derived member nouns`() {
        for (source in listOf(
            "राम + ङस् सीता + ङस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् सङ्ख्या + शस् युज् + णिच् + लोट् + सिप् ।",
            "एक + ङस् द्वि + ओस् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                "सूची + ङस् सङ्ख्या + मतुप् + शस् युज् + णिच् + लोट् + सिप् ।",
        )) assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last(), source)
    }

    @Test
    fun `number members of a genitive list are summed`() {
        for (phrase in listOf("सूची + ङस् सङ्ख्या + शस्", "सङ्ख्या + शस् सूची + ङस्")) {
            val sentence = "$phrase युज् + णिच् + लोट् + सिप् ।"
            val bound = assertIs<ExecutionBindingResult.Bound>(dev.panini.execution.binding.VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", sentence),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्")))
            val invocation = bound.ukti.invocations.single()
            assertTrue(dev.panini.core.Karaka.SAMBANDHA in invocation.bindings, invocation.toString())
            assertEquals(CollectionMemberSelection.NUMBERS,
                (invocation.bindings[dev.panini.core.Karaka.KARMAN] as? ExecutionExpression.Pada)?.memberSelection,
                invocation.toString())
            val results = PaniniVM().evalScript(
                "एक + ङस् द्वि + ओस् त्रि + आम् च सूची + सुँ असँ + लट् + तिप् ।\n" +
                    "$phrase युज् + णिच् + लोट् + सिप् ।",
            )
            assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
            assertEquals(6L, assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(results.last()).typedValue).value)
        }
    }
}
