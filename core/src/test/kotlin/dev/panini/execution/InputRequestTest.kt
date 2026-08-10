package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class InputRequestTest {
    @Test
    fun `request encoding preserves choices and remains compatible with text requests`() {
        val choice = InputRequest("वर्ण", InputValueType.CHOICE, listOf("लोहित", "नील"))

        assertEquals(choice, InputRequest.decode(choice.encode()))
        assertEquals(
            InputRequest("नाम", InputValueType.TEXT),
            InputRequest.decode("PVM_INPUT\tTEXT\tनाम"),
        )
    }

    @Test
    fun `boolean validation accepts English and Sanskrit values`() {
        val request = InputRequest("अनुमत", InputValueType.BOOLEAN)

        assertEquals("true", assertIs<InputValidation.Valid>(request.validate("आम्")).value)
        assertEquals("true", assertIs<InputValidation.Valid>(request.validate("YES")).value)
        assertEquals("false", assertIs<InputValidation.Valid>(request.validate("नहीं")).value)
        assertEquals("false", assertIs<InputValidation.Valid>(request.validate("false")).value)
        assertIs<InputValidation.Invalid>(request.validate("perhaps"))
    }

    @Test
    fun `choice validation returns the declared spelling`() {
        val request = InputRequest("mode", InputValueType.CHOICE, listOf("Fast", "Safe"))

        assertEquals("Safe", assertIs<InputValidation.Valid>(request.validate(" safe ")).value)
        assertIs<InputValidation.Invalid>(request.validate("slow"))
    }

    @Test
    fun `input parsers accept Devanagari digits and reject unknown booleans`() {
        assertEquals(20, "२०".toInputLongOrNull())
        assertEquals(true, "सत्य".toInputBooleanOrNull())
        assertNull("maybe".toInputBooleanOrNull())
    }
}
