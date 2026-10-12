package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class DirectResultAssignmentTest {
    @Test fun `further derived action noun cannot alias base action history`() {
        for (affix in listOf("मतुप्", "तरप्")) {
            val reference = "युज् + ल्युट् + $affix + ङस् फल + अम् उत्तर + ङे दा + लोट् + सिप् ।"
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः $reference"
            assertEquals(emptyList(), DirectResultAssignment.suggestions(source))
            kotlin.test.assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
        }
    }

    @Test fun `competing action modifiers cannot select a result by adjacency`() {
        for (phrase in listOf("युज् + ल्युट् + ङस् भज् + ल्युट् + ङस् फल + अम्",
            "भज् + ल्युट् + ङस् फल + अम् युज् + ल्युट् + ङस्")) {
            val source = "$phrase उत्तर + ङे दा + लोट् + सिप् ।"
            val padas = dev.panini.vyakaranam.parser.PaniniParser().parse(source).grammaticalVakyas().single().padas
            kotlin.test.assertFailsWith<IllegalArgumentException> {
                dev.panini.execution.binding.NamedActionResultReferenceResolver.resolve(padas)
            }
            kotlin.test.assertIs<ExecutionResult.Failure>(PaniniVM().evalScript(source).last())
            assertEquals(emptyList(), DirectResultAssignment.suggestions(
                "युज् + णिच् + लोट् + सिप् ततः $source"))
        }
    }

    @Test fun `rewrite follows case roles rather than destination or modifier position`() {
        for (phrase in listOf(
            "उत्तर + ङे युज् + ल्युट् + ङस् फल + अम्",
            "फल + अम् उत्तर + ङे युज् + ल्युट् + ङस्",
            "युज् + ल्युट् + ङस् उत्तर + ङे फल + अम्",
        )) {
            val source = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः $phrase दा + लोट् + सिप् ।"
            val suggestion = DirectResultAssignment.suggestions(source).single()
            assertEquals("उत्तर + ङे दा + लोट् + सिप्", suggestion.replacement)
            val rewritten = source.replaceRange(suggestion.offset, suggestion.offset + suggestion.length, suggestion.replacement)
            fun result(program: String): SanskritValue? {
                val results = PaniniVM().evalScript(program)
                kotlin.test.assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
                return (results.last() as ExecutionResult.Success).typedValue
            }
            assertEquals(result(source), result(rewritten), phrase)
        }
    }

    @Test fun `rewrite cannot change the selected action history or ignore modifiers`() {
        for (clause in listOf(
            "भज् + ल्युट् + ङस् फल + अम् उत्तर + ङे दा + लोट् + सिप्",
            "युज् + ल्युट् + ङस् पूर्व + अम् फल + अम् उत्तर + ङे दा + लोट् + सिप्",
            "युज् + ल्युट् + ङस् फल + अम् उत्तर + ङे न दा + लोट् + सिप्",
            "युज् + ल्युट् + ङस् फल + अम् उत्तर + ङे दा + लट् + तिप्",
        )) assertEquals(emptyList(), DirectResultAssignment.suggestions(
            "युज् + णिच् + लोट् + सिप् ततः $clause ।"), clause)
    }

    @Test fun `derived result noun is not rewritten into an implicit pipeline`() {
        assertEquals(emptyList(), DirectResultAssignment.suggestions(
            "युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् युज् + ल्युट् + ङस् फल + त्व + अम् उत्तर + ङे ।"))
    }

    @Test
    fun `verbose phala assignment receives a direct pipeline replacement`() {
        val source = "युज् + णिच् + लोट् + सिप् ततः दा + लोट् + सिप् युज् + ल्युट् + ङस् फल + अम् उत्तर + ङे ।"
        val suggestion = DirectResultAssignment.suggestions(source).single()

        assertEquals(
            listOf("उत्तर + ङे दा + लोट् + सिप्"),
            listOf(suggestion.replacement),
        )
        assertEquals(
            "युज् + णिच् + लोट् + सिप् ततः उत्तर + ङे दा + लोट् + सिप् ।",
            source.replaceRange(suggestion.offset, suggestion.offset + suggestion.length, suggestion.replacement),
        )
    }

    @Test
    fun `direct assignment does not receive a suggestion`() {
        assertEquals(
            emptyList(),
            DirectResultAssignment.suggestions("युज् + लोट् + सिप् ततः उत्तर + ङे दा + लोट् + सिप् ।"),
        )
    }
}
