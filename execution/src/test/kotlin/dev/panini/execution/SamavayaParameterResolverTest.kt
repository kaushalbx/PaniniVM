package dev.panini.execution

import kotlin.test.Test
import kotlin.test.assertEquals

class SamavayaParameterResolverTest {

    @Test
    fun `replaces parsed accusative collection parameter`() {
        assertEquals(
            "एक + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।",
            SamavayaParameterResolver.replace(
                "समवाय+अम् युज् + णिच् + लोट् + सिप् ।",
                "एक + अम् द्वि + अम् च",
            ),
        )
    }

    @Test
    fun `does not replace collection operation or non accusative stem`() {
        val operation = "समवाय + ल्युट् + टा कृ + लोट् + सिप् ।"
        assertEquals(operation, SamavayaParameterResolver.replace(operation, "एक + अम्"))

        val nominative = "समवाय + सुँ युज् + णिच् + लोट् + सिप् ।"
        assertEquals(nominative, SamavayaParameterResolver.replace(nominative, "एक + अम्"))
    }
}
