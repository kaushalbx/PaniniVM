package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class DirectResultAssignmentTest {
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
